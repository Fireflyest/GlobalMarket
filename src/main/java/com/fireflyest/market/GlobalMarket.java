package com.fireflyest.market;

import org.fireflyest.craftcommand.argument.NumberArgs;
import org.fireflyest.craftcommand.argument.OfficePlayerArgs;
import org.fireflyest.craftcommand.argument.StringArgs;
import org.fireflyest.craftdatabase.sql.SQLConnector;
import org.fireflyest.craftgui.api.ViewGuide;

import com.fireflyest.market.command.MarketAdminCommand;
import com.fireflyest.market.command.MarketReloadCommand;
import com.fireflyest.market.command.MarketAffairCommand;
import com.fireflyest.market.command.MarketAuctionCommand;
import com.fireflyest.market.command.MarketBidCommand;
import com.fireflyest.market.command.MarketBlackCommand;
import com.fireflyest.market.command.MarketBuyCommand;
import com.fireflyest.market.command.MarketCancelCommand;
import com.fireflyest.market.command.MarketCategoryCommand;
import com.fireflyest.market.command.MarketCheckCommand;
import com.fireflyest.market.command.MarketCommand;
import com.fireflyest.market.command.MarketEditCommand;
import com.fireflyest.market.command.MarketFinishCommand;
import com.fireflyest.market.command.MarketHomeCommand;
import com.fireflyest.market.command.MarketMailCommand;
import com.fireflyest.market.command.MarketMineCommand;
import com.fireflyest.market.command.MarketOrderCommand;
import com.fireflyest.market.command.MarketRepriceCommand;
import com.fireflyest.market.command.MarketSaleCommand;
import com.fireflyest.market.command.MarketSearchCommand;
import com.fireflyest.market.command.MarketSellCommand;
import com.fireflyest.market.command.MarketSendCommand;
import com.fireflyest.market.command.MarketSignCommand;
import com.fireflyest.market.command.MarketSizeCommand;
import com.fireflyest.market.command.MarketStarCommand;
import com.fireflyest.market.command.MarketStoreCommand;
import com.fireflyest.market.command.MarketVisitCommand;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.data.StateCache;
import com.fireflyest.market.listener.PlayerEventListener;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskCancel;
import com.fireflyest.market.task.TaskHeat;
import com.fireflyest.market.view.AffairView;
import com.fireflyest.market.view.CategoryView;
import com.fireflyest.market.view.EditView;
import com.fireflyest.market.view.HomeView;
import com.fireflyest.market.view.MailView;
import com.fireflyest.market.view.MainView;
import com.fireflyest.market.view.ManageView;
import com.fireflyest.market.view.MineView;
import com.fireflyest.market.view.SearchView;
import com.fireflyest.market.view.StoreView;
import com.fireflyest.market.view.VisitView;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.fireflyest.craftgui.util.TranslateUtils;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.util.TimeUtils;

/**
 * @author Fireflyest
 * 2022/2/15 22:05
 */
public class GlobalMarket extends JavaPlugin{

    /*
    材质
    排行榜 4
    价格统计 7
    交易 8
    收购 9 √
    统计书 √
    多语言 自定义文本 √
    自定义按钮 √
    拍卖 √
    点券支持 √
    界面交互 √
    商品编辑 √
    快捷上架 √
     */

    public static final String TASK_MARKET = "task.market";
    public static final String TASK_MAIL = "task.mail";

    public static final String MAIN_VIEW = "market.main";
    public static final String MAIL_VIEW = "market.mail";
    public static final String MINE_VIEW = "market.mine";
    public static final String VISIT_VIEW = "market.visit";
    public static final String HOME_VIEW = "market.home";
    public static final String MANAGE_VIEW = "market.manage";
    public static final String STORE_VIEW = "market.store";
    public static final String CATEGORY_VIEW = "market.category";
    public static final String AFFAIR_VIEW = "market.affair";
    public static final String EDIT_VIEW = "market.edit";
    public static final String SEARCH_VIEW = "market.search";

    private MarketService service;
    private MarketYaml yaml;
    private MarketEconomy economy;
    private TaskHandler handler;
    private ViewGuide guide;
    private BukkitTask marketTask;
    private StateCache cache;
    private String url;

    @Override
    public void onEnable() {
        // 统计
        new Metrics(this, 15549);

        // 数据
        this.getLogger().info("Enable data service.");
        yaml = new MarketYaml(this);
        try {
            if (Config.SQL) {
                url = Config.URL;
                SQLConnector.setupConnect(SQLConnector.MYSQL, url, Config.USER, Config.PASSWORD);
            } else {
                url = "jdbc:sqlite:" + getDataFolder().getParent() + "/" + this.getClass().getSimpleName() + "/storage.db";
                SQLConnector.setupConnect(SQLConnector.SQLITE, url, null, null);
            }
            service = new MarketService(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setupHandler();
        this.setupGuide();
        
        economy = new MarketEconomy();
        cache = new StateCache();
        
        // 注册事件
        this.getServer().getPluginManager().registerEvents(new PlayerEventListener(service, guide, handler), this);

        // 注册指令
        PluginCommand marketAdmin = this.getCommand("marketadmin");
        if (marketAdmin != null) {
            MarketAdminCommand marketAdminCommand = new MarketAdminCommand(guide);
            MarketBlackCommand marketBlackCommand = new MarketBlackCommand(service);
            marketBlackCommand.setArgument(0, new OfficePlayerArgs());
            marketBlackCommand.setArgument(0, new StringArgs("0", "1"));
            MarketCheckCommand marketCheckCommand = new MarketCheckCommand();
            MarketReloadCommand marketAdminReloadCommand = new MarketReloadCommand(yaml);
            MarketSizeCommand marketSizeCommand = new MarketSizeCommand(service);
            marketSizeCommand.setArgument(0, new OfficePlayerArgs());
            marketSizeCommand.setArgument(1, new NumberArgs());
            marketAdminCommand.addSubCommand("black", marketBlackCommand);
            marketAdminCommand.addSubCommand("check", marketCheckCommand);
            marketAdminCommand.addSubCommand("reload", marketAdminReloadCommand);
            marketAdminCommand.addSubCommand("size", marketSizeCommand);
            marketAdmin.setExecutor(marketAdminCommand);
            marketAdmin.setTabCompleter(marketAdminCommand);
        }
        PluginCommand market = this.getCommand("market");
        if (market != null) {
            StringArgs currencyArgs = new StringArgs("coin", "point", "item");
            MarketCommand marketCommand = new MarketCommand(guide);
            MarketAffairCommand marketAffairCommand = new MarketAffairCommand(service, economy, guide, handler);
            marketAffairCommand.setArgument(0, new NumberArgs());
            MarketAuctionCommand marketAuctionCommand = new MarketAuctionCommand(service, guide, handler);
            marketAuctionCommand.setArgument(0, new NumberArgs());
            marketAuctionCommand.setArgument(1, new NumberArgs());
            marketAuctionCommand.setArgument(2, currencyArgs);
            MarketBidCommand marketBidCommand = new MarketBidCommand(service, economy, guide, handler);
            marketBidCommand.setArgument(0, new NumberArgs());
            marketBidCommand.setArgument(1, new NumberArgs());
            MarketBuyCommand marketBuyCommand = new MarketBuyCommand(service, economy, guide, handler);
            marketBuyCommand.setArgument(0, new NumberArgs());
            marketBuyCommand.setArgument(1, new NumberArgs());
            MarketCancelCommand marketCancelCommand = new MarketCancelCommand(service, guide, handler);
            marketCancelCommand.setArgument(0, new NumberArgs());
            MarketCategoryCommand marketCategoryCommand = new MarketCategoryCommand(guide);
            marketCategoryCommand.setArgument(0, new StringArgs("category1", "category2", "category3", "category4", "category5", "category6", "category7"));
            MarketEditCommand marketEditCommand = new MarketEditCommand(guide);
            marketEditCommand.setArgument(0, new NumberArgs());
            MarketFinishCommand marketFinishCommand = new MarketFinishCommand(service, economy, guide, handler);
            marketFinishCommand.setArgument(0, new NumberArgs());
            MarketHomeCommand marketHomeCommand = new MarketHomeCommand(guide);
            MarketMailCommand  marketMailCommand = new MarketMailCommand(guide);
            MarketOrderCommand marketOrderCommand = new MarketOrderCommand(service, guide, handler);
            marketOrderCommand.setArgument(0, new NumberArgs());
            marketOrderCommand.setArgument(1, new NumberArgs());
            marketOrderCommand.setArgument(2, currencyArgs);
            MarketMineCommand marketMineCommand = new MarketMineCommand(guide);
            MarketRepriceCommand marketRepriceCommand = new MarketRepriceCommand(service, guide, handler);
            marketRepriceCommand.setArgument(0, new NumberArgs());
            marketRepriceCommand.setArgument(1, new NumberArgs());
            MarketSaleCommand marketSaleCommand = new MarketSaleCommand(service, economy, guide, handler);
            marketSaleCommand.setArgument(0, new NumberArgs());
            marketSaleCommand.setArgument(1, new NumberArgs());
            MarketSearchCommand marketSearchCommand = new MarketSearchCommand(guide);
            marketSearchCommand.setArgument(0, new StringArgs("[...]"));
            MarketSellCommand marketSellCommand = new MarketSellCommand(service, guide, handler);
            marketSellCommand.setArgument(0, new NumberArgs());
            marketSellCommand.setArgument(1, new NumberArgs());
            marketSellCommand.setArgument(2, currencyArgs);
            MarketSendCommand marketSendCommand = new MarketSendCommand(service, handler);
            marketSendCommand.setArgument(0, new OfficePlayerArgs());
            marketSendCommand.setArgument(1, new NumberArgs());
            marketSendCommand.setArgument(2, new StringArgs("<...>"));
            MarketSignCommand marketSignCommand = new MarketSignCommand(service, economy, guide, handler);
            marketSignCommand.setArgument(0, new NumberArgs());
            MarketStarCommand marketStarCommand = new MarketStarCommand(service, cache);
            marketStarCommand.setArgument(0, new OfficePlayerArgs());
            MarketStoreCommand marketStoreCommand = new MarketStoreCommand(service);
            marketStoreCommand.setArgument(0, new StringArgs("[...]"));
            MarketVisitCommand marketVisitCommand = new MarketVisitCommand(service, guide, cache);
            marketVisitCommand.setArgument(0, new OfficePlayerArgs());
            marketCommand.addSubCommand("affair", marketAffairCommand);
            marketCommand.addSubCommand("auction", marketAuctionCommand);
            marketCommand.addSubCommand("bid", marketBidCommand);
            marketCommand.addSubCommand("buy", marketBuyCommand);
            marketCommand.addSubCommand("cancel", marketCancelCommand);
            marketCommand.addSubCommand("category", marketCategoryCommand);
            marketCommand.addSubCommand("edit", marketEditCommand);
            marketCommand.addSubCommand("finish", marketFinishCommand);
            marketCommand.addSubCommand("home", marketHomeCommand);
            marketCommand.addSubCommand("mail", marketMailCommand);
            marketCommand.addSubCommand("mine", marketMineCommand);
            marketCommand.addSubCommand("order", marketOrderCommand);
            marketCommand.addSubCommand("reprice", marketRepriceCommand);
            marketCommand.addSubCommand("sale", marketSaleCommand);
            marketCommand.addSubCommand("search", marketSearchCommand);
            marketCommand.addSubCommand("sell", marketSellCommand);
            marketCommand.addSubCommand("send", marketSendCommand);
            marketCommand.addSubCommand("sign", marketSignCommand);
            marketCommand.addSubCommand("star", marketStarCommand);
            marketCommand.addSubCommand("store", marketStoreCommand);
            marketCommand.addSubCommand("visit", marketVisitCommand);
            market.setExecutor(marketCommand);
            market.setTabCompleter(marketCommand);
        }

        // 若有自动下架，建立监控线程
        if(Config.TERM_OF_VALIDITY == -1){
            return;
        }
        final long limit = (long) Config.TERM_OF_VALIDITY * 1000 * 60 * 60 * 24;
        // 20mc刻为一秒
        marketTask = new BukkitRunnable() {
            @Override
            public void run() {
                long deadline = TimeUtils.getTime() - limit;
                // 超时下架
                for (long id : service.selectTransactionCancel(deadline)) {
                    handler.putTasks(TASK_MARKET, new TaskCancel(service.selectTransactionOwnerName(id), service, guide, id));
                }
                // 降热度
                for (long id : service.selectTransactionIdByType("")) {
                    handler.putTasks(TASK_MARKET, new TaskHeat(service.selectTransactionOwnerName(id), service, economy, guide, id, -1));
                }
                MarketItem.setHeatRank(service.selectTransactionIdByHeat(10));
            }
        }.runTaskTimerAsynchronously(this, 20 * 60L, 20 * 60 * 30L); // 每三十分钟
    }

    @Override
    public void onDisable() {
        // 关闭数据库
        if (service != null) {
            SQLConnector.close(url);
        }
        Bukkit.getScheduler().cancelTasks(this);

        // 自动下架监控
        if (marketTask != null) marketTask.cancel();
    }


    public void setupHandler() {
        RegisteredServiceProvider<TaskHandler> rsp = Bukkit.getServer().getServicesManager().getRegistration(TaskHandler.class);
        if (rsp == null) {
            this.getLogger().warning("TaskHandler not found!");
            return;
        }
        handler = rsp.getProvider();
        handler.createWorker(TASK_MARKET, this);
        handler.createWorker(TASK_MAIL, this);
    }

    /**
     * 界面初始化
     */
    public void setupGuide() {

        TranslateUtils.setLanguage(Config.LANG);

        RegisteredServiceProvider<ViewGuide> rsp = Bukkit.getServer().getServicesManager().getRegistration(ViewGuide.class);
        if (rsp == null) {
            this.getLogger().warning("GUI not found!");
            return;
        }
        guide = rsp.getProvider();

        guide.addView(MAIN_VIEW, new MainView(service, yaml));
        guide.addView(MAIL_VIEW, new MailView(service, yaml));
        guide.addView(MINE_VIEW, new MineView(service, yaml));
        guide.addView(VISIT_VIEW, new VisitView(service, yaml));
        guide.addView(HOME_VIEW, new HomeView(yaml));
        guide.addView(MANAGE_VIEW, new ManageView(yaml));
        guide.addView(STORE_VIEW, new StoreView(service, yaml));
        guide.addView(CATEGORY_VIEW, new CategoryView(service, yaml));
        guide.addView(AFFAIR_VIEW, new AffairView(service, yaml));
        guide.addView(EDIT_VIEW, new EditView(service, yaml, guide, handler));
        guide.addView(SEARCH_VIEW, new SearchView(service, yaml));
    }

    public static GlobalMarket getPlugin() {
        return getPlugin(GlobalMarket.class);
    }

}
