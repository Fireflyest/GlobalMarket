package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Note;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.TimeUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.item.ViewItemBuilder;

import java.util.ArrayList;
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

                Note note = MarketManager.getTodayNote();

                ItemStack book = new ViewItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial()).build();
                BookMeta bookMeta = ((BookMeta) book.getItemMeta());
                ComponentBuilder componentBuilder = new ComponentBuilder(Language.PLUGIN_NAME)
                        .append("\n")
                        .append("------------------");

                componentBuilder.append(String.format(MarketButton.CIRCULATE_PRICE, note.getMoney())).append("\n")
                        .append(String.format(MarketButton.CIRCULATE_AMOUNT, note.getAmount())).append("\n")
                        .append(String.format(MarketButton.MAX_PRICE, note.getMax())).append("\n")
                        .append("\n")
                        .append(TimeUtils.getTimeToday());
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
     * 某个商品的信息/market data id
     * @param player 指令使用者
     * @param id 商品id
     */
    public static void dataSale(Player player, int id){
        Sale sale = MarketManager.getSale(id);
        if(sale == null){
            player.sendMessage(Language.DATA_NULL);
            return;
        }
        player.sendMessage("§e§m =                                             = ");
        player.sendMessage(Language.TITLE+"§f商品信息");
        player.sendMessage("§3卖家§7: §f"+sale.getOwner());
        player.sendMessage("§3买家§7: §f"+sale.getBuyer());
        player.sendMessage("§3原价§7: §f"+sale.getPrice());
        player.sendMessage("§3现价§7: §f"+sale.getCost());
        player.sendMessage("§3拍卖§7: §f"+sale.isAuction());
        player.sendMessage("§3热度§7: §f"+sale.getHeat());
        player.sendMessage("§e§m =                                             = ");
    }

    /**
     * 玩家信息统计/market statistic [name]
     * @param player 指令使用者
     * @param name 目标玩家
     */
    public static void statisticPlayer(Player player, String name){
        User user = MarketManager.getUser(name);
        if(user == null || "".equals(user.getUuid())){
            player.sendMessage(Language.USER_ERROR);
            return;
        }
        player.sendMessage("§e§m =                                             = ");
        player.sendMessage(Language.TITLE+"§f玩家§3"+user.getName()+"§f注册于"+ TimeUtils.getTime(user.getRegister()));
        player.sendMessage("§3信誉度§7: §f"+user.getCredit());
        player.sendMessage("§3黑名单§7: §f"+user.isBlack());
        player.sendMessage("§3在出售§7: §f"+user.getSelling());
        player.sendMessage("§3累计交易数量§7: §f"+user.getAmount());
        player.sendMessage("§3累计交易金额§7: §f"+user.getMoney());
        player.sendMessage("§f输入§3: §3/market data §f查看目前商品信息");
        player.sendMessage("§e§m =                                             = ");
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
