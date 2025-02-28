package com.fireflyest.market;

import io.fireflyest.emberlib.cache.CacheOrganism;
import io.fireflyest.emberlib.command.ComplexCommand;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.command.args.ObjectArgs;
import io.fireflyest.emberlib.command.args.OfficePlayerArgs;
import io.fireflyest.emberlib.database.DatabaseConnector;
import io.fireflyest.emberlib.inventory.ViewGuide;
import com.fireflyest.market.command.DeliveryArgs;
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
import com.fireflyest.market.command.MarketCollateCommand;
import com.fireflyest.market.command.MarketCommand;
import com.fireflyest.market.command.MarketEditCommand;
import com.fireflyest.market.command.MarketFinishCommand;
import com.fireflyest.market.command.MarketHelpCommand;
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
import com.fireflyest.market.command.TransactionArgs;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.listener.PlayerEventListener;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskCancel;
import com.fireflyest.market.task.TaskTimer;
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
import java.util.UUID;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.h;

import io.fireflyest.emberlib.task.TaskHandler;
import io.fireflyest.emberlib.util.TimeUtils;
import io.fireflyest.emberlib.util.YamlUtils;

/**
 * a global market plugin
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class GlobalMarket extends JavaPlugin {

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
    private MarketEconomy economy;
    private TaskHandler handler;
    private ViewGuide guide;
    private BukkitTask marketTask;
    private CacheOrganism cache;
    private String url;

    @Override
    public void onEnable() {
        // 统计
        new Metrics(this, 15549);

        // 数据
        this.getLogger().info("Enable data service.");
        YamlUtils.loadToClass(this, Config.class, "config.yml");
        YamlUtils.loadToClass(this, Language.class, "lang/" + Config.LANG.get() + ".yml");
        try {
            if (Config.SQL_ENABLE.get().booleanValue()) {
                url = Config.SQL_URL.get();
                DatabaseConnector.setupConnect(
                    DatabaseConnector.MYSQL, url, Config.SQL_USER.get(), Config.SQL_PASSWORD.get());
            } else {
                url = "jdbc:sqlite:" + getDataFolder().getParent() 
                    + "/" + this.getClass().getSimpleName() + "/storage.db";
                DatabaseConnector.setupConnect(DatabaseConnector.SQLITE, url, null, null);
            }
            service = new MarketService(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setupHandler();
        this.setupGuide();
        
        economy = new MarketEconomy();
        cache = new CacheOrganism("cache");
        cache.load(this);
        
        // 注册事件
        this.getServer()
            .getPluginManager()
            .registerEvents(new PlayerEventListener(service, guide, handler), this);

        // 注册指令
        final ComplexCommand marketAdminCommand = new MarketAdminCommand(guide).name("marketadmin");
        final SubCommand blackCommand = new MarketBlackCommand(service)
            .addArg(new OfficePlayerArgs())
            .addArg(new ObjectArgs("0", "1"));
        final SubCommand sizeCommand = new MarketSizeCommand(service)
            .addArg(new OfficePlayerArgs())
            .addArg(new ObjectArgs(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16));
        marketAdminCommand.addSub("black", blackCommand)
            .addSub("size", sizeCommand)
            .addSub("check", new MarketCheckCommand())
            .addSub("collate", new MarketCollateCommand(service))
            .addSub("reload", new MarketReloadCommand())
            .apply(this);
        
        final ComplexCommand makretCommand = new MarketCommand(guide).name("market");
        final ObjectArgs currencyArgs = new ObjectArgs("coin", "point", "item");
        final TransactionArgs playerTransactionArgs = new TransactionArgs(service, null);
        final TransactionArgs retailTransactionArgs = new TransactionArgs(service, "retail");
        final TransactionArgs auctionTransactionArgs = new TransactionArgs(service, "auction");
        final TransactionArgs orderTransactionArgs = new TransactionArgs(service, "order");
        final DeliveryArgs deliveryArgs = new DeliveryArgs(service);
        final SubCommand marketAffairCommand = 
            new MarketAffairCommand(service, guide, cache, handler).addArg(retailTransactionArgs);
        final SubCommand marketAuctionCommand = new MarketAuctionCommand(service, guide, handler)
            .addArg(new ObjectArgs("价格", 9, 998, 666))
            .addArg(new ObjectArgs("数量", 1, 16, 64))
            .addArg(currencyArgs);
        final SubCommand marketBidCommand = new MarketBidCommand(service, economy, guide, handler)
            .addArg(auctionTransactionArgs)
            .addArg(new ObjectArgs("金额", 10, 100, 1000));
        final SubCommand marketBuyCommand = new MarketBuyCommand(service, economy, guide, handler)
            .addArg(retailTransactionArgs)
            .addArg(new ObjectArgs("数量", 1, 16, 64));
        final SubCommand marketCancelCommand = new MarketCancelCommand(service, guide, handler)
            .addArg(playerTransactionArgs);
        final SubCommand marketCategoryCommand = new MarketCategoryCommand(guide)
            .addArg(new ObjectArgs("1", "2", "3", "4", "5", "6", "7"));
        final SubCommand marketEditCommand = new MarketEditCommand(guide)
            .addArg(playerTransactionArgs);
        final SubCommand marketFinishCommand = 
            new MarketFinishCommand(service, economy, guide, handler).addArg(playerTransactionArgs);
        final SubCommand marketOrderCommand = new MarketOrderCommand(service, guide, handler)
            .addArg(new ObjectArgs("价格", 9, 998, 666))
            .addArg(new ObjectArgs("数量", 1, 16, 64))
            .addArg(currencyArgs);
        final SubCommand marketRepriceCommand = new MarketRepriceCommand(service, guide, handler)
            .addArg(playerTransactionArgs)
            .addArg(new ObjectArgs("价格", 9, 998, 666));
        final SubCommand marketSaleCommand = new MarketSaleCommand(service, economy, guide, handler)
            .addArg(orderTransactionArgs)
            .addArg(new ObjectArgs("数量", 1, 16, 64));
        final SubCommand marketSearchCommand = new MarketSearchCommand(guide)
            .addArg(new ObjectArgs("[name], [lore]"));
        final SubCommand marketSellCommand = new MarketSellCommand(service, guide, handler)
            .addArg(new ObjectArgs("价格", 9, 998, 666))
            .addArg(new ObjectArgs("数量", 1, 16, 64))
            .addArg(currencyArgs);
        final SubCommand marketSendCommand = new MarketSendCommand(service, guide, handler)
            .addArg(new OfficePlayerArgs())
            .addArg(new ObjectArgs("数量", 1, 16, 64))
            .addArg(new ObjectArgs("附言"));
        final SubCommand marketSignCommand = new MarketSignCommand(service, economy, guide, handler)
            .addArg(deliveryArgs);
        final SubCommand marketStarCommand = new MarketStarCommand(service, cache)
            .addArg(new OfficePlayerArgs());
        final SubCommand marketStoreCommand = new MarketStoreCommand(service)
            .addArg(new OfficePlayerArgs());
        final SubCommand marketVisitCommand = new MarketVisitCommand(service, guide, cache)
            .addArg(new OfficePlayerArgs());
        makretCommand.addSub("affair", marketAffairCommand)
            .addSub("auction", marketAuctionCommand)
            .addSub("bid", marketBidCommand)
            .addSub("buy", marketBuyCommand)
            .addSub("cancel", marketCancelCommand)
            .addSub("category", marketCategoryCommand)
            .addSub("edit", marketEditCommand)
            .addSub("finish", marketFinishCommand)
            .addSub("help", new MarketHelpCommand())
            .addSub("home", new MarketHomeCommand(guide))
            .addSub("mail", new MarketMailCommand(guide))
            .addSub("mine", new MarketMineCommand(guide))
            .addSub("order", marketOrderCommand)
            .addSub("reprice", marketRepriceCommand)
            .addSub("sale", marketSaleCommand)
            .addSub("search", marketSearchCommand)
            .addSub("sell", marketSellCommand)
            .addSub("send", marketSendCommand)
            .addSub("sign", marketSignCommand)
            .addSub("star", marketStarCommand)
            .addSub("store", marketStoreCommand)
            .addSub("visit", marketVisitCommand)
            .apply(this);
        
        
        final long limit = (long) Config.TRANSACTION_EXPIRATION.get() * 1000 * 60 * 60 * 24;
        final long prepare = 1000 * 60 * 60 * 3L;
        // 20mc刻为一秒
        marketTask = new BukkitRunnable() {
            @Override
            public void run() {
                final long deadline = TimeUtils.getTime() - limit;
                final long prepareDeadline = TimeUtils.getTime() - prepare;
                // 超时下架
                if (Config.TRANSACTION_EXPIRATION.get() != -1) {
                    for (long id : service.selectTransactionCancel(deadline)) {
                        final UUID owner = UUID.fromString(service.selectTransactionOwner(id));
                        handler.putTasks(TASK_MARKET, new TaskCancel(owner, service, guide, id));
                    }
                }
                // 预售超时
                for (long id : service.selectTransactionPrepareCancel(prepareDeadline)) {
                    final UUID owner = UUID.fromString(service.selectTransactionOwner(id));
                    handler.putTasks(TASK_MARKET, new TaskCancel(owner, service, guide, id));
                }
                // 降热度
                final long[] ids = service.selectTransactionIdByType("");
                if (ids.length > 0) {
                    final UUID owner = UUID.fromString(service.selectTransactionOwner(ids[0]));
                    handler.putTasks(TASK_MARKET, 
                        new TaskTimer(owner, service, economy, guide, ids));
                    
                }
                MarketItem.setHeatRank(service.selectTransactionIdByHeat(10));
            }
        }.runTaskTimerAsynchronously(this, 20 * 60L, 20 * 60 * 30L); // 每三十分钟
    }

    @Override
    public void onDisable() {
        // 关闭数据库
        if (service != null) {
            DatabaseConnector.close(url);
        }
        Bukkit.getScheduler().cancelTasks(this);

        // 工作队列
        if (handler != null) {
            handler.removeWorker(TASK_MARKET);
            handler.removeWorker(TASK_MAIL);
        }

        // 自动下架监控
        if (marketTask != null) {
            marketTask.cancel();
        }

        // 缓存
        if (cache != null) {
            cache.save(this);
        }
    }

    /**
     * 注册任务处理器
     */
    public void setupHandler() {
        final RegisteredServiceProvider<TaskHandler> rsp = Bukkit.getServer()
            .getServicesManager()
            .getRegistration(TaskHandler.class);
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
        final RegisteredServiceProvider<ViewGuide> rsp = Bukkit.getServer()
            .getServicesManager()
            .getRegistration(ViewGuide.class);
        if (rsp == null) {
            this.getLogger().warning("GUI not found!");
            return;
        }
        guide = rsp.getProvider();

        guide.addView(MAIN_VIEW, new MainView(service));
        guide.addView(MAIL_VIEW, new MailView(service));
        guide.addView(MINE_VIEW, new MineView(service, guide, handler));
        guide.addView(VISIT_VIEW, new VisitView(service));
        guide.addView(HOME_VIEW, new HomeView());
        guide.addView(MANAGE_VIEW, new ManageView());
        guide.addView(STORE_VIEW, new StoreView(service));
        guide.addView(CATEGORY_VIEW, new CategoryView(service));
        guide.addView(AFFAIR_VIEW, new AffairView(service));
        guide.addView(EDIT_VIEW, new EditView(service, guide, handler));
        guide.addView(SEARCH_VIEW, new SearchView(service));
    }

    public static GlobalMarket getPlugin() {
        return getPlugin(GlobalMarket.class);
    }

}
