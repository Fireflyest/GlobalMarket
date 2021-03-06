package com.fireflyest.market.command;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.gui.api.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketAffair;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketStatistic;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.YamlUtils;
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

    private final MarketAffair marketAffair;

    private final Data data;
    private final ViewGuide guide;
    private final Economy economy;

    public MarketCommand() {
        this.data = GlobalMarket.getData();
        this.guide = GlobalMarket.getGuide();
        economy = GlobalMarket.getEconomy();
        this.marketAffair = MarketAffair.getInstance();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, String label, String[] args) {
        if(!label.equalsIgnoreCase("market")) return true;

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
     * ????????????
     * @param sender ?????????
     * @param var1 ????????????
     */
    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;
        switch (var1){
            case "help":
                sender.sendMessage(
                        "??e??m =               ??f??l[??eGlobeMarket??f??l]??e??m               =\n"+
                        "??b/market                            ??f - ????????????\n"+
                        "??b/market help                      ??f - ????????????\n"+
                        "??b/market mine                      ??f - ????????????\n"+
                        "??b/market mail                       ??f - ????????????\n"+
                        "??b/market clean                     ??f - ????????????\n"+
                        "??b/market sell [m] <a>              ??f - ????????????\n"+
                        "??b/market auction [m] <a>          ??f - ????????????\n"+
                        "??b/market discount [id] [a]        ??f - ????????????\n"+
                        "??b/market reprice [id] [a]          ??f - ????????????\n"+
                        "??b/market send [p] <a>             ??f - ????????????\n"+
                        "??b/market statistic <p|id>          ??f - ????????????\n"+
                        "??b/market other [p]                 ??f - ????????????\n"+
                        "??b/market classify <type>         ??f - ????????????\n"+
                        "??9????????7p??9??????????????7a??9??????????????7m??9??????????????7<>??9?????????????????7[]??9????????????"
                        );
                break;
            case "reload":
                if(!sender.isOp())return;
                sender.sendMessage(Language.RELOADING);
                YamlUtils.loadConfig();
                sender.sendMessage(Language.RELOADED);
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
            case "all":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                guide.openView(player, GlobalMarket.MAIN_VIEW, MainView.ALL);
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
                MarketStatistic.statisticData(player);
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
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                MarketStatistic.statisticMarket(player);
                break;
            case "classify":{
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!Config.ITEM_CLASSIFY){
                    player.sendMessage(Language.COMMAND_ERROR);
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
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                player.closeInventory();
                player.sendMessage(Language.SEARCH_ITEM);
                break;
            case "sign":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(!player.hasPermission("market.sign")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                marketAffair.affairSignAll(player);
                break;
            case "quick":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }
                guide.openView(player, GlobalMarket.SELL_VIEW, player.getName());
                break;
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

    /**
     * ???????????????
     * @param sender ?????????
     * @param var1 ????????????
     * @param var2 ??????
     */
    private void executeCommand(CommandSender sender, String var1, String var2){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1){
            case "point":
                // TODO: 2021/5/5 point ??????break??????
                sender.sendMessage("???????????????");
                break;
            case "sell":
            case "auction":
            case "send":
                String var3 = String.valueOf(player.getInventory().getItemInMainHand().getAmount());
                this.executeCommand(sender, var1, var2, var3);
                break;
            case "admin":
                if(!player.isOp()){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }else {
                    Sale sale = data.queryOne(Sale.class, "id", var2);
                    if (sale == null) {
                        sender.sendMessage(Language.DATA_NULL);
                        return;
                    }
                    sale.setAdmin(!sale.isAdmin());
                    data.update(sale);
                    player.sendMessage(Language.UNLIMITED_ITEM);
                }
                break;
            case "classify":{
                if(!Config.ITEM_CLASSIFY){
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                guide.openView(player, GlobalMarket.CLASSIFY_VIEW, var2);
                break;
            }
            case "affair":{
                guide.openView(player, GlobalMarket.AFFAIR_VIEW, var2);
                break;
            }
            case "buy":
                marketAffair.affairBuy(player, ConvertUtils.parseInt(var2), 0);
                break;
            case "sign":
                marketAffair.affairSign(player, ConvertUtils.parseInt(var2));
                break;
            case "cancel":
                marketAffair.affairCancel(player, ConvertUtils.parseInt(var2));
                player.closeInventory();
                break;
            case "finish":
                marketAffair.affairFinish(player, ConvertUtils.parseInt(var2));
                break;
            case "search":
                if(!player.hasPermission("market.search")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                guide.openView(player, GlobalMarket.SEARCH_VIEW, var2);
                break;
            case "statistic":
                if(!player.hasPermission("market.statistic")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                MarketStatistic.statisticPlayer(player, var2);
                break;
            case "data":
                int target = ConvertUtils.parseInt(var2);
                MarketStatistic.statisticSale(player, target);
            case "other":{
                if(!player.hasPermission("market.other")){
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                guide.openView(player, GlobalMarket.MINE_VIEW, var2);
                break;
            }
            default:
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

    /**
     * ???????????????
     * @param sender ?????????
     * @param var1 ????????????
     * @param var2 ???????????????
     * @param var3 ???????????????
     */
    private void executeCommand(CommandSender sender, String var1, String var2, String var3){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }
        switch (var1) {
            case "send": {
                // ?????????????????????
                if (var2.equals(player.getName())) {
                    player.sendMessage(Language.SEND_ERROR);
                    return;
                }
                // ????????????????????????
                if (MarketManager.getUser(var2) == null) {
                    player.sendMessage(Language.USER_ERROR);
                    return;
                }
                // ??????????????????
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
                    marketAffair.affairSend(var2, saleItem);
                }
                break;
            }
            case "add" : {
                // ????????????
                int id = ConvertUtils.parseInt(var2);
                int add = ConvertUtils.parseInt(var3);
                if (add <= 0) {
                    player.sendMessage(Language.COMMAND_ERROR);
                    return;
                }
                marketAffair.affairAuction(player, id, add);
                break;
            }
            case "discount" : {
                if (!player.hasPermission("market.discount")) {
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                marketAffair.affairDiscount(player, ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3));
                player.sendMessage(String.format(Language.DISCOUNT_ITEM, var3));
                break;
            }
            case "point" :
                // TODO: 2021/5/5 point ??????break??????
                    sender.sendMessage("???????????????");
            case "sell" :
            case "auction":
            {
                double price = ConvertUtils.parseDouble(var2);
                int amount = ConvertUtils.parseInt(var3);
                if (price <= 0 || price > Config.MAX_PRICE) {
                    player.sendMessage(Language.COMMAND_ERROR + " ??3the price??f you entered is invalid!");
                    return;
                }
                if (amount <= 0 || amount > 64) {
                    player.sendMessage(Language.COMMAND_ERROR + " ??3the amount??f you entered is invalid!");
                    return;
                }
                ItemStack item = player.getInventory().getItemInMainHand();
                // ?????????????????? op?????????
                if (Config.LIMIT_AMOUNT && !player.isOp()) {
                    User user = MarketManager.getUser(player.getName());
                    int limit = player.hasPermission("market.vip") ? Config.LIMIT_AMOUNT_NUM_VIP : Config.LIMIT_AMOUNT_NUM;
                    if (user.getSelling() > limit) {
                        player.sendMessage(Language.NOT_ENOUGH_SPACE);
                        return;
                    }
                }
                //??????????????????
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
                // ??????????????????
                if (item.getType().equals(XMaterial.WRITTEN_BOOK.parseMaterial())) {
                    String value = ItemUtils.getItemValue(item);
                    if (value.equals("record")) {
                        player.sendMessage(Language.TYPE_ERROR);
                        return;
                    }
                }

                // ????????????????????????
                int has = item.getAmount();
                if (amount > has) {
                    player.sendMessage(Language.NOT_ENOUGH_ITEM);
                    return;
                }

                // ??????
                if (Config.TAX) {
                    if (price > Config.TAX_THRESHOLD) {
                        double tax = Config.TAX_RATE * price;
                        if (economy.has(player, tax)) {
                            // ??????
                            economy.withdrawPlayer(player, tax);
                            player.sendMessage(String.format(Language.TAX, Config.TAX_RATE, tax));
                        } else {
                            player.sendMessage(Language.TAX_BURDEN);
                            return;
                        }
                    }
                }
                ItemStack saleItem = item.clone();
                saleItem.setAmount(amount);
                if (var1.equals("sell") && sender.hasPermission("market.sell")) {
                    marketAffair.affairSell(player.getName(), false, false, price, saleItem);
                    item.setAmount(has - amount);
                    player.sendMessage(Language.SELL_ITEM);
                } else if (var1.equals("auction") && sender.hasPermission("market.auction")) {
                    marketAffair.affairSell(player.getName(), true, false, price, saleItem);
                    item.setAmount(has - amount);
                    player.sendMessage(Language.SELL_ITEM);
                } else if (var1.equals("point") && sender.hasPermission("market.point")) {
                    marketAffair.affairSell(player.getName(), false, true, price, saleItem);
                    item.setAmount(has - amount);
                    player.sendMessage(Language.SELL_ITEM);
                } else {
                    player.sendMessage(Language.NOT_PERMISSION);
                }
                player.closeInventory();
                break;
            }
            case "reprice" : {
                if (!player.hasPermission("market.reprice")) {
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                marketAffair.affairReprice(player, ConvertUtils.parseInt(var2), ConvertUtils.parseDouble(var3));
                player.sendMessage(String.format(Language.REPRICE_ITEM, var3));
                break;
            }
            case "desc" : {
                if (!player.hasPermission("market.desc")) {
                    player.sendMessage(Language.NOT_PERMISSION);
                    return;
                }
                Sale sale = data.queryOne(Sale.class, "id", var2);
                if (sale == null) {
                    sender.sendMessage(Language.DATA_NULL);
                    return;
                }
                sale.setDesc(var3);
                data.update(sale);
                player.sendMessage(String.format(Language.DESC_ITEM, var2));
                break;
            }
            case "buy" :
                // ??????????????????
                if(! Config.BUY_PARTIAL && !"0".equals(var3)){
                    player.sendMessage(Language.COMMAND_ERROR);
                    break;
                }
                marketAffair.affairBuy(player, ConvertUtils.parseInt(var2), ConvertUtils.parseInt(var3));
                break;
            default :
                sender.sendMessage(Language.COMMAND_ERROR);
        }
    }

}
