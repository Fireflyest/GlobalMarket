package com.fireflyest.market;

import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.task.TaskCancel;
import com.fireflyest.market.task.TaskFinish;
import org.fireflyest.craftgui.api.ViewGuide;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Note;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.command.MarketCommand;
import com.fireflyest.market.command.MarketTab;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.Storage;
import com.fireflyest.market.listener.PlayerEventListener;
import com.fireflyest.market.sql.SqlData;
import com.fireflyest.market.sql.SqlStorage;
import com.fireflyest.market.sqll.SqLiteData;
import com.fireflyest.market.sqll.SqLiteStorage;
import com.fireflyest.market.util.TimeUtils;
import com.fireflyest.market.util.YamlUtils;
import com.fireflyest.market.view.*;
import net.milkbowl.vault.economy.Economy;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.sql.SQLException;

/**
 * @author Fireflyest
 * 2022/2/15 22:05
 */
public class GlobalMarket extends JavaPlugin{

    /*
    点券支持
    邮箱交费
    价格统计
    交易
    收购
    界面交互
    排行榜
     */

    public static final String MAIN_VIEW = "market.main";
    public static final String MAIL_VIEW = "market.mail";
    public static final String MINE_VIEW = "market.mine";
    public static final String OTHER_VIEW = "market.other";
    public static final String HOME_VIEW = "market.home";
    public static final String CLASSIFY_VIEW = "market.classify";
    public static final String AFFAIR_VIEW = "market.affair";
    public static final String SELL_VIEW = "market.sell";
    public static final String SEARCH_VIEW = "market.search";

    private static JavaPlugin plugin;

    private static Storage storage;
    private static Data data;
    private static Economy economy;
    private static ViewGuide guide;

    private BukkitTask marketTask;

    public static Data getData() {
        return data;
    }
    public static Storage getStorage() {
        return storage;
    }
    public static Economy getEconomy() {
        if (economy == null) setupEconomy();
        return economy;
    }
    public static ViewGuide getGuide() {
        return guide;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;

        // 统计
        new Metrics(this, 15549);

        this.setupData();
        this.setupGuide();

        // 注册事件
        this.getServer().getPluginManager().registerEvents( new PlayerEventListener(), this);

        // 注册指令
        PluginCommand command = this.getCommand("market");
        if(command!=null){
            command.setExecutor(new MarketCommand());
            command.setTabCompleter(new MarketTab());
        }

        // 初始化管理
        MarketManager.initMarketManager();
        // 任务处理线程
        MarketTasks.initMarketTasks(this);

        // 若有自动下架，建立监控线程
        if(Config.LIMIT_TIME == -1) return;
        // 20mc刻为一秒
        marketTask = new BukkitRunnable() {
            @Override
            public void run() {
                long limit = (long) Config.LIMIT_TIME * 1000 * 60 * 60 * 24;
                long now = TimeUtils.getDate();
                // 找出非系统商店的判断是否超时
                MarketManager.getSales().stream().filter(sale -> !sale.isAdmin()).forEach(sale -> {
                    long delta = now - sale.getAppear();
                    // 判断是否超时
                    if (delta > limit) {
                        if (sale.isAuction()){
                            // 结束竞拍
                            MarketTasks.getTaskManager().putTask(new TaskFinish(sale.getOwner(), sale.getId()));
                        }else {
                            // 下架
                            MarketTasks.getTaskManager().putTask(new TaskCancel(sale.getOwner(), sale.getId()));
                        }
                    }
                });
            }
        }.runTaskTimerAsynchronously(this, 20 * 10, 20 * 60 * 30); // 每三十分钟
    }

    @Override
    public void onDisable() {
        try {
            storage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MarketTasks.close();
        Bukkit.getScheduler().cancelTasks(this);

        // 自动下架监控
        if (marketTask != null) {
            marketTask.cancel();
        }
    }

    private void setupData(){
        YamlUtils.iniYamlUtils(plugin);

        if(Config.SQL){
            if(Config.DEBUG) Bukkit.getLogger().info("使用数据库存储");
            // 数据库访问对象
            try {
                storage = new SqlStorage(Config.URL, Config.USER, Config.PASSWORD);
                data = new SqlData(storage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            if(Config.DEBUG) Bukkit.getLogger().info("使用本地存储");
            // 本地数据库访问对象
            String url = "jdbc:sqlite:" + getDataFolder() + "/storage.db";

            try {
                storage = new SqLiteStorage(url);
                data = new SqLiteData(storage);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        data.createTable(User.class);
        data.createTable(Sale.class);
        data.createTable(Mail.class);
        data.createTable(Note.class);
    }

    /**
     * 界面初始化
     */
    private void setupGuide() {
        RegisteredServiceProvider<ViewGuide> rsp = Bukkit.getServer().getServicesManager().getRegistration(ViewGuide.class);
        if (rsp == null) {
            Bukkit.getLogger().warning("Gui not found!");
            return;
        }
        guide = rsp.getProvider();

        guide.addView(MAIN_VIEW, new MainView(Language.PLUGIN_NAME));
        guide.addView(MAIL_VIEW, new MailView(Language.PLUGIN_NAME));
        guide.addView(MINE_VIEW, new MineView(Language.PLUGIN_NAME));
        guide.addView(OTHER_VIEW, new OtherView(Language.PLUGIN_NAME));
        guide.addView(HOME_VIEW, new HomeView(Language.PLUGIN_NAME));
        guide.addView(CLASSIFY_VIEW, new ClassifyView(Language.PLUGIN_NAME));
        guide.addView(AFFAIR_VIEW, new AffairView(Language.PLUGIN_NAME));
        guide.addView(SELL_VIEW, new SellView(Language.PLUGIN_NAME));
        guide.addView(SEARCH_VIEW, new SearchView(Language.PLUGIN_NAME));
    }

    /**
     * 经济插件
     */
    private static void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getLogger().warning("Economy not found!");
            return;
        }
        economy = rsp.getProvider();
    }
}
