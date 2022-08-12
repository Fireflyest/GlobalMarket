package com.fireflyest.market.command;

import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.task.*;
import org.fireflyest.craftgui.api.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketStatistic;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.view.HomeView;
import com.fireflyest.market.view.MainView;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MarketCommand implements CommandExecutor {

    private final MarketTasks.TaskManager taskManager;

    private final Data data;
    private final ViewGuide guide;
    private final Economy economy;

    public MarketCommand() {
        this.data = GlobalMarket.getData();
        this.guide = GlobalMarket.getGuide();
        this.economy = GlobalMarket.getEconomy();

        this.taskManager = MarketTasks.getTaskManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if(!label.equalsIgnoreCase("market") && !label.equalsIgnoreCase("mk")) return true;

        switch (args.length) {
            case 1:
                this.executeCommand(sender, args[0]);
                break;
            case 2:
                this.executeCommand(sender, args[0], args[1]);
                break;
            case 3:
                this.executeCommand(sender, args[0], args[1], args[2]);
                break;
            default:
                this.executeCommand(sender);
        }
        return true;
    }


    private void executeCommand(CommandSender sender){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player != null) {
            guide.openView(player, GlobalMarket.MAIN_VIEW, MainView.NORMAL);
        }else {
            sender.sendMessage(Language.PLAYER_COMMAND);
        }
    }

    /**
     * 无参指令
     * @param sender 发送者
     * @param var1 指令类型
     */
    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;
        switch (var1){
            case "help":
                sender.sendMessage(Language.HELP.replace("\\n", "\n"));
                break;
            case "mine":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                String playerName = player.getName();
                guide.openView(player, GlobalMarket.MINE_VIEW, playerName);
                break;
            }
            case "close":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                player.closeInventory();
                break;
            case "data":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                MarketStatistic.dataPlayer(player);
                break;
            case "mail":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                String playerName = player.getName();
                guide.openView(player, GlobalMarket.MAIL_VIEW, playerName);
                break;
            }
            case "point":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                guide.openView(player, GlobalMarket.MAIN_VIEW, MainView.POINT);
                break;
            }
            case "retail":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                guide.openView(player, GlobalMarket.MAIN_VIEW, MainView.RETAIL);
                break;
            }
            case "auction":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                guide.openView(player, GlobalMarket.MAIN_VIEW, MainView.AUCTION);
                break;
            }
            case "admin":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                guide.openView(player, GlobalMarket.MAIN_VIEW, MainView.ADMIN);
                break;
            }
            case "statistic":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.statistic")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.statistic"));
                    return;
                }
                MarketStatistic.statisticMarket(player);
                break;
            case "classify":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                guide.openView(player, GlobalMarket.HOME_VIEW, HomeView.NORMAL);
                break;
            }
            case "search":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.search")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.search"));
                    return;
                }
                player.closeInventory();
                player.sendMessage(Language.SEARCH_ITEM);
                break;
            case "delete":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                player.closeInventory();
                player.sendMessage(Language.DELETE_ITEM);
                break;
            case "send":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.send")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.send"));
                    return;
                }
                player.closeInventory();
                player.sendMessage(Language.SEND_ITEM);
                break;
            case "sell":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                player.sendMessage(Language.SELL_ITEM_PREPARE);
                guide.refreshPage(player.getName());
                break;
            case "sign":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.sign")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.sign"));
                    return;
                }
                taskManager.putTask(new TaskSignAll(player.getName()));
                break;
            case "quick":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.quick")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.quick"));
                    return;
                }
                if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }
                taskManager.putTask(new TaskSell(player.getName(), false, false, -1, player.getInventory().getItemInMainHand().clone()));
                player.getInventory().getItemInMainHand().setAmount(0);
                break;
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

    /**
     * 单参数指令
     * @param sender 发送者
     * @param var1 指令类型
     * @param var2 参数
     */
    private void executeCommand(CommandSender sender, String var1, String var2){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1){
            case "point":
                if (!Config.POINT){
                    sender.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
            case "sell":
            case "auction":
            case "send":
                String var3 = String.valueOf(player.getInventory().getItemInMainHand().getAmount());
                this.executeCommand(sender, var1, var2, var3);
                break;
            case "admin":
                if(!player.hasPermission("market.admin")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
                    return;
                }
                taskManager.putTask(new TaskAdmin(player.getName(), ConvertUtils.parseInt(var2)));
                break;
            case "classify":{
                guide.openView(player, GlobalMarket.CLASSIFY_VIEW, var2);
                break;
            }
            case "affair":{
                guide.openView(player, GlobalMarket.AFFAIR_VIEW, var2);
                break;
            }
            case "edit":{
                guide.openView(player, GlobalMarket.SALE_VIEW, var2);
                break;
            }
            case "buy":
                taskManager.putTask(new TaskBuy(player.getName(), ConvertUtils.parseInt(var2), 0));
                break;
            case "sign":
                taskManager.putTask(new TaskSign(player.getName(), ConvertUtils.parseInt(var2)));
                break;
            case "cancel":
                taskManager.putTask(new TaskCancel(player.getName(), ConvertUtils.parseInt(var2)));
                player.closeInventory();
                break;
            case "finish":
                taskManager.putTask(new TaskFinish(player.getName(), ConvertUtils.parseInt(var2)));
                break;
            case "search":
                if(!player.hasPermission("market.search")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.search"));
                    return;
                }
                guide.openView(player, GlobalMarket.SEARCH_VIEW, var2);
                break;
            case "statistic":
                MarketStatistic.statisticPlayer(player, var2);
                break;
            case "data":
                int target = ConvertUtils.parseInt(var2);
                MarketStatistic.dataSale(player, target);
                break;
            case "other":{
                if(!player.hasPermission("market.other")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.other"));
                    return;
                }
                guide.openView(player, GlobalMarket.OTHER_VIEW, var2);
                break;
            }
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

    /**
     * 双参数指令
     * @param sender 发送者
     * @param var1 指令类型
     * @param var2 第一个参数
     * @param var3 第二个参数
     */
    private void executeCommand(CommandSender sender, String var1, String var2, String var3){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1) {
            case "send": {
                if(!player.hasPermission("market.send")){
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.send"));
                    return;
                }
                // 禁止发送给自己
                if (var2.equals(player.getName())) {
                    player.sendMessage(Language.SEND_ERROR);
                    if (!Config.DEBUG) return;
                }
                // 判断玩家是否存在
                if (MarketManager.getUser(var2) == null) {
                    player.sendMessage(Language.USER_ERROR);
                    return;
                }
                // 邮箱数量限制
                if (Config.LIMIT_MAIL && MarketManager.getMailAmount(var2) > Config.LIMIT_MAIL_NUM) {
                    player.sendMessage(Language.FULL_MAIL);
                    return;
                }
                int sendAmount = ConvertUtils.parseInt(var3);
                if (sendAmount <= 0 || sendAmount > 64) {
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                ItemStack sendItem = player.getInventory().getItemInMainHand();
                int sendHas = sendItem.getAmount();
                if (sendAmount > sendHas) {
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                } else {
                    ItemStack saleItem = sendItem.clone();
                    saleItem.setAmount(sendAmount);
                    sendItem.setAmount(sendHas - sendAmount);
                    taskManager.putTask(new TaskSend(player.getName(), var2, saleItem));
                }
                break;
            }
            case "add" : {
                // 拍卖加价
                int id = ConvertUtils.parseInt(var2);
                int add = ConvertUtils.parseInt(var3);
                if (add <= 0) {
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                taskManager.putTask(new TaskBid(player.getName(), id, add));
                break;
            }
            case "discount" : {
                if (!player.hasPermission("market.discount")) {
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.discount"));
                    return;
                }
                taskManager.putTask(new TaskDiscount(player.getName(), ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3)));

                player.sendMessage( Language.DISCOUNT_ITEM.replace("%discount%", String.valueOf(var3)));
                break;
            }
            case "point" :
                if (!Config.POINT){
                    sender.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
            case "sell" :
            case "auction":
            {
                double price = ConvertUtils.parseDouble(var2);
                int amount = ConvertUtils.parseInt(var3);
                if (price <= 0 || price > Config.MAX_PRICE) {
                    player.sendMessage(Language.COMMAND_ERROR + " §3the price§f you entered is invalid!");
                    return;
                }
                if (amount <= 0 || amount > 64) {
                    player.sendMessage(Language.COMMAND_ERROR + " §3the amount§f you entered is invalid!");
                    return;
                }
                ItemStack item = player.getInventory().getItemInMainHand();
                // 判断最大数量 op无限制
                if (Config.LIMIT_AMOUNT && !player.hasPermission("market.admin")) {
                    User user = MarketManager.getUser(player.getName());
                    int limit = Config.LIMIT_AMOUNT_NUM;
                    if (player.hasPermission("market.vip")){
                        limit = Config.LIMIT_AMOUNT_NUM_VIP;
                    } else if (player.hasPermission("market.svip")) {
                        limit = Config.LIMIT_AMOUNT_NUM_VIP;
                    }
                    if (user.getSelling() >= limit) {
                        player.sendMessage(Language.NOT_ENOUGH_SPACE);
                        return;
                    }
                }
                //判断违规上架
                if (Config.LIMIT_LORE) {
                    ItemMeta meta = item.getItemMeta();
                    if (null != meta) {
                        List<String> lores = meta.getLore();
                        if (null != lores) {
                            for (String lore : Config.LIMIT_LORE_LIST.split(",")) {
                                if (lores.contains(lore)) {
                                    player.sendMessage(Language.TYPE_ERROR);
                                    return;
                                }
                            }
                        }
                    }
                }

                // 判断物品是否足够
                int has = item.getAmount();
                if (amount > has) {
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }

                // 赋税
                if (Config.TAX) {
                    if (price > Config.TAX_THRESHOLD) {
                        double tax = Config.TAX_RATE * price;
                        if (economy.has(player, tax)) {
                            // 扣钱
                            economy.withdrawPlayer(player, tax);
                            player.sendMessage(Language.TAX
                                    .replace("%rate%", String.valueOf(Config.TAX_RATE))
                                    .replace("%money%", String.valueOf(tax)));
                        } else {
                            player.sendMessage(Language.TAX_BURDEN);
                            return;
                        }
                    }
                }
                ItemStack saleItem = item.clone();
                saleItem.setAmount(amount);
                if (var1.equals("sell") && sender.hasPermission("market.sell")) {
                    taskManager.putTask(new TaskSell(player.getName(), false, false, price, saleItem));
                    item.setAmount(has - amount);
                    player.sendMessage(Language.SELL_ITEM);
                } else if (var1.equals("auction") && sender.hasPermission("market.auction")) {
                    taskManager.putTask(new TaskSell(player.getName(), true, false, price, saleItem));
                    item.setAmount(has - amount);
                    player.sendMessage(Language.SELL_ITEM);
                } else if (var1.equals("point") && sender.hasPermission("market.point")) {
                    taskManager.putTask(new TaskSell(player.getName(), false, true, Math.round(price), saleItem));
                    item.setAmount(has - amount);
                    player.sendMessage(Language.SELL_ITEM);
                } else {
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market."+ var1));
                }
                player.closeInventory();
                break;
            }
            case "reprice" : {
                if (!player.hasPermission("market.reprice")) {
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.reprice"));
                    return;
                }
                taskManager.putTask(new TaskReprice(player.getName(), ConvertUtils.parseInt(var2), ConvertUtils.parseDouble(var3)));
                player.sendMessage(Language.REPRICE_ITEM.replace("%price%", String.valueOf(var3)));
                break;
            }
            case "desc" : {
                if (!player.hasPermission("market.desc")) {
                    player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.desc"));
                    return;
                }
                Sale sale = data.queryOne(Sale.class, "id", var2);
                if (sale == null) {
                    sender.sendMessage(Language.DATA_NULL);
                    return;
                }
                if (sale.isAuction()){
                    sender.sendMessage(Language.TYPE_ERROR);
                    return;
                }
                sale.setDesc(var3);
                data.update(sale);
                player.sendMessage(Language.DESC_ITEM.replace("%id%", String.valueOf(var2)));
                break;
            }
            case "buy" :
                // 禁用零散购买
                if(! Config.BUY_PARTIAL && !"0".equals(var3)){
                    player.sendMessage(Language.COMMAND_ERROR);
                    break;
                }
                taskManager.putTask(new TaskBuy(player.getName(), ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3)));
                break;
            default :
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

}
