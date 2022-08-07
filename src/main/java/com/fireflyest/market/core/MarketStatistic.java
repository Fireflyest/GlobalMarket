package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Circulation;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.TimeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.item.ViewItemBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MarketStatistic {

    private MarketStatistic(){
    }

    /**
     * 当天市场交易统计/market statistic
     * @param player 指令使用者
     */
    public static void statisticMarket(Player player){
        player.closeInventory();

        new BukkitRunnable(){
            @Override
            public void run() {

                // 书
                ItemStack book = new ViewItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial()).build();
                BookMeta bookMeta = ((BookMeta) book.getItemMeta());

                Date today = new Date();
                for (int i = 0; i < 7; i++) {
                    String dayStr = TimeUtils.getTimeDay(DateUtils.addDays(today, -i));
                    Circulation circulation = MarketManager.getCirculation(dayStr);

                    // 页面
                    ComponentBuilder componentBuilder = new ComponentBuilder(Language.PLUGIN_NAME)
                            .append("\n")
                            .append("------------------");
                    componentBuilder
                            .append(String.format(MarketButton.CIRCULATE_PRICE + Language.COIN_SYMBOL, circulation.getCoin())).append("\n")
                            .append(String.format(MarketButton.CIRCULATE_PRICE + Language.POINT_SYMBOL, circulation.getPoint())).append("\n")
                            .append(String.format(MarketButton.CIRCULATE_AMOUNT, circulation.getAmount())).append("\n")
                            .append(String.format(MarketButton.MAX_PRICE, circulation.getMax())).append("\n")
                            .append("\n")
                            .append(dayStr);
                    // 添加
                    if (bookMeta != null) {
                        bookMeta.spigot().addPage(componentBuilder.create());
                    }
                }


//                // 三天的记录
//                Date now = new Date();
//                Date dateYesterday = DateUtils.addDays(now, -1);
//                Date dateBeforeYesterday = DateUtils.addDays(now, -2);
//                Circulation circulation = MarketManager.getCirculation(TimeUtils.getTimeDay(now));
//                Circulation circulationYesterday = MarketManager.getCirculation(TimeUtils.getTimeDay(dateYesterday));
//                Circulation circulationBeforeYesterday = MarketManager.getCirculation(TimeUtils.getTimeDay(dateBeforeYesterday));
//
//                // 第一页
//                ComponentBuilder componentBuilderToday = new ComponentBuilder(Language.PLUGIN_NAME)
//                        .append("\n")
//                        .append("------------------");
//                componentBuilderToday
//                        .append(String.format(MarketButton.CIRCULATE_PRICE + Language.COIN_SYMBOL, circulation.getCoin())).append("\n")
//                        .append(String.format(MarketButton.CIRCULATE_PRICE + Language.POINT_SYMBOL, circulation.getPoint())).append("\n")
//                        .append(String.format(MarketButton.CIRCULATE_AMOUNT, circulation.getAmount())).append("\n")
//                        .append(String.format(MarketButton.MAX_PRICE, circulation.getMax())).append("\n")
//                        .append("\n")
//                        .append(TimeUtils.getTimeDay(now));
//                // 第二页
//                ComponentBuilder componentBuilderYesterday = new ComponentBuilder(Language.PLUGIN_NAME)
//                        .append("\n")
//                        .append("------------------");
//                componentBuilderYesterday
//                        .append(String.format(MarketButton.CIRCULATE_PRICE + Language.COIN_SYMBOL, circulationYesterday.getCoin())).append("\n")
//                        .append(String.format(MarketButton.CIRCULATE_PRICE + Language.POINT_SYMBOL, circulationYesterday.getPoint())).append("\n")
//                        .append(String.format(MarketButton.CIRCULATE_AMOUNT, circulationYesterday.getAmount())).append("\n")
//                        .append(String.format(MarketButton.MAX_PRICE, circulationYesterday.getMax())).append("\n")
//                        .append("\n")
//                        .append(TimeUtils.getTimeDay(dateYesterday));
//                // 第三页
//                ComponentBuilder componentBuilderBeforeYesterday = new ComponentBuilder(Language.PLUGIN_NAME)
//                        .append("\n")
//                        .append("------------------");
//                componentBuilderBeforeYesterday
//                        .append(String.format(MarketButton.CIRCULATE_PRICE + Language.COIN_SYMBOL, circulationBeforeYesterday.getCoin())).append("\n")
//                        .append(String.format(MarketButton.CIRCULATE_PRICE + Language.POINT_SYMBOL, circulationBeforeYesterday.getPoint())).append("\n")
//                        .append(String.format(MarketButton.CIRCULATE_AMOUNT, circulationBeforeYesterday.getAmount())).append("\n")
//                        .append(String.format(MarketButton.MAX_PRICE, circulationBeforeYesterday.getMax())).append("\n")
//                        .append("\n")
//                        .append(TimeUtils.getTimeDay(dateBeforeYesterday));

                if (bookMeta != null) {
                    bookMeta.setAuthor(player.getName());
                    bookMeta.setTitle(Language.PLUGIN_NAME.replace("§f", "§0"));
                }
                book.setItemMeta(bookMeta);

                player.openBook(book);
            }
        }.runTaskAsynchronously(GlobalMarket.getPlugin());
    }

    /**
     * 某个商品的信息/market data id
     * @param player 指令使用者
     * @param id 商品id
     */
    public static void dataSale(Player player, int id){
        player.closeInventory();

        new BukkitRunnable(){
            @Override
            public void run() {

                Sale sale = MarketManager.getSale(id);
                if(sale == null){
                    player.sendMessage(Language.DATA_NULL);
                    return;
                }

                ItemStack book = new ViewItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial()).build();
                BookMeta bookMeta = ((BookMeta) book.getItemMeta());
                ComponentBuilder componentBuilder = new ComponentBuilder(Language.PLUGIN_NAME)
                        .append("\n")
                        .append("------------------");

                componentBuilder.append(String.format(MarketButton.SALE_HEAT, sale.getHeat())).append("\n");

                if (bookMeta != null) {
                    bookMeta.setAuthor(player.getName());
                    bookMeta.setTitle(Language.PLUGIN_NAME.replace("§f", "§0"));
                    bookMeta.spigot().addPage(componentBuilder.create());
                }
                book.setItemMeta(bookMeta);

                player.openBook(book);
            }
        }.runTaskAsynchronously(GlobalMarket.getPlugin());
    }

    /**
     * 玩家信息统计/market statistic [name]
     * @param player 指令使用者
     * @param name 目标玩家
     */
    public static void statisticPlayer(Player player, String name){
        player.closeInventory();


        new BukkitRunnable(){
            @Override
            public void run() {

                User user = MarketManager.getUser(name);
                if(user == null || "".equals(user.getUuid())){
                    player.sendMessage(Language.USER_ERROR);
                    return;
                }

                ItemStack book = new ViewItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial()).build();
                BookMeta bookMeta = ((BookMeta) book.getItemMeta());
                ComponentBuilder componentBuilder = new ComponentBuilder(Language.PLUGIN_NAME)
                        .append("\n")
                        .append("------------------");

                componentBuilder.append(String.format(MarketButton.PLAYER_CREDIT, user.getCredit())).append("\n")
                        .append(String.format(MarketButton.PLAYER_BLACK, user.isBlack())).append("\n")
                        .append(String.format(MarketButton.PLAYER_SELLING, user.getSelling())).append("\n")
                        .append(String.format(MarketButton.PLAYER_STATIC_AMOUNT, user.getAmount())).append("\n")
                        .append(String.format(MarketButton.PLAYER_STATIC_MONEY, user.getMoney())).append("\n")
                        .append("\n")
                        .append(user.getName());

                if (bookMeta != null) {
                    bookMeta.setAuthor(player.getName());
                    bookMeta.setTitle(Language.PLUGIN_NAME.replace("§f", "§0"));
                    bookMeta.spigot().addPage(componentBuilder.create());
                }
                book.setItemMeta(bookMeta);

                player.openBook(book);
            }
        }.runTaskAsynchronously(GlobalMarket.getPlugin());
    }

    /**
     * 个人市场数据/market data
     * @param player 玩家
     */
    public static void dataPlayer(Player player){
        player.closeInventory();

        new BukkitRunnable(){
            @Override
            public void run() {

                List<Sale> sales = MarketManager.getPlayerSales(player.getName());

                if(sales == null || sales.size() == 0){
                    player.sendMessage(Language.DATA_NULL);
                    return;
                }
                List<Integer> list = new ArrayList<>();
                int amount = 0;
                double cost = 0;
                for(Sale sale : sales){
                    cost+=sale.getCost();
                    amount++;
                    list.add(sale.getId());
                }

                ItemStack book = new ViewItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial()).build();
                BookMeta bookMeta = ((BookMeta) book.getItemMeta());
                ComponentBuilder componentBuilder = new ComponentBuilder(Language.PLUGIN_NAME)
                        .append("\n")
                        .append("------------------");

                componentBuilder.append(String.format(MarketButton.TOTAL_SALE_PRICE, cost)).append("\n")
                        .append(String.format(MarketButton.TOTAL_SALE_AMOUNT, amount)).append("\n")
                        .append(MarketButton.TOTAL_SALE_LIST).append("\n");
                int i = 0;
                for (Integer integer : list) {
                    i++;
                    componentBuilder
                            .append(i > 1 ? ", ": "")
                            .reset()
                            .color( i%2 == 0 ? ChatColor.BLACK : ChatColor.DARK_GRAY)
                            .append(String.valueOf(integer))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/market edit %s", integer)));
                }
                if (bookMeta != null) {
                    bookMeta.setAuthor(player.getName());
                    bookMeta.setTitle(Language.PLUGIN_NAME.replace("§f", "§0"));
                    bookMeta.spigot().addPage(componentBuilder.create());
                }
                book.setItemMeta(bookMeta);

                player.openBook(book);
            }
        }.runTaskAsynchronously(GlobalMarket.getPlugin());
    }

}
