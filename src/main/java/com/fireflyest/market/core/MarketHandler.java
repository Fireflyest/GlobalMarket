package com.fireflyest.market.core;

import org.fireflyest.craftgui.api.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ChatUtils;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import com.fireflyest.market.util.TranslateUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Fireflyest
 * 2021/3/30 14:39
 */

public class MarketHandler implements MarketInteract{

    private static final ArrayBlockingQueue<MarketTask> TRADE_QUEUE = new ArrayBlockingQueue<>(64);

    private static final ArrayBlockingQueue<MarketTask> MAIL_QUEUE = new ArrayBlockingQueue<>(64);

    private static final MarketHandler marketHandler = new MarketHandler();

    private Economy economy;
    private ViewGuide guide;
//    private Economy pointEconomy;

    private BukkitTask tradeLooper;
    private BukkitTask mailLooper;
    private boolean enable;

    private Data data;
    
    public static MarketHandler getInstance() {
        return marketHandler;
    }

    private MarketHandler(){
    }

    public void createTaskHandler(JavaPlugin plugin){
        data = GlobalMarket.getData();
        economy = GlobalMarket.getEconomy();
        guide = GlobalMarket.getGuide();
        enable = true;

        // 开启两个线程运行队列
        tradeLooper = new BukkitRunnable() {
            @Override
            public void run() {
                while (enable) {
                    try {
                        if (TRADE_QUEUE.isEmpty()) {
                            synchronized (TRADE_QUEUE) {
                                TRADE_QUEUE.wait();
                            }
                            continue;
                        }
                        executeTradeTask(TRADE_QUEUE.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.cancel();
            }
        }.runTaskAsynchronously(plugin);

        mailLooper = new BukkitRunnable() {
            @Override
            public void run() {
                while (enable) {
                    try {
                        if (MAIL_QUEUE.isEmpty()) {
                            synchronized (MAIL_QUEUE) {
                                MAIL_QUEUE.wait();
                            }
                            continue;
                        }
                        executeMailTask(MAIL_QUEUE.take());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void stop(){
        enable = false;
        synchronized (TRADE_QUEUE){
            TRADE_QUEUE.notify();
        }
        synchronized (MAIL_QUEUE){
            MAIL_QUEUE.notify();
        }
        tradeLooper.cancel();
        mailLooper.cancel();
        tradeLooper = null;
        mailLooper = null;
    }

    private void executeTradeTask(MarketTask task){
        switch (task.type){
            case MarketTask.BUY:
                this.affairBuy(task.player, task.id, task.amount);
                break;
            case MarketTask.SELL:
                this.affairSell(task.name, task.auction, task.point, task.price, task.item);
                break;
            case MarketTask.CANCEL:
                this.affairCancel(task.player, task.id);
                break;
            case MarketTask.FINISH:
                this.affairFinish(task.player, task.id);
                break;
            case MarketTask.AUCTION:
                this.affairAuction(task.player, task.id, task.amount);
                break;
            case MarketTask.REPRICE:
                this.affairReprice(task.player, task.id, task.price);
                break;
            case MarketTask.DISCOUNT:
                this.affairDiscount(task.player, task.id, task.amount);
                break;
            default:
        }

    }

    private void executeMailTask(MarketTask task){
        switch (task.type){
            case MarketTask.SEND:
                this.affairSend(task.name, task.item, task.price, task.point);
                break;
            case MarketTask.SIGN:
                this.affairSign(task.player, task.id);
                break;
            case MarketTask.SIGN_ALL:
                this.affairSignAll(task.player);
                break;
            default:
        }
    }

    public MarketTask obtainTask(int type, int id){
        MarketTask task = new MarketTask();
        task.type = type;
        task.id = id;
        return task;
    }

    public MarketTask obtainTask(int type, Player player, int id){
        MarketTask task = new MarketTask();
        task.type = type;
        task.player = player;
        task.id = id;
        return task;
    }

    public MarketTask obtainBuyTask(int type, Player player, int id, int amount){
        MarketTask task = new MarketTask();
        task.type = type;
        task.player = player;
        task.id = id;
        task.amount = amount;
        return task;
    }

    public MarketTask obtainSignTask(Player player){
        MarketTask task = new MarketTask();
        task.type = MarketTask.SIGN_ALL;
        task.player = player;
        return task;
    }

    public MarketTask obtainSellTask(String name, boolean auction, boolean point, double price, ItemStack item){
        MarketTask task = new MarketTask();
        task.type = MarketTask.SELL;
        task.name = name;
        task.auction = auction;
        task.point = point;
        task.price = price;
        task.item = item;
        return task;
    }

    public MarketTask obtainDiscountTask(Player player, int id, int discount){
        MarketTask task = new MarketTask();
        task.type = MarketTask.DISCOUNT;
        task.player = player;
        task.id = id;
        task.amount = discount;
        return task;
    }

    public MarketTask obtainRepriceTask(Player player, int id, double price){
        MarketTask task = new MarketTask();
        task.type = MarketTask.REPRICE;
        task.player = player;
        task.id = id;
        task.price = price;
        return task;
    }

    public MarketTask obtainMailTask(String to, ItemStack item){
        MarketTask task = new MarketTask();
        task.type = MarketTask.SEND;
        task.name = to;
        task.item = item;
        return task;
    }

    public MarketTask obtainMailTask(String to, ItemStack item, double price, boolean point){
        MarketTask task = new MarketTask();
        task.type = MarketTask.SEND;
        task.name = to;
        task.item = item;
        task.price = price;
        task.point = point;
        return task;
    }

    public void sendTask(MarketTask task){
        try {
            switch (task.type){
                case MarketTask.SEND:
                case MarketTask.SIGN:
                case MarketTask.SIGN_ALL:
                    synchronized (MAIL_QUEUE){
                        MAIL_QUEUE.put(task);
                        MAIL_QUEUE.notify();
                    }
                    break;
                default:
                    synchronized (TRADE_QUEUE){
                        TRADE_QUEUE.put(task);
                        TRADE_QUEUE.notify();
                    }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void affairBuy(Player player, int id, int amount) {
        // TODO: 2021/5/4 点券
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        String buyer = player.getName();
        String seller = sale.getOwner();
        if(sale.isAuction()){
            player.sendMessage(Language.TYPE_ERROR);
            return;
        }
        if(seller.equals(buyer)){
            player.sendMessage(Language.BUY_ERROR);
            if (!Config.DEBUG) return;
        }
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        if(!"null".equals(sale.getNbt()) && !"".equals(sale.getNbt())) ItemUtils.setItemValue(item, sale.getNbt());

        String itemName = sale.getNickname();
        boolean buyAll = amount == 0, point = sale.isPoint();
        double price, cost;
        if (buyAll){
            price = sale.getPrice();
            cost = sale.getCost();
        }else {
            price = sale.getPrice() / item.getAmount() * amount;
            cost = sale.getCost() / item.getAmount() * amount;
        }

        // 钱不够
        if (!economy.has(player, cost)){
            player.sendMessage(String.format(Language.NOT_ENOUGH_MONEY, "你"));
            return;
        }

        // 更新统计
        User user = MarketManager.getUser(seller);

        // 判断是否无限
        if(sale.isAdmin()){
            if (!buyAll) {
                item.setAmount(amount);
            }
            this.obtainMailTask(buyer, item).sendToTarget();
        }else {
            if (buyAll){
                // 发送给购买者
                this.obtainMailTask(buyer, item).sendToTarget();

                MarketManager.removeSale(sale);

                // 统计数据修改
                user.setSelling(user.getSelling() - 1);
                user.setAmount(user.getAmount() + 1);
            }else {
                ItemStack save = item.clone();
                int saveAmount = item.getAmount() - amount;
                if(saveAmount < 0){
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }

                //判断剩余数量
                if(saveAmount == 0){
                    MarketManager.removeSale(sale);

                    // 统计数据修改
                    user.setSelling(user.getSelling() - 1);
                    user.setAmount(user.getAmount() + 1);
                }else {
                    // 更新数量和价格
                    save.setAmount(saveAmount);
                    sale.setStack(SerializeUtil.serializeItemStack(save));
                    sale.setPrice(sale.getPrice() - price);
                    sale.setCost(sale.getCost() - cost);

                    MarketManager.updateSale(sale);

                }
                // 邮寄给买家
                item.setAmount(amount);
                this.obtainMailTask(buyer, item).sendToTarget();

            }
        }

        // 买家扣钱并通知
        economy.withdrawPlayer(player, cost);
        player.sendMessage(Language.BUY_ITEM);

        // 发送给卖家
        ItemStack record = ItemUtils.getRecordItem(itemName, buyer, cost, point);
        this.obtainMailTask(seller, record, cost, point).sendToTarget();

        // 更新卖家统计数据
        user.setMoney(user.getMoney() + cost);
        data.update(user);

        guide.refreshPage(player.getName());

    }

    @Override
    public void affairCancel(Player player, int id) {
        Sale sale = MarketManager.getSale(id);

        // 判断商品是否存在
        if(null == sale){
            if (player != null) player.sendMessage(Language.DATA_NULL);
            return;
        }
        // 判断操作者是否商品主人
        if(player != null && !sale.getOwner().equals(player.getName())){
            player.sendMessage(Language.CANCEL_ERROR);
            return;
        }

        // 解析物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        if(!"null".equals(sale.getNbt()) && !"".equals(sale.getNbt())) ItemUtils.setItemValue(item, sale.getNbt());

        // 减少正在出售的数量
        User user = MarketManager.getUser(sale.getOwner());
        user.setSelling(user.getSelling() - 1);
        data.update(user);

        // 删除商品
        MarketManager.removeSale(sale);
        if (player != null) player.sendMessage(Language.CANCEL_ITEM);

        // 发送邮件
        this.obtainMailTask(sale.getOwner() , item).sendToTarget();

        if (player != null) guide.refreshPage(player.getName());
    }

    @Override
    public void affairCancel(int id) {
        this.affairCancel(null, id);
    }

    @Override
    public void affairAuction(Player player, int id, int add) {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        if(!sale.isAuction()){
            player.sendMessage(Language.TYPE_ERROR);
            return;
        }
        if(sale.getOwner().equals(player.getName())){
            player.sendMessage(Language.BUY_ERROR);
            if (!Config.DEBUG) return;
        }
        // 拍卖加价
        double cost = sale.getCost()+add;
        if(economy.has(player, cost)){
            sale.setCost(cost);
            sale.setHeat(sale.getHeat()+1);
            sale.setBuyer(player.getName());
        }else {
            player.sendMessage(String.format(Language.NOT_ENOUGH_MONEY, "你"));
        }

        player.sendMessage(Language.AUCTION_ITEM);

        MarketManager.updateSale(sale);

        guide.refreshPage(player.getName());
    }

    @Override
    public void affairSell(String seller, boolean auction, boolean point, double price, ItemStack item) {
        // 获取物品名称
        String itemName;
        if(item.hasItemMeta() && item.getItemMeta() != null && !"".equals(item.getItemMeta().getDisplayName())){
            itemName = item.getItemMeta().getDisplayName();
        }else {
            itemName = TranslateUtils.translate(item.getType());
        }

        String stack = SerializeUtil.serializeItemStack(item);
        String meta = SerializeUtil.serializeItemMeta(item);
        Sale sale = new Sale(0, stack, meta, ItemUtils.getItemValue(item), new Date().getTime(), seller, "", price, price, 0, itemName, auction, point);
        
        MarketManager.addSale(sale, item.getType());

        // 增加正在出售的数量
        User user = MarketManager.getUser(seller);
        user.setSelling(user.getSelling() + 1);
        data.update(user);

        // 广播
        if(!Config.SELL_BROADCAST) return;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            ChatUtils.sendItemButton(onlinePlayer, auction, item, String.format("/market affair %s", sale.getId()), seller);
        }
    }

    @Override
    public void affairSign(Player player, int id) {
        Mail mail = MarketManager.getMail(id);

        if (mail == null) {
            player.sendMessage(Language.DATA_NULL);
            return;
        }

        // 判断背包是否满
        if(player.getInventory().firstEmpty() == -1){
            player.sendMessage(Language.SIGN_ERROR);
            return;
        }

        mail.setSigned(true);
        ItemStack item = SerializeUtil.deserialize(mail.getStack(), mail.getMeta());
        if(!"null".equals(mail.getNbt()) && !"".equals(mail.getNbt())) ItemUtils.setItemValue(item, mail.getNbt());
        player.getInventory().addItem(item);
        if (mail.isRecord()){
            economy.depositPlayer(player, mail.getPrice());
            player.sendMessage(String.format(Language.AFFAIR_FINISH, mail.getPrice()));
        }else {
            player.sendMessage(Language.SIGN_FINISH);
        }
        
        MarketManager.removeMail(mail);

        guide.refreshPage(player.getName());

    }

    /**
     * 用于发送交易记录
     * @param to 目标
     * @param item 交易记录
     * @param price 价格
     * @param point 是否点券
     */
    private void affairSend(String to, ItemStack item, double price, boolean point) {
        String stack = SerializeUtil.serializeItemStack(item);
        String meta = SerializeUtil.serializeItemMeta(item);
        Mail mail = new Mail(0, stack, meta, ItemUtils.getItemValue(item), new Date().getTime(), to, "", false, price > 0, price, point);
        
        MarketManager.addMail(mail);

        // 通知收到邮箱
        Player player = Bukkit.getPlayer(to);
        if(player != null && to.equals(player.getName())){
            player.sendMessage(Language.HAS_MAIL);
            ChatUtils.sendCommandButton(player, Language.MAIL_BUTTON, Language.MAIL_HOVER, "/market mail");
        }
    }

    @Override
    public void affairSend(String to, ItemStack item) {
        this.affairSend(to, item, 0, false);
    }

    @Override
    public void affairFinish(Player player, int id) {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        String seller = sale.getOwner();
        String buyer = sale.getBuyer();
        String itemName = sale.getNickname();
        double cost = sale.getCost();
        boolean point = sale.isPoint();
        if(!seller.equalsIgnoreCase(player.getName())){
            player.sendMessage(Language.BUY_ERROR);
            return;
        }
        if(!sale.isAuction() || "".equalsIgnoreCase(buyer)){
            // 如果不是拍卖的物品 或者无人拍
            this.obtainTask(MarketTask.CANCEL, player, id).sendToTarget();
            return;
        }
        User buyUser = MarketManager.getUser(buyer);
        OfflinePlayer buyerPlayer = Bukkit.getOfflinePlayer(UUID.fromString(buyUser.getUuid()));

        if (!economy.has(buyerPlayer, cost)){
            player.sendMessage(String.format(Language.NOT_ENOUGH_MONEY, "对方"));
            buyUser.setCredit(buyUser.getCredit() - 1);
            data.update(buyUser);
            this.obtainTask(MarketTask.CANCEL, player, id).sendToTarget();
            return;
        }
        if (buyerPlayer.isOnline()){
            ((Player) buyerPlayer).sendMessage(Language.BUY_ITEM);
        }
        // 买家扣钱
        economy.withdrawPlayer(buyerPlayer, sale.getCost());
        // 发送物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        if(!"null".equals(sale.getNbt()) && !"".equals(sale.getNbt())) ItemUtils.setItemValue(item, sale.getNbt());
        this.obtainMailTask(buyer, item).sendToTarget();

        // 更新市场
        MarketManager.removeSale(sale);

        // 发送给卖家
        ItemStack record = ItemUtils.getRecordItem(itemName, buyer, cost, point);
        this.obtainMailTask(seller, record, cost, point).sendToTarget();

        // 更新卖家统计数据
        User user = MarketManager.getUser(seller);
        user.setSelling(user.getSelling() - 1);
        user.setAmount(user.getAmount() + 1);
        user.setMoney(user.getMoney() + cost);
        data.update(user);

        guide.refreshPage(player.getName());

    }

    @Override
    public void affairDiscount(Player player, int id, int discount) {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        if(!sale.getOwner().equalsIgnoreCase(player.getName())){
            player.sendMessage(Language.BUY_ERROR);
            return;
        }
        // 判断是否拍卖物
        if(sale.isAuction()){
            player.sendMessage(Language.TYPE_ERROR);
            return;
        }
        // 判断打折数值
        if(discount >= 10 || discount < 0){
            player.sendMessage(Language.COMMAND_ERROR);
            return;
        }

        sale.setCost(sale.getPrice()*discount*0.1);

        MarketManager.updateSale(sale);

    }

    @Override
    public void affairReprice(Player player, int id, double price) {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        if(!sale.getOwner().equalsIgnoreCase(player.getName())){
            player.sendMessage(Language.BUY_ERROR);
            return;
        }

        if(price < 0 || price > Config.MAX_PRICE){
            player.sendMessage(Language.COMMAND_ERROR);
            return;
        }

        sale.setCost(price);

        MarketManager.updateSale(sale);

    }

    @Override
    public void affairSignAll(Player player) {
        List<Integer> ids = new ArrayList<>();
        data.query(Mail.class, "owner", player.getName()).forEach(mail -> {
            if (mail.getOwner().equalsIgnoreCase(player.getName())) ids.add(mail.getId());
        });
        for (Integer id : ids) {
            this.affairSign(player, id);
        }
    }


}
