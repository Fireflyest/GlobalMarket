package com.fireflyest.market.util;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Sale;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 */
public class ItemUtils {

//    private static String versionPacket;
//    private static Class<?> craftItemStackClass;
//    private static Class<?> itemStackClass;
//    private static Class<?> compoundClass;
//    private static Method asNMSCopy;
//    private static Method asBukkitCopy;
//    private static Method getTag;
//    private static Method setTag;
//    private static Method compoundSet;
//    private static Method compoundGet;

//    static {
//        String version = Bukkit.getVersion();
//        String v = version.substring(version.indexOf(".")+1, version.lastIndexOf("."));
//        int r = 1;
//        while (craftItemStackClass == null && r < 9){
//            try {
//                versionPacket = String.format("v1_%s_R%d", v, r);
//                craftItemStackClass = Class.forName(
//                        String.format("org.bukkit.craftbukkit.%s.inventory.CraftItemStack", versionPacket));
//
//                itemStackClass = Class.forName("net.minecraft.world.item.ItemStack");
//                compoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
//            } catch (ClassNotFoundException ignore) {}
//            r ++;
//        }
//        if (craftItemStackClass != null) {
//            try {
//                asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
//                asBukkitCopy = craftItemStackClass.getMethod("asBukkitCopy", itemStackClass);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
//        }
//        if (itemStackClass != null) {
//            try {
//                getTag = itemStackClass.getMethod("u");
//                setTag = itemStackClass.getMethod("c", compoundClass);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        if (compoundClass != null) {
//            try {
//                compoundSet = compoundClass.getMethod("a", String.class, String.class);
//                compoundGet = compoundClass.getMethod("l", String.class);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }

    private ItemUtils(){
    }

    /**
     * 设置物品名称
     * @param item 物品
     * @param name 名称
     */
    public static void setDisplayName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            meta.setDisplayName(name.replace("&", "§"));
            item.setItemMeta(meta);
        }
    }

    public static void setItemValue(ItemStack item, String value) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return;
        meta.setLocalizedName(value);
        item.setItemMeta(meta);
//        setItemNBT(item, "market", value);

    }

    public static String getItemValue(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if(meta == null)return "";
        return meta.getLocalizedName();
//        return getItemNBT(item, "market");
    }

//    public static ItemStack setItemNBT(ItemStack item, String key, String value) {
//        try {
//            Object craftItem = asNMSCopy.invoke(null, item);
//            Object nbt = getTag.invoke(craftItem);
//
//            compoundSet.invoke(nbt, key, value);
//
//            setTag.invoke(craftItem, nbt);
//
//            item = ((ItemStack) asBukkitCopy.invoke(null, craftItem));
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//        return item;
//    }
//
//    public static String getItemNBT(ItemStack item, String key) {
//        String value;
//        try {
//            Object craftItem = asNMSCopy.invoke(null, item);
//            Object nbt = getTag.invoke(craftItem);
//
//            value = ((String) compoundGet.invoke(nbt, key));
//
//            setTag.invoke(craftItem, nbt);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//        return value;
//    }

    /**
     * 添加注释
     * @param item 物品
     * @param lore 注释
     */
    public static void addLore(ItemStack item, String lore){
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            lores.add(lore);
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

    public static void setLore(ItemStack item, String lore, int line){
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            while (lores.size() <= line){
                lores.add("");
            }
            lores.set(line, lore);
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

    public static void loreSaleItem(ItemStack item, Sale sale){
        ItemUtils.addLore(item, "");
        if (!"null".equals(sale.getDesc())) ItemUtils.addLore(item, "§f" + sale.getDesc());
        ItemUtils.addLore(item, "§e§m·                         ·");
        ItemUtils.addLore(item, "§f"+(sale.isAuction() ? "[§7拍卖§f]":"[§7直售§f]") + (sale.isPoint() ? "[§6点券§f]" : "") + (sale.isAdmin() ? "[§c无限§f]" : ""));
        ItemUtils.addLore(item, "§3§l卖家§7: §f"+sale.getOwner());
        if(sale.getPrice() != sale.getCost()){
            ItemUtils.addLore(item, "§3§l原价§7: §f§m"+sale.getPrice());
            ItemUtils.addLore(item, "§3§l现价§7: §f"+sale.getCost());
        }else {
            ItemUtils.addLore(item, "§3§l价格§7: §f"+sale.getPrice());
        }
        ItemUtils.setItemValue(item, "affair " + sale.getId());
    }

    public static void loreMailItem(ItemStack item, Mail mail){
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        ItemUtils.addLore(item, "§3§l日期§7: §f");
        ItemUtils.addLore(item, "§f" + TimeUtils.getTime(mail.getAppear()));
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§7请尽快签收！");
        ItemUtils.setItemValue(item, "sign " + mail.getId());
    }

    @NotNull
    public static ItemStack getRecordItem(String name, String buyer, double cost, boolean point){
        ItemStack item;
        Material w_book = XMaterial.WRITTEN_BOOK.parseMaterial();
        if(null == w_book) return new ItemStack(Material.STONE);
        item = new ItemStack(w_book);

        BookMeta meta = (BookMeta)item.getItemMeta();
        if(meta != null){
            meta.setDisplayName("§e§l交易记录");
            meta.setLocalizedName("record");
            meta.setTitle("交易记录");
            String info =  "§3§l出售的物品§7: " +
                    name +
                    "\n" +
                    "§3§l买家§7: " +
                    buyer +
                    "\n" +
                    "§3§l花费§7: " +
                    cost +
                    "\n" +
                    "§3§l是否点券出售§7: " +
                    (point ? "是" : "否");
            meta.addPage(info);
            meta.setAuthor("§f§l[§7GlobeMarket§f§l]");
            item.setItemMeta(meta);
        }
        return item;
    }

}
