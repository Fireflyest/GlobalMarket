package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.item.ViewItemBuilder;

public class MarketButton {

    public static ItemStack AIR;
    public static ItemStack MINE;
    public static ItemStack OTHER;
    public static ItemStack MARKET;
    public static ItemStack DATA;
    public static ItemStack STATISTIC;
    public static ItemStack CLASSIFY;
    public static ItemStack SIGN;
    public static ItemStack MAIL;
    public static ItemStack POINT;
    public static ItemStack ADMIN;
    public static ItemStack CLOSE;
    public static ItemStack DONE;
    public static ItemStack BLANK;
    public static ItemStack BUY_1;
    public static ItemStack BUY_8;
    public static ItemStack BUY_ALL;
    public static ItemStack CANCEL;
    public static ItemStack BID_1;
    public static ItemStack BID_10;
    public static ItemStack BID_100;
    public static ItemStack PAGE_PRE;
    public static ItemStack PAGE_NEXT;
    public static ItemStack PAGE_PRE_DISABLE;
    public static ItemStack PAGE_NEXT_DISABLE;
    public static ItemStack ADD_1;
    public static ItemStack ADD_10;
    public static ItemStack ADD_100;
    public static ItemStack REDUCE_1;
    public static ItemStack REDUCE_10;
    public static ItemStack REDUCE_100;

    public static ItemStack EDIBLE;
    public static ItemStack ITEM;
    public static ItemStack BLOCK;
    public static ItemStack BURNABLE;
    public static ItemStack INTERACTABLE;
    public static ItemStack EQUIP;
    public static ItemStack KNOWLEDGE;
    public static ItemStack SEARCH;

    static {
        AIR = new ViewItemBuilder(Material.AIR).build();
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§l我的")
                .command("mine")
                .build();
        OTHER = new ViewItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .name("§3§l他的")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§l市场")
                .command(" ")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§l个人统计")
                .command("data")
                .build();
        STATISTIC = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l市场统计")
                .command("statistic")
                .build();
        CLASSIFY = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§3§l分类")
                .command("classify")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.FEATHER.parseMaterial())
                .name("§3§l全部签收")
                .command("sign")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§l邮箱")
                .command("mail")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§l点券市场")
                .command("point")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§l系统商城")
                .command("admin")
                .build();
        CLOSE = new ViewItemBuilder(XMaterial.REDSTONE.parseMaterial())
                .name("§c§l关闭")
                .command("close")
                .build();
        DONE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§l确定")
                .build();
        BLANK = new ViewItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial())
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .build();
        BID_1 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .build();
        PAGE_NEXT = new ViewItemBuilder(XMaterial.LIME_DYE.parseMaterial())
                .name("§a§l▶")
                .command("page next")
                .build();
        PAGE_PRE = new ViewItemBuilder(XMaterial.LIME_DYE.parseMaterial())
                .name("§a§l◀")
                .command("page pre")
                .build();
        PAGE_NEXT_DISABLE = new ViewItemBuilder(XMaterial.GRAY_DYE.parseMaterial())
                .name("§7§l▷")
                .build();
        PAGE_PRE_DISABLE = new ViewItemBuilder(XMaterial.GRAY_DYE.parseMaterial())
                .name("§7§l◁")
                .build();
        ADD_1 = new ViewItemBuilder(XMaterial.YELLOW_DYE.parseMaterial())
                .name("§e§l+1")
                .command("add1")
                .build();
        ADD_10 = new ViewItemBuilder(XMaterial.YELLOW_DYE.parseMaterial())
                .name("§e§l+10")
                .command("add10")
                .build();
        ADD_100 = new ViewItemBuilder(XMaterial.YELLOW_DYE.parseMaterial())
                .name("§e§l+100")
                .command("add100")
                .build();
        REDUCE_1 = new ViewItemBuilder(XMaterial.RED_DYE.parseMaterial())
                .name("§c§l-1")
                .command("reduce1")
                .build();
        REDUCE_10 = new ViewItemBuilder(XMaterial.RED_DYE.parseMaterial())
                .name("§c§l-10")
                .command("reduce10")
                .build();
        REDUCE_100 = new ViewItemBuilder(XMaterial.RED_DYE.parseMaterial())
                .name("§c§l-100")
                .command("reduce100")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l食物")
                .lore("§f吃货专区")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l物品")
                .lore("§f杂七杂八的东西")
                .command("classify item")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l方块")
                .lore("§f搭积木")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l可燃物")
                .lore("§f燃烧吧，烈焰")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l功能方块")
                .lore("§f似乎是魔法在运作")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l装备")
                .lore("§f要生存只能战斗")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l知识")
                .lore("§f知识的力量")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l搜索")
                .lore("§f按照关键词搜索商品")
                .command("search")
                .build();
    }

    private MarketButton(){
    }



}
