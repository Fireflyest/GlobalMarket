package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.util.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MarketItem {

    public static ItemStack AIR;
    public static ItemStack MINE;
    public static ItemStack MARKET;
    public static ItemStack MARKET_ALL;
    public static ItemStack DATA;
    public static ItemStack STATISTIC;
    public static ItemStack CLASSIFY;
    public static ItemStack SIGN;
    public static ItemStack MAIL;
    public static ItemStack MENU;
    public static ItemStack POINT;
    public static ItemStack ADMIN;
    public static ItemStack CLOSE;
    public static ItemStack DONE;
    public static ItemStack BLANK;
    public static ItemStack BOOK;
    public static ItemStack BUY_1;
    public static ItemStack BUY_8;
    public static ItemStack BUY_ALL;
    public static ItemStack CANCEL;
    public static ItemStack ADD_NUGGET;
    public static ItemStack ADD_INGOT;
    public static ItemStack ADD_BLOCK;
    public static ItemStack PAGE;
    public static ItemStack PAGE_PRE;
    public static ItemStack PAGE_NEXT;
    public static ItemStack ADD_1;
    public static ItemStack ADD_10;
    public static ItemStack ADD_100;
    public static ItemStack REDUCE_1;
    public static ItemStack REDUCE_10;
    public static ItemStack REDUCE_100;
    public static ItemStack AMOUNT;
    public static ItemStack ERROR;

    public static ItemStack EDIBLE;
    public static ItemStack ITEM;
    public static ItemStack BLOCK;
    public static ItemStack BURNABLE;
    public static ItemStack INTERACTABLE;
    public static ItemStack EQUIP;
    public static ItemStack KNOWLEDGE;
    public static ItemStack SEARCH;

    private MarketItem(){
    }

    static {

        AIR = new ItemStack(Material.AIR);

        Material mine = XMaterial.CHEST.parseMaterial();
        if(null != mine){
            MINE = new ItemStack(mine);
            ItemUtils.setDisplayName(MINE, "§3§l我的");
            ItemUtils.addLore(MINE, "§f点击打开个人");
            ItemUtils.setItemValue(MINE, "mine");
        }

        Material market = XMaterial.ENDER_CHEST.parseMaterial();
        if(null != market){
            MARKET = new ItemStack(market);
            ItemUtils.setDisplayName(MARKET, "§3§l玩家市场");
            ItemUtils.addLore(MARKET, "§f点击回到玩家交易市场");
            ItemUtils.addLore(MARKET, "§f普通交易与点券交易");
            ItemUtils.setItemValue(MARKET, " ");
        }

        Material marketAll = XMaterial.GOLD_INGOT.parseMaterial();
        if(null != marketAll){
            MARKET_ALL = new ItemStack(marketAll);
            ItemUtils.setDisplayName(MARKET_ALL, "§3§l总市场");
            ItemUtils.addLore(MARKET_ALL, "§f点击前往总市场");
            ItemUtils.addLore(MARKET_ALL, "§f普通交易、点券交易以及系统商店");
            ItemUtils.setItemValue(MARKET_ALL, "all");
        }

        Material data = XMaterial.BOOK.parseMaterial();
        if(null != data){
            DATA = new ItemStack(data);
            ItemUtils.setDisplayName(DATA, "§3§l个人统计");
            ItemUtils.addLore(DATA, "§f点击查看");
            ItemUtils.setItemValue(DATA, "data");
        }

        Material statistic = XMaterial.BOOKSHELF.parseMaterial();
        if(null != statistic){
            STATISTIC = new ItemStack(statistic);
            ItemUtils.setDisplayName(STATISTIC, "§3§l市场统计");
            ItemUtils.addLore(STATISTIC, "§f点击查看");
            ItemUtils.setItemValue(STATISTIC, "statistic");
        }

        Material classify = XMaterial.ENDER_EYE.parseMaterial();
        if(null != classify){
            CLASSIFY = new ItemStack(classify);
            ItemUtils.setDisplayName(CLASSIFY, "§3§l分类");
            ItemUtils.addLore(CLASSIFY, "§f点击查看所有分类");
            ItemUtils.setItemValue(CLASSIFY, "classify");
        }

        Material mail = XMaterial.JUKEBOX.parseMaterial();
        if(null != mail){
            MAIL = new ItemStack(mail);
            ItemUtils.setDisplayName(MAIL, "§3§l邮箱");
            ItemUtils.addLore(MAIL, "§f点击打开");
            ItemUtils.setItemValue(MAIL, "mail");
        }

        Material menu = XMaterial.CLOCK.parseMaterial();
        if(null != menu){
            MENU = new ItemStack(menu);
            ItemUtils.setDisplayName(MENU, "§3§l菜单");
            ItemUtils.addLore(MENU, "§f点击打开");
            ItemUtils.setItemValue(MENU, "menu");
        }

        Material point = XMaterial.DIAMOND.parseMaterial();
        if(null != point){
            POINT = new ItemStack(point);
            ItemUtils.setDisplayName(POINT, "§3§l点券商城");
            ItemUtils.addLore(POINT, "§f点击打开");
            ItemUtils.addLore(POINT, "§f点券交易的所有商品");
            ItemUtils.setItemValue(POINT, "point");
        }

        Material admin = XMaterial.ITEM_FRAME.parseMaterial();
        if(null != admin){
            ADMIN = new ItemStack(admin);
            ItemUtils.setDisplayName(ADMIN, "§3§l系统商城");
            ItemUtils.addLore(ADMIN, "§f点击打开");
            ItemUtils.addLore(ADMIN, "§f系统交易商品");
            ItemUtils.setItemValue(ADMIN, "admin");
        }

        Material clean = XMaterial.FEATHER.parseMaterial();
        if(null != clean){
            SIGN = new ItemStack(clean);
            ItemUtils.setDisplayName(SIGN, "§3§l签收");
            ItemUtils.addLore(SIGN, "§f全部签收");
            ItemUtils.setItemValue(SIGN, "sign");
        }

        Material close = XMaterial.REDSTONE.parseMaterial();
        if(null != close){
            CLOSE = new ItemStack(close);
            ItemUtils.setDisplayName(CLOSE, "§c§l关闭");
            ItemUtils.addLore(CLOSE, "§f点击关闭");
            ItemUtils.setItemValue(CLOSE, "close");
        }

        Material done = XMaterial.EMERALD.parseMaterial();
        if(null != done){
            DONE = new ItemStack(done);
            ItemUtils.setDisplayName(DONE, "§c§l确定");
            ItemUtils.addLore(DONE, "§f请设置出售价格！");

        }

        Material blank = XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial();
        if(null != blank){
            BLANK = new ItemStack(blank);
            ItemUtils.setDisplayName(BLANK, " ");
        }

        Material book = XMaterial.WRITABLE_BOOK.parseMaterial();
        if(null != book){
            BOOK = new ItemStack(book);
        }

        Material buy1 = XMaterial.PRISMARINE_CRYSTALS.parseMaterial();
        if(null != buy1){
            BUY_1 = new ItemStack(buy1);
        }
        Material buy2 = XMaterial.PRISMARINE_CRYSTALS.parseMaterial();
        if(null != buy2){
            BUY_8 = new ItemStack(buy2);
        }
        Material buy3 = XMaterial.PRISMARINE_CRYSTALS.parseMaterial();
        if(null != buy3){
            BUY_ALL = new ItemStack(buy3);
        }

        Material cancel = XMaterial.HOPPER.parseMaterial();
        if(null != cancel){
            CANCEL = new ItemStack(cancel);
        }

        Material add = XMaterial.GOLD_NUGGET.parseMaterial();
        Material addIngot = XMaterial.GOLD_INGOT.parseMaterial();
        Material addBlock = XMaterial.GOLD_BLOCK.parseMaterial();
        if(null != add){
            ADD_NUGGET = new ItemStack(add);
        }
        if (addIngot != null) {
            ADD_INGOT = new ItemStack(addIngot);
        }
        if (addBlock != null) {
            ADD_BLOCK = new ItemStack(addBlock);
        }

        Material page = XMaterial.PAPER.parseMaterial();
        if(null != page){
            PAGE = new ItemStack(page);
            ItemUtils.setDisplayName(PAGE, "§3§l换页");
            ItemUtils.addLore(PAGE, "§f左右键");
        }

        Material pagePre = XMaterial.PAPER.parseMaterial();
        if(null != pagePre){
            PAGE_PRE = new ItemStack(pagePre);
            ItemUtils.setDisplayName(PAGE_PRE, "§3§l上一页");
        }

        Material pageNext = XMaterial.PAPER.parseMaterial();
        if(null != pageNext){
            PAGE_NEXT = new ItemStack(pageNext);
            ItemUtils.setDisplayName(PAGE_NEXT, "§3§l下一页");
        }

        Material addMoney = XMaterial.YELLOW_DYE.parseMaterial();
        Material reduceMoney = XMaterial.RED_DYE.parseMaterial();
        if (addMoney != null) {
            ADD_1 = new ItemStack(addMoney);
            ItemUtils.setDisplayName(ADD_1, "§e§l+1");
            ItemUtils.setItemValue(ADD_1, "add1");

            ADD_10 = new ItemStack(addMoney);
            ItemUtils.setDisplayName(ADD_10, "§e§l+10");
            ItemUtils.setItemValue(ADD_10, "add10");

            ADD_100 = new ItemStack(addMoney);
            ItemUtils.setDisplayName(ADD_100, "§e§l+100");
            ItemUtils.setItemValue(ADD_100, "add100");
        }
        if (reduceMoney != null) {
            REDUCE_1 = new ItemStack(reduceMoney);
            ItemUtils.setDisplayName(REDUCE_1, "§c§l-1");
            ItemUtils.setItemValue(REDUCE_1, "reduce1");

            REDUCE_10 = new ItemStack(reduceMoney);
            ItemUtils.setDisplayName(REDUCE_10, "§c§l-10");
            ItemUtils.setItemValue(REDUCE_10, "reduce10");

            REDUCE_100 = new ItemStack(reduceMoney);
            ItemUtils.setDisplayName(REDUCE_100, "§c§l-100");
            ItemUtils.setItemValue(REDUCE_100, "reduce100");
        }

        Material amount = XMaterial.HOPPER_MINECART.parseMaterial();
        if (amount != null) {
            AMOUNT = new ItemStack(amount);
            ItemUtils.setDisplayName(AMOUNT, "§c§l修改物品数量");
            ItemUtils.addLore(AMOUNT, "§f左键减少，右键增加");
            ItemUtils.setItemValue(AMOUNT, "amount");
        }

        Material error = XMaterial.BARRIER.parseMaterial();
        if(null != error){
            ERROR = new ItemStack(error);
            ItemUtils.setDisplayName(ERROR, "§c§l错误");
            ItemUtils.addLore(ERROR, "§f按钮错误");
            ItemUtils.setItemValue(ERROR, "help");
        }


//      "edible", "item", "block", "record", "burnable", "interactable", "occluding", "solid", "equip", "knowledge"
        Material edible = XMaterial.CARROT.parseMaterial();
        if(null != edible){
            EDIBLE = new ItemStack(edible);
            ItemUtils.setDisplayName(EDIBLE, "§3§l食物");
            ItemUtils.addLore(EDIBLE, "§f吃货专区");
            ItemUtils.setItemValue(EDIBLE, "classify edible");
        }

        Material item = XMaterial.STICK.parseMaterial();
        if(null != item){
            ITEM = new ItemStack(item);
            ItemUtils.setDisplayName(ITEM, "§3§l物品");
            ItemUtils.addLore(ITEM, "§f杂七杂八的东西");
            ItemUtils.setItemValue(ITEM, "classify item");
        }

        Material block = XMaterial.QUARTZ_BLOCK.parseMaterial();
        if(null != block){
            BLOCK = new ItemStack(block);
            ItemUtils.setDisplayName(BLOCK, "§3§l方块");
            ItemUtils.addLore(BLOCK, "§f搭积木吗");
            ItemUtils.setItemValue(BLOCK, "classify block");
        }

        Material burnable = XMaterial.COAL.parseMaterial();
        if(null != burnable){
            BURNABLE = new ItemStack(burnable);
            ItemUtils.setDisplayName(BURNABLE, "§3§l可燃物");
            ItemUtils.addLore(BURNABLE, "§f燃烧吧，烈焰");
            ItemUtils.setItemValue(BURNABLE, "classify burnable");
        }

        Material interactable = XMaterial.FURNACE.parseMaterial();
        if(null != interactable){
            INTERACTABLE = new ItemStack(interactable);
            ItemUtils.setDisplayName(INTERACTABLE, "§3§l功能方块");
            ItemUtils.addLore(INTERACTABLE, "§f似乎是魔法在运作");
            ItemUtils.setItemValue(INTERACTABLE, "classify interactable");
        }

        Material equip = XMaterial.GOLDEN_SWORD.parseMaterial();
        if(null != equip){
            EQUIP = new ItemStack(equip);
            ItemUtils.setDisplayName(EQUIP, "§3§l装备");
            ItemUtils.addLore(EQUIP, "§f要生存只能战斗");
            ItemUtils.setItemValue(EQUIP, "classify equip");
        }

        Material knowledge = XMaterial.BOOKSHELF.parseMaterial();
        if(null != knowledge){
            KNOWLEDGE = new ItemStack(knowledge);
            ItemUtils.setDisplayName(KNOWLEDGE, "§3§l知识");
            ItemUtils.addLore(KNOWLEDGE, "§f知识就是力量");
            ItemUtils.setItemValue(KNOWLEDGE, "classify knowledge");
        }

        Material search = XMaterial.COMPASS.parseMaterial();
        if(null != search){
            SEARCH = new ItemStack(search);
            ItemUtils.setDisplayName(SEARCH, "§3§l搜索");
            ItemUtils.addLore(SEARCH, "§f按照关键词搜索商品");
            ItemUtils.setItemValue(SEARCH, "search");
        }

    }

    public static ItemStack getPageItem(int i){
        ItemStack tmp = PAGE.clone();
        tmp.setAmount(i);
        ItemUtils.addLore(tmp, "§7<§e"+(i)+"§7>§0");
        ItemUtils.setItemValue(tmp, "page");
        return tmp;
    }

    public static ItemStack getPrePageItem(int i){
        ItemStack tmp = PAGE_PRE.clone();
        ItemUtils.addLore(tmp, "§7<§e"+(i)+"§7>§0");
        ItemUtils.setItemValue(tmp, "page pre");
        return tmp;
    }

    public static ItemStack getNextPageItem(int i){
        ItemStack tmp = PAGE_NEXT.clone();
        ItemUtils.addLore(tmp, "§7<§e"+(i)+"§7>§0");
        ItemUtils.setItemValue(tmp, "page next");
        return tmp;
    }

}
