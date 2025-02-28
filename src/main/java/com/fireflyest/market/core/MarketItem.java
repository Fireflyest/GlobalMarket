package com.fireflyest.market.core;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.inventory.item.XMaterial;
import io.fireflyest.emberlib.util.ItemUtils;
import io.fireflyest.emberlib.util.TimeUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * 市场按钮物品
 * 
 * @author Fireflyest
 * @since 3.3
 */
public final class MarketItem {
    
    private static final int MS_PER_DAY = 1000 * 60 * 60 * 24;

    private static List<Long> heatList = new ArrayList<>();
    private static Map<String, Pair<ItemBuilder, Slot>> itemMap;

    private static Pair<ItemBuilder, Slot> defaultPair = 
        Pair.of(new ItemBuilder(Material.STONE), new Slot());

    private MarketItem() {

    }

    /**
     * 获取市场物品
     * 
     * @param key 键
     * @return 物品
     */
    public static Pair<ItemBuilder, Slot> getPair(@NotNull String key) {
        if (itemMap == null) {
            itemMap = YamlUtils.loadItems(GlobalMarket.getPlugin(), "items.yml");
        }
        return itemMap.getOrDefault(key, defaultPair);
    }

    /**
     * 获取物品
     * 
     * @param key 键
     * @return 物品
     */
    public static ItemBuilder getItemBuilder(@NotNull String key) {
        if (itemMap == null) {
            itemMap = YamlUtils.loadItems(GlobalMarket.getPlugin(), "items.yml");
        }
        return getPair(key).first();
    }

    /**
     * 附上交易物品信息
     * 
     * @param item 物品
     * @param transaction 交易
     */
    public static void loreItemData(ItemStack item, Transaction transaction) {
        if (item == null) {
            return;
        }
        boolean admin = false;
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        String tag = getTag(transaction.getAppear(), transaction.getId());
        final String symbol = getSymbol(transaction.getCurrency());
        final String type = transaction.getType();
        final String ownerName = ownerName(transaction.getOwner());
        switch (type) {
            case "retail":
            case "adminretail":
            case "order":
            case "adminorder":
                admin = type.startsWith("admin");
                tag += type.endsWith("r") ? Language.TAG_ORDER.get() : Language.TAG_RETAIL.get();
                ItemUtils.addLore(item, tag + (admin ? Language.TAG_ADMIN.get() : ""));
                if (!admin) {
                    ItemUtils.addLore(item, String.format(Language.GUI_SELLER.get(), ownerName));
                }
                if (transaction.getPrice() != transaction.getCost()) {
                    ItemUtils.addLore(item, String.format(
                        Language.GUI_PRICE_ORIGINAL.get(), transaction.getPrice(), symbol));
                    ItemUtils.addLore(item, String.format(
                        Language.GUI_PRICE_PRESENT.get(), transaction.getCost(), symbol));
                } else {
                    ItemUtils.addLore(item, String.format(
                        Language.GUI_PRICE_NORMAL.get(), transaction.getPrice(), symbol));
                }
                break;
            case "auction":
                ItemUtils.addLore(item, tag + Language.TAG_AUCTION.get());
                ItemUtils.addLore(item, String.format(Language.GUI_SELLER.get(), ownerName));
                ItemUtils.addLore(item, String.format(
                    Language.GUI_PRICE_START.get(), transaction.getPrice(), symbol));
                if (transaction.getPrice() != transaction.getCost()) {
                    ItemUtils.addLore(item, String.format(
                        Language.GUI_PRICE_PRESENT.get(), transaction.getCost(), symbol));
                    ItemUtils.addLore(item, String.format(
                        Language.GUI_CONFIRM.get(), 3 - transaction.getHeat()));
                }
                break;
            case "prepare":
            default:
                ItemUtils.addLore(item, Language.TAG_PREPARE.get());
                ItemUtils.addLore(item, String.format(Language.GUI_SELLER.get(), ownerName));
                break;
        }
    }

    /**
     * 附上邮件信息
     * @param item 物品
     * @param delivery 邮件数据
     */
    public static void loreMailData(ItemStack item, Delivery delivery) {
        if (item == null) {
            return;
        }
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        if (StringUtils.isNotEmpty(delivery.getInfo())) {
            ItemUtils.addLore(item, "§f" + delivery.getInfo());
        }
        ItemUtils.addLore(item, "§7- " + delivery.getSender());
        ItemUtils.addLore(item, "§7- " + TimeUtils.getLocalDate(delivery.getAppear()));
    }

    /**
     * 获取交易记录物品
     * 
     * @param textItemStack 物品
     * @param buyer 购买者
     * @param cost 花费
     * @param symbol 符号
     * @return 物品
     */
    @NotNull
    public static ItemStack getRecordItem(BaseComponent[] textItemStack, 
            String buyer, double cost, String symbol) {
        
        final ItemStack book = new ItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial())
                .name(Language.GUI_RECORD.get())
                .build();

        final BookMeta bookMeta = ((BookMeta) book.getItemMeta());
        final ComponentBuilder componentBuilder = new ComponentBuilder(Language.GUI_RECORD.get())
                .append("\n")
                .append("------------------\n");

        componentBuilder.append(Language.GUI_ITEM.get()).append(textItemStack).append("\n")
                .append(String.format(Language.GUI_BUYER.get(), buyer)).append("\n")
                .append(String.format(Language.GUI_REWARD.get(), cost, symbol)).append("\n");

        if (bookMeta != null) {
            bookMeta.setAuthor(GlobalMarket.getPlugin().getName());
            bookMeta.setTitle(Language.GUI_RECORD.get());
            bookMeta.spigot().addPage(componentBuilder.create());
        }
        book.setItemMeta(bookMeta);
        return book;
    }

    /**
     * 获取货币符号
     * @param currency 货币
     * @return 符号
     */
    public static String getSymbol(String currency) {
        switch (currency) {
            case "coin":
                return Language.SYMBOL_COIN.get();
            case "point":
                return Language.SYMBOL_POINT.get();
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
            tag += Language.TAG_HEAT.get();
        }
        final Instant appearInstant = TimeUtils.getInstant(appear);
        // 上架6小时内
        if (Instant.now().compareTo(appearInstant) < 1000 * 60 * 60 * 6) {
            tag += Language.TAG_NEW.get();
        }
        // 到期6小时内
        if (Config.TRANSACTION_EXPIRATION.get() != -1) {
            final long time = 
                appear + Config.TRANSACTION_EXPIRATION.get() * MS_PER_DAY - 1000 * 60 * 60 * 6;
            if (Instant.now().isAfter(TimeUtils.getInstant(time))) {
                tag += Language.TAG_DEADLINE.get();
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

    /**
     * 获取玩家名
     * 
     * @param uidString 玩家uid
     * @return 玩家名
     */
    public static String ownerName(String uidString) {
        return Bukkit.getOfflinePlayer(UUID.fromString(uidString)).getName();
    }

}
