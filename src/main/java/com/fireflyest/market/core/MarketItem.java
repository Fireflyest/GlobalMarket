package com.fireflyest.market.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import io.fireflyest.craftgui.button.ButtonAction;
import io.fireflyest.craftitem.builder.ItemBuilder;
import io.fireflyest.util.ItemUtils;
import io.fireflyest.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;

public class MarketItem {
    
    private static List<Long> heatList = new ArrayList<>();

    private MarketItem() {

    }

    /**
     * 附上交易物品信息
     * @param item
     * @param transaction
     * @param action
     */
    public static void loreItemData(ItemStack item, Transaction transaction, String action) {
        boolean admin = false;
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        String tag = getTag(transaction.getAppear(), transaction.getId());
        String symbol = getSymbol(transaction.getCurrency());
        String type = transaction.getType();
        switch (type) {
            case "retail":
            case "adminretail":
            case "order":
            case "adminorder":
                admin = type.startsWith("admin");
                tag += type.endsWith("r") ? Language.TAG_ORDER : Language.TAG_RETAIL;
                ItemUtils.addLore(item, tag + (admin ? Language.TAG_ADMIN : ""));
                if (!admin) {
                    ItemUtils.addLore(item, String.format(Language.GUI_SELLER, transaction.getOwnerName()));
                }
                if(transaction.getPrice() != transaction.getCost()){
                    ItemUtils.addLore(item, String.format(Language.GUI_ORIGINAL_PRICE, transaction.getPrice(), symbol));
                    ItemUtils.addLore(item, String.format(Language.GUI_PRESENT_PRICE, transaction.getCost(), symbol));
                }else {
                    ItemUtils.addLore(item, String.format(Language.GUI_PRICE, transaction.getPrice(), symbol));
                }
                break;
            case "auction":
                ItemUtils.addLore(item, tag + Language.TAG_AUCTION);
                ItemUtils.addLore(item, String.format(Language.GUI_SELLER, transaction.getOwnerName()));
                ItemUtils.addLore(item, String.format(Language.GUI_START_PRICE, transaction.getPrice(), symbol));
                if(transaction.getPrice() != transaction.getCost()){
                    ItemUtils.addLore(item, String.format(Language.GUI_PRESENT_PRICE, transaction.getCost(), symbol));
                    ItemUtils.addLore(item, String.format(Language.GUI_AUCTION_CONFIRM, 3 - transaction.getHeat()));
                }
                break;
            case "prepare":
            default:
                ItemUtils.addLore(item, Language.TAG_PREPARE);
                ItemUtils.addLore(item, String.format(Language.GUI_SELLER, transaction.getOwnerName()));
                break;
        }
        if (!"".equals(action)) {
            ItemUtils.setItemNbt(item, ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_SHIFT_COMMAND_SEND);
            ItemUtils.setItemNbt(item, ButtonAction.NBT_VALUE_KEY, "market "+ action + " " + transaction.getId() + ";market cancel " +  + transaction.getId());
        }
    }

    public static void loreItemData(ItemStack item, Transaction transaction) {
        loreItemData(item, transaction, "affair");
    }

    /**
     * 附上邮件信息
     * @param item 物品
     * @param delivery 邮件数据
     */
    public static void loreMailData(ItemStack item, Delivery delivery) {
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§7- " + delivery.getSender());
        if (!"".equals(delivery.getInfo())) {
            ItemUtils.addLore(item, "§f" + delivery.getInfo());
        }
        ItemUtils.addLore(item, "§e§m·                         ·");
        ItemUtils.addLore(item, "§f" + TimeUtils.getLocalDate(delivery.getAppear()));
        ItemUtils.setItemNbt(item, ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PLAYER_COMMAND_SEND);
        ItemUtils.setItemNbt(item, ButtonAction.NBT_VALUE_KEY, "market sign " + delivery.getId());
    }

    @NotNull
    public static ItemStack getRecordItem(String itemName, String buyer, double cost, String symbol) {
        return new ItemBuilder("WRITTEN_BOOK")
                .name(Language.GUI_MARKET_RECORD)
                .lore(String.format(Language.GUI_TRANSACTION_ITEM, itemName))
                .lore(String.format(Language.GUI_BUYER, buyer))
                .lore(String.format(Language.GUI_REWARD,  cost, symbol))
                .build();
    }

    /**
     * 获取货币符号
     * @param currency 货币
     * @return 符号
     */
    public static String getSymbol(String currency) {
        switch(currency) {
            case "coin":
                return Language.COIN_SYMBOL;
            case "point":
                return Language.POINT_SYMBOL;
            case "item":
            default:
                break;
        }
        return "";
    }

    /**
     * 时间标签
     * @param appear 出现时刻
     * @return 标签
     */
    public static String getTag(long appear, long id) {
        String tag = "";
        // 热度高
        if (heatList.contains(id)) {
            tag += Language.TAG_HEAT;
        }
        Instant appearInstant = TimeUtils.getInstant(appear);
        // 上架6小时内
        if (Instant.now().compareTo(appearInstant) < 1000 * 60 * 60 * 6) {
            tag += Language.TAG_NEW;
        }
        // 到期6小时内
        if (Config.TERM_OF_VALIDITY != -1) {
            long time = appear + Config.TERM_OF_VALIDITY * 1000 * 60 * 60 * 24 - 1000 * 60 * 60 * 6;
            if (Instant.now().isAfter(TimeUtils.getInstant(time))) {
                tag += Language.TAG_DEADLINE;
            }
        }
        return tag;
    }

    /**
     * 更新热度前十
     * @param heatRank 热度排行
     */
    public static void setHeatRank(long[] heatRank) {
        heatList.clear();
        for (long id : heatRank) {
            heatList.add(id);
        }
    }

}
