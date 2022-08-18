package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Button;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.TimeUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.fireflyest.craftgui.item.ViewItemBuilder;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

public class MarketButton {

    public static ItemStack AIR = new ViewItemBuilder(Material.AIR).build();
    public static ItemStack MINE;
    public static ItemStack OTHER;
    public static ItemStack MARKET;
    public static ItemStack AUCTION;
    public static ItemStack COLLECT;
    public static ItemStack RETAIL;
    public static ItemStack WAIT;
    public static ItemStack TRADE;
    public static ItemStack DATA;
    public static ItemStack SALE_DATA;
    public static ItemStack STATISTIC;
    public static ItemStack CLASSIFY;
    public static ItemStack SEND;
    public static ItemStack SIGN;
    public static ItemStack DELETE;
    public static ItemStack MAIL;
    public static ItemStack TRANSPORT;
    public static ItemStack POINT;
    public static ItemStack COIN;
    public static ItemStack ADMIN;
    public static ItemStack CLOSE;
    public static ItemStack BACK;
    public static ItemStack DONE;
    public static ItemStack EDIT;
    public static ItemStack CLEAR;
    public static ItemStack DOT;
    public static ItemStack NUM0;
    public static ItemStack NUM1;
    public static ItemStack NUM2;
    public static ItemStack NUM3;
    public static ItemStack NUM4;
    public static ItemStack NUM5;
    public static ItemStack NUM6;
    public static ItemStack NUM7;
    public static ItemStack NUM8;
    public static ItemStack NUM9;
    public static ItemStack BLANK;
    public static ItemStack SELL;
    public static ItemStack SELL_VIP;
    public static ItemStack SELL_OP;
    public static ItemStack BUY_1;
    public static ItemStack BUY_8;
    public static ItemStack BUY_ALL;
    public static ItemStack CANCEL;
    public static ItemStack BID_10;
    public static ItemStack BID_100;
    public static ItemStack BID_1000;
    public static ItemStack PAGE_PRE;
    public static ItemStack PAGE_NEXT;
    public static ItemStack PAGE_PRE_DISABLE;
    public static ItemStack PAGE_NEXT_DISABLE;

    public static ItemStack EDIBLE;
    public static ItemStack ITEM;
    public static ItemStack BLOCK;
    public static ItemStack BURNABLE;
    public static ItemStack INTERACTABLE;
    public static ItemStack EQUIP;
    public static ItemStack KNOWLEDGE;
    public static ItemStack SEARCH;

    public static String PREPARE_TEXT;
    public static String SELLER_TEXT;
    public static String BUYER_TEXT;
    public static String AUCTIONER_TEXT;
    public static String AUCTION_TEXT;
    public static String BID_TIME_TEXT;
    public static String START_PRICE_TEXT;
    public static String RETAIL_TEXT;
    public static String UNLIMITED_TEXT;
    public static String ORIGINAL_PRICE_TEXT;
    public static String PRESENT_PRICE_TEXT;
    public static String PRICE_TEXT;
    public static String MARKET_RECORD_TEXT;
    public static String SALE_ITEM_TEXT;
    public static String REWARD_TEXT;
    public static String TOTAL_SALE_PRICE;
    public static String TOTAL_SALE_AMOUNT;
    public static String TOTAL_SALE_LIST;
    public static String CIRCULATE_PRICE;
    public static String CIRCULATE_AMOUNT;
    public static String MAX_PRICE;
    public static String SALE_HEAT;
    public static String PLAYER_CREDIT;
    public static String PLAYER_BLACK;
    public static String PLAYER_SELLING;
    public static String PLAYER_STATISTIC_AMOUNT;
    public static String PLAYER_STATISTIC_MONEY;

    static {
        DOT = new ViewItemBuilder(XMaterial.STONE_BUTTON.parseMaterial())
                .name("§3§l.")
                .build();
        NUM0 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l0")
                .build();
        NUM1 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l1")
                .build();
        NUM2 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l2")
                .build();
        NUM3 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l3")
                .build();
        NUM4 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l4")
                .build();
        NUM5 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l5")
                .build();
        NUM6 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l6")
                .build();
        NUM7 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l7")
                .build();
        NUM8 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l8")
                .build();
        NUM9 = new ViewItemBuilder(XMaterial.WHITE_BANNER.parseMaterial())
                .name("§3§l9")
                .build();
        BLANK = new ViewItemBuilder(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial())
                .name(" ")
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
        CLOSE = new ViewItemBuilder(XMaterial.REDSTONE.parseMaterial())
                .name("§c§l❌")
                .command("close")
                .model(5280000)
                .build();
        DONE = new ViewItemBuilder(XMaterial.SLIME_BALL.parseMaterial())
                .name("§3§l✔")
                .build();
        CLEAR = new ViewItemBuilder(XMaterial.REPEATER.parseMaterial())
                .name("§3§l←")
                .build();
        SELL = new ViewItemBuilder(XMaterial.LIME_STAINED_GLASS_PANE.parseMaterial())
                .name("§f§l[↓]")
                .command("sell")
                .build();
        SELL_VIP = new ViewItemBuilder(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial())
                .name("§e§l[↓]")
                .build();
        SELL_OP = new ViewItemBuilder(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial())
                .name("§c§l[↓]")
                .build();
        WAIT = new ViewItemBuilder(XMaterial.PAINTING.parseMaterial())
                .name("§3§l...")
                .build();
        OTHER = new ViewItemBuilder(XMaterial.PLAYER_HEAD.parseMaterial())
                .name("§3§lShop of %player%")
                .build();
        EDIT = new ViewItemBuilder(XMaterial.NAME_TAG.parseMaterial())
                .name("§3§l✍")
                .build();
        BACK = new ViewItemBuilder(XMaterial.REDSTONE.parseMaterial())
                .name("§c§l«")
                .command(" ")
                .build();
    }

    private MarketButton(){
    }

    public static void loadButton(){
        if (Config.LANG.startsWith("zh")){
            loadChineseMenuButton();
        }  else if (Config.LANG.startsWith("de")) {
            loadGermanMenuButton();
        } else if (Config.LANG.startsWith("fr")) {
            loadFrenchMenuButton();
        } else if (Config.LANG.startsWith("ja")) {
            loadJapaneseManuButton();
        } else if (Config.LANG.startsWith("ru")) {
            loadRussianMenuButton();
        } else {
            loadManuButton();
        }

        List<Button> buttonList = GlobalMarket.getData().query(Button.class);
        for (Button button : buttonList) {
            try {
                Field field = MarketButton.class.getField(button.getTarget().toUpperCase());
                ItemStack buttonItem = ((ItemStack) field.get(null));
                buttonItem.setType(Material.valueOf(button.getMaterial()));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //⛏⚒🔱〽⚠♻✈⚓🎣✂Ⓜℹ❤▶⏩◀⏪🍖🌊🔥⛄☄☀⭐🧪⚗🛡🗡🪓♀♂🏹♠♥♦♣↔⏭⏯⏮⏸⏹⏺⏏✳✴❇
    private static void loadManuButton() {
        // 加载物品
        //<editor-fold defaultstate="collapsed" desc="loadMenuButton">
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§lMine")
                .command("mine")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§lMarket")
                .command(" ")
                .build();
        AUCTION = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§3§lAuction")
                .command("auction")
                .build();
        COLLECT = new ViewItemBuilder(XMaterial.BUCKET.parseMaterial())
                .name("§3§lCollect")
                .command("collect")
                .build();
        RETAIL = new ViewItemBuilder(XMaterial.MAP.parseMaterial())
                .name("§3§lRetail")
                .command("retail")
                .build();
        TRADE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§lTrade")
                .command("trade")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§lPersonal Data")
                .command("data")
                .build();
        SALE_DATA = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§3§lSale Data")
                .build();
        STATISTIC = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§lMarket Statistic")
                .command("statistic")
                .build();
        CLASSIFY = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§3§lClassify")
                .command("classify")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.FEATHER.parseMaterial())
                .name("§3§lSign All")
                .command("sign")
                .build();
        DELETE = new ViewItemBuilder(XMaterial.LAVA_BUCKET.parseMaterial())
                .name("§3§lDestroy")
                .command("delete")
                .build();
        SEND = new ViewItemBuilder(XMaterial.END_PORTAL_FRAME.parseMaterial())
                .name("§3§lSend Mail")
                .command("send")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§lMail Box")
                .command("mail")
                .build();
        TRANSPORT = new ViewItemBuilder(XMaterial.CHEST_MINECART.parseMaterial())
                .name("§3§lTransport Vehicle")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§lPoint")
                .command("point")
                .build();
        COIN = new ViewItemBuilder(XMaterial.SUNFLOWER.parseMaterial())
                .name("§3§lCoin")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§lAuthority")
                .command("admin")
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lBuy One")
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lBuy Some")
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lBuy All")
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .name("§3§lCancel")
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .name("§e§lBid")
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§lBid")
                .build();
        BID_1000 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .name("§e§lBid")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EDIBLE_TITLE)
                .lore("§fThey are all edible")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_ITEM_TITLE)
                .lore("§fVarious item")
                .command("classify items")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BLOCK_TITLE)
                .lore("§fMaterial of construction")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BURNABLE_TITLE)
                .lore("§fRoaring flame")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_INTERACTABLE_TITLE)
                .lore("§fSome block interactable")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EQUIP_TITLE)
                .lore("§fTo survive")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_KNOWLEDGE_TITLE)
                .lore("§fKnowledge is power")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.COMPASS.parseMaterial())
                .name("§3§lSearch")
                .command("search")
                .build();
        //</editor-fold>
        // 文本
        //<editor-fold desc="loadMenuText">
        PREPARE_TEXT = "§f[§8Prepare§f]";
        AUCTION_TEXT = "§f[§eAuction§f]";
        RETAIL_TEXT = "§f[§7Retail§f]";
        UNLIMITED_TEXT = "[§cUnlimited§f]";
        SELLER_TEXT = "§3§lSeller§7: §f%s";
        BUYER_TEXT = "§3§lBuyer§7: §f%s";
        AUCTIONER_TEXT = "§3§lAuctioner§7: §f%s";
        START_PRICE_TEXT = "§3§lStart Price§7: §f§m%s%s";
        PRESENT_PRICE_TEXT = "§3§lPresent Price§7: §f%s%s";
        BID_TIME_TEXT = "§7The §3%s§7 Confirmation";
        ORIGINAL_PRICE_TEXT = "§3§lOriginal Price§7: §f§m%s%s";
        PRICE_TEXT = "§3§lPrice§7: §f%s%s";

        MARKET_RECORD_TEXT = "§e§lMarket Record";
        SALE_ITEM_TEXT = "§3§lSale item§f: %s";
        REWARD_TEXT = "§3§lReward§f: %s%s";

        TOTAL_SALE_PRICE = "§3§lTotal Sales Price§7: §0%s";
        TOTAL_SALE_AMOUNT = "§3§lTotal Sales Amount§7: §0%s";
        TOTAL_SALE_LIST = "§3§lTotal Sales§7: ";

        CIRCULATE_PRICE = "§3§lCirculate§7: §0%s";
        CIRCULATE_AMOUNT = "§3§lAmount§7: §0%s";
        MAX_PRICE = "§3§lMax Price§7: §0%s";

        SALE_HEAT = "§3§lSale Heat§7: §0%s";

        PLAYER_CREDIT = "§3§lCredit§7: §0%s";
        PLAYER_BLACK = "§3§lBlacklist§7: §0%s";
        PLAYER_SELLING = "§3§lSelling§7: §0%s";
        PLAYER_STATISTIC_AMOUNT = "§3§lTransaction Amount§7: §0%s";
        PLAYER_STATISTIC_MONEY = "§3§lTransaction Money§7: §0%s";
        //</editor-fold>
    }

    private static void loadRussianMenuButton() {
        // 加载物品
        //<editor-fold defaultstate="collapsed" desc="loadMenuButton">
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§lмой")
                .command("mine")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§lрынок")
                .command(" ")
                .build();
        AUCTION = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§3§lаукцион")
                .command("auction")
                .build();
        COLLECT = new ViewItemBuilder(XMaterial.BUCKET.parseMaterial())
                .name("§3§lкупить")
                .command("collect")
                .build();
        RETAIL = new ViewItemBuilder(XMaterial.MAP.parseMaterial())
                .name("§3§lрозница")
                .command("retail")
                .build();
        TRADE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§lбартер")
                .command("trade")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§lличные данные")
                .command("data")
                .build();
        SALE_DATA = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§3§lДанные продукта")
                .build();
        STATISTIC = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§lСтатистика рынка")
                .command("statistic")
                .build();
        CLASSIFY = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§3§lКлассификация")
                .command("classify")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.FEATHER.parseMaterial())
                .name("§3§lВсе подписано")
                .command("sign")
                .build();
        DELETE = new ViewItemBuilder(XMaterial.LAVA_BUCKET.parseMaterial())
                .name("§3§lмусорное ведро")
                .command("delete")
                .build();
        SEND = new ViewItemBuilder(XMaterial.END_PORTAL_FRAME.parseMaterial())
                .name("§3§lотправить по почте")
                .command("send")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§lпочтовый ящик")
                .command("mail")
                .build();
        TRANSPORT = new ViewItemBuilder(XMaterial.CHEST_MINECART.parseMaterial())
                .name("§3§lгрузовик")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§lPoints")
                .command("point")
                .build();
        COIN = new ViewItemBuilder(XMaterial.SUNFLOWER.parseMaterial())
                .name("§3§lCoins")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§lофициальный")
                .command("admin")
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lОдин")
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lчасть")
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lвсе")
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .name("§3§lОтмена")
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .name("§e§lставка")
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§lставка")
                .build();
        BID_1000 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .name("§e§lставка")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EDIBLE_TITLE)
                .lore("§fчто-нибудь съесть")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_ITEM_TITLE)
                .lore("§fразличные вещи")
                .command("classify item")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BLOCK_TITLE)
                .lore("§fстроительные блоки")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BURNABLE_TITLE)
                .lore("§fгореть, пламя")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_INTERACTABLE_TITLE)
                .lore("§fКажется, магия работает")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EQUIP_TITLE)
                .lore("§fвооружись")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_KNOWLEDGE_TITLE)
                .lore("§fсила знания")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.COMPASS.parseMaterial())
                .name("§3§lпоиск")
                .command("search")
                .build();
        //</editor-fold>
        // 文本
        //<editor-fold desc="loadMenuText">
        PREPARE_TEXT = "§f[§8Предпродажа§f]";
        AUCTION_TEXT = "§f[§eаукцион§f]";
        RETAIL_TEXT = "§f[§7розница§f]";
        UNLIMITED_TEXT = "[§cнеограниченный§f]";
        SELLER_TEXT = "§3§lпродавца§7: §f%s";
        BUYER_TEXT = "§3§lпокупатель§7: §f%s";
        AUCTIONER_TEXT = "§3§lаукционист§7: §f%s";
        START_PRICE_TEXT = "§3§lСтартовая цена§7: §f§m%s%s";
        PRESENT_PRICE_TEXT = "§3§lтекущая цена§7: §f%s%s";
        BID_TIME_TEXT = "§3%s§72-е подтверждение";
        ORIGINAL_PRICE_TEXT = "§3§lисходная цена§7: §f§m%s%s";
        PRICE_TEXT = "§3§lцена§7: §f%s%s";

        MARKET_RECORD_TEXT = "§e§lЗапись транзакции";
        SALE_ITEM_TEXT = "§3§lтоварный§f: %s";
        REWARD_TEXT = "§3§lнаграда§f: %s%s";

        TOTAL_SALE_PRICE = "§3§lИтоговая цена§7: §0%s";
        TOTAL_SALE_AMOUNT = "§3§lколичество товаров§7: §0%s";
        TOTAL_SALE_LIST = "§3§lНомер продукта§7: ";

        CIRCULATE_PRICE = "§3§lСумма сделки§7: §0%s";
        CIRCULATE_AMOUNT = "§3§lКоличество транзакций§7: §0%s";
        MAX_PRICE = "§3§lМаксимальная сумма транзакции§7: §0%s";

        SALE_HEAT = "§3§lнагревать§7: §0%s";

        PLAYER_CREDIT = "§3§lрепутация§7: §0%s";
        PLAYER_BLACK = "§3§lчерный список§7: §0%s";
        PLAYER_SELLING = "§3§lв наличии§7: §0%s";
        PLAYER_STATISTIC_AMOUNT = "§3§lСовокупное количество транзакций§7: §0%s";
        PLAYER_STATISTIC_MONEY = "§3§lСовокупная сумма транзакции§7: §0%s";
        //</editor-fold>
    }

    private static void loadJapaneseManuButton() {
        // 加载物品
        //<editor-fold defaultstate="collapsed" desc="loadMenuButton">
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§l私の")
                .command("mine")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§l市場")
                .command(" ")
                .build();
        AUCTION = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§3§l競売")
                .command("auction")
                .build();
        COLLECT = new ViewItemBuilder(XMaterial.BUCKET.parseMaterial())
                .name("§3§l購入")
                .command("collect")
                .build();
        RETAIL = new ViewItemBuilder(XMaterial.MAP.parseMaterial())
                .name("§3§l小売")
                .command("retail")
                .build();
        TRADE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§lバーター")
                .command("trade")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§l個人データ")
                .command("data")
                .build();
        SALE_DATA = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§3§l製品データ")
                .build();
        STATISTIC = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l市場統計")
                .command("statistic")
                .build();
        CLASSIFY = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§3§l分類")
                .command("classify")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.FEATHER.parseMaterial())
                .name("§3§lすべて署名")
                .command("sign")
                .build();
        DELETE = new ViewItemBuilder(XMaterial.LAVA_BUCKET.parseMaterial())
                .name("§3§lゴミ箱")
                .command("delete")
                .build();
        SEND = new ViewItemBuilder(XMaterial.END_PORTAL_FRAME.parseMaterial())
                .name("§3§l郵送で送る")
                .command("send")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§l郵便受け")
                .command("mail")
                .build();
        TRANSPORT = new ViewItemBuilder(XMaterial.CHEST_MINECART.parseMaterial())
                .name("§3§lトラック")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§lPoints")
                .command("point")
                .build();
        COIN = new ViewItemBuilder(XMaterial.SUNFLOWER.parseMaterial())
                .name("§3§lCoins")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§l権威ある市場")
                .command("admin")
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§l一")
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§l部")
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§l即購入")
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .name("§3§l取消")
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .name("§e§l入札")
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§l入札")
                .build();
        BID_1000 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .name("§e§l入札")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EDIBLE_TITLE)
                .lore("§fフードゾーン")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_ITEM_TITLE)
                .lore("§f雑多なこと")
                .command("classify item")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BLOCK_TITLE)
                .lore("§fビルディングブロック")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BURNABLE_TITLE)
                .lore("§f燃やす、炎")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_INTERACTABLE_TITLE)
                .lore("§f魔法が効いているようです")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EQUIP_TITLE)
                .lore("§f戦うだけで生き残るために")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_KNOWLEDGE_TITLE)
                .lore("§f知識の力")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.COMPASS.parseMaterial())
                .name("§3§l検索")
                .command("search")
                .build();
        //</editor-fold>
        // 文本
        //<editor-fold desc="loadMenuText">
        PREPARE_TEXT = "§f[§8プレセール§f]";
        AUCTION_TEXT = "§f[§e競売§f]";
        RETAIL_TEXT = "§f[§7小売§f]";
        UNLIMITED_TEXT = "[§c無制限§f]";
        SELLER_TEXT = "§3§l売り手§7: §f%s";
        BUYER_TEXT = "§3§l買い手§7: §f%s";
        AUCTIONER_TEXT = "§3§l競売人§7: §f%s";
        START_PRICE_TEXT = "§3§l開始価格§7: §f§m%s%s";
        PRESENT_PRICE_TEXT = "§3§l現在の価格§7: §f%s%s";
        BID_TIME_TEXT = "§3%s§7回目の料金確認";
        ORIGINAL_PRICE_TEXT = "§3§l元値§7: §f§m%s%s";
        PRICE_TEXT = "§3§l価格§7: §f%s%s";

        MARKET_RECORD_TEXT = "§e§l取引記録";
        SALE_ITEM_TEXT = "§3§l貿易品§f: %s";
        REWARD_TEXT = "§3§l褒美§f: %s%s";

        TOTAL_SALE_PRICE = "§3§l商品の合計金額§7: §0%s";
        TOTAL_SALE_AMOUNT = "§3§l製品数§7: §0%s";
        TOTAL_SALE_LIST = "§3§l製品番号§7: ";

        CIRCULATE_PRICE = "§3§l取引金額§7: §0%s";
        CIRCULATE_AMOUNT = "§3§l取引回数§7: §0%s";
        MAX_PRICE = "§3§l最大取引金額§7: §0%s";

        SALE_HEAT = "§3§l熱§7: §0%s";

        PLAYER_CREDIT = "§3§l信用度§7: §0%s";
        PLAYER_BLACK = "§3§lブラックリスト§7: §0%s";
        PLAYER_SELLING = "§3§l在庫あり§7: §0%s";
        PLAYER_STATISTIC_AMOUNT = "§3§l累計取引数§7: §0%s";
        PLAYER_STATISTIC_MONEY = "§3§l累計取引金額§7: §0%s";
        //</editor-fold>
    }

    private static void loadFrenchMenuButton() {
        // 加载物品
        //<editor-fold defaultstate="collapsed" desc="loadMenuButton">
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§lLa mienne")
                .command("mine")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§lMarché")
                .command(" ")
                .build();
        AUCTION = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§3§lVente aux enchères")
                .command("auction")
                .build();
        COLLECT = new ViewItemBuilder(XMaterial.BUCKET.parseMaterial())
                .name("§3§lAcquisitions")
                .command("collect")
                .build();
        RETAIL = new ViewItemBuilder(XMaterial.MAP.parseMaterial())
                .name("§3§lDe détail")
                .command("retail")
                .build();
        TRADE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§lTroc")
                .command("trade")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§lDonnées personnelles")
                .command("data")
                .build();
        SALE_DATA = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§3§lDonnées sur les produits de base")
                .build();
        STATISTIC = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§lStatistiques du marché")
                .command("statistic")
                .build();
        CLASSIFY = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§3§lClassification")
                .command("classify")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.FEATHER.parseMaterial())
                .name("§3§lSignez tout")
                .command("sign")
                .build();
        DELETE = new ViewItemBuilder(XMaterial.LAVA_BUCKET.parseMaterial())
                .name("§3§lSupprimer")
                .command("delete")
                .build();
        SEND = new ViewItemBuilder(XMaterial.END_PORTAL_FRAME.parseMaterial())
                .name("§3§lPar la poste")
                .command("send")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§lMailbox")
                .command("mail")
                .build();
        TRANSPORT = new ViewItemBuilder(XMaterial.CHEST_MINECART.parseMaterial())
                .name("§3§lVéhicule de transport")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§lPoints")
                .command("point")
                .build();
        COIN = new ViewItemBuilder(XMaterial.SUNFLOWER.parseMaterial())
                .name("§3§lCoins")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§lOfficial")
                .command("admin")
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lPièce unique")
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lSection")
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lTous")
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .name("§3§lAnnulation")
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .name("§e§lAppel d'offres")
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§lAppel d'offres")
                .build();
        BID_1000 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .name("§e§lAppel d'offres")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EDIBLE_TITLE)
                .lore("§fIls sont tous comestibles")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_ITEM_TITLE)
                .lore("§fDivers items")
                .command("classify item")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BLOCK_TITLE)
                .lore("§fMatériaux de construction")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BURNABLE_TITLE)
                .lore("§fFlammes d'ours")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_INTERACTABLE_TITLE)
                .lore("§fCertains blocs sont interactifs")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EQUIP_TITLE)
                .lore("§fArmez - vous")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_KNOWLEDGE_TITLE)
                .lore("§fLa connaissance est le pouvoir")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.COMPASS.parseMaterial())
                .name("§3§lSearch")
                .command("search")
                .build();
        //</editor-fold>
        // 文本
        //<editor-fold desc="loadMenuText">
        PREPARE_TEXT = "§f[§8Pré - vente§f]";
        AUCTION_TEXT = "§f[§eVente aux enchères§f]";
        RETAIL_TEXT = "§f[§7De détail§f]";
        UNLIMITED_TEXT = "[§cInfini§f]";
        SELLER_TEXT = "§3§lVendeur§7: §f%s";
        BUYER_TEXT = "§3§lAcheteur§7: §f%s";
        AUCTIONER_TEXT = "§3§lPropriétaire§7: §f%s";
        START_PRICE_TEXT = "§3§lPrix de départ§7: §f§m%s%s";
        PRESENT_PRICE_TEXT = "§3§lPrix courants§7: §f%s%s";
        BID_TIME_TEXT = "§7The §3%s§7 Confirmation";
        ORIGINAL_PRICE_TEXT = "§3§lPrix d'origine§7: §f§m%s%s";
        PRICE_TEXT = "§3§lPrix§7: §f%s%s";

        MARKET_RECORD_TEXT = "§e§lTransactions";
        SALE_ITEM_TEXT = "§3§lArticles vendus§f: %s";
        REWARD_TEXT = "§3§lRécolte§f: %s%s";

        TOTAL_SALE_PRICE = "§3§lMontant total§7: §0%s";
        TOTAL_SALE_AMOUNT = "§3§lNombre§7: §0%s";
        TOTAL_SALE_LIST = "§3§lListe§7: ";

        CIRCULATE_PRICE = "§3§lTransaction§7: §0%s";
        CIRCULATE_AMOUNT = "§3§lNombre§7: §0%s";
        MAX_PRICE = "§3§lPrix maximum§7: §0%s";

        SALE_HEAT = "§3§lChaleur§7: §0%s";

        PLAYER_CREDIT = "§3§lCrédibilité§7: §0%s";
        PLAYER_BLACK = "§3§lListe noire§7: §0%s";
        PLAYER_SELLING = "§3§lNombre§7: §0%s";
        PLAYER_STATISTIC_AMOUNT = "§3§lStatistiques§7: §0%s";
        PLAYER_STATISTIC_MONEY = "§3§lTransaction§7: §0%s";
        //</editor-fold>
    }

    private static void loadGermanMenuButton() {
        // 加载物品
        //<editor-fold defaultstate="collapsed" desc="loadMenuButton">
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§lMein")
                .command("mine")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§lMarkt")
                .command(" ")
                .build();
        AUCTION = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§3§lAuktion")
                .command("auction")
                .build();
        COLLECT = new ViewItemBuilder(XMaterial.BUCKET.parseMaterial())
                .name("§3§lAkquisition")
                .command("collect")
                .build();
        RETAIL = new ViewItemBuilder(XMaterial.MAP.parseMaterial())
                .name("§3§lEinzelhandel")
                .command("retail")
                .build();
        TRADE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§lTauschhandel")
                .command("trade")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§lPersonenbezogene")
                .command("data")
                .build();
        SALE_DATA = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§3§lRohstoffdaten")
                .build();
        STATISTIC = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§lMarktstatistiken")
                .command("statistic")
                .build();
        CLASSIFY = new ViewItemBuilder(XMaterial.ENDER_EYE.parseMaterial())
                .name("§3§lKlassifizierung")
                .command("classify")
                .build();
        SIGN = new ViewItemBuilder(XMaterial.FEATHER.parseMaterial())
                .name("§3§lSign All")
                .command("sign")
                .build();
        DELETE = new ViewItemBuilder(XMaterial.LAVA_BUCKET.parseMaterial())
                .name("§3§lMülleimer")
                .command("delete")
                .build();
        SEND = new ViewItemBuilder(XMaterial.END_PORTAL_FRAME.parseMaterial())
                .name("§3§lPost")
                .command("send")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§lPostfach")
                .command("mail")
                .build();
        TRANSPORT = new ViewItemBuilder(XMaterial.CHEST_MINECART.parseMaterial())
                .name("§3§lLKW")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§lPoints")
                .command("point")
                .build();
        COIN = new ViewItemBuilder(XMaterial.SUNFLOWER.parseMaterial())
                .name("§3§lCoins")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§lAutoritativ")
                .command("admin")
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lSingleton")
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lTeil")
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§lGanz")
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .name("§3§lAbbrechen")
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .name("§e§lGebot")
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§lGebot")
                .build();
        BID_1000 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .name("§e§lGebot")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EDIBLE_TITLE)
                .lore("§fSie sind alle essbar")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_ITEM_TITLE)
                .lore("§fVerschiedene")
                .command("classify items")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BLOCK_TITLE)
                .lore("§fMaterial der Konstruktion")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BURNABLE_TITLE)
                .lore("§fRöhrende Flamme")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_INTERACTABLE_TITLE)
                .lore("§fEinige Block interaktierbar")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EQUIP_TITLE)
                .lore("§fBewaffne dich")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_KNOWLEDGE_TITLE)
                .lore("§fWissen ist Macht")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.COMPASS.parseMaterial())
                .name("§3§lSuche")
                .command("search")
                .build();
        //</editor-fold>
        // 文本
        //<editor-fold desc="loadMenuText">
        PREPARE_TEXT = "§f[§8Vorverkauf§f]";
        AUCTION_TEXT = "§f[§eAuktion§f]";
        RETAIL_TEXT = "§f[§7Einzelhandel§f]";
        UNLIMITED_TEXT = "[§cUnbegrenzt§f]";
        SELLER_TEXT = "§3§lVerkäufer§7: §f%s";
        BUYER_TEXT = "§3§lKäufer§7: §f%s";
        AUCTIONER_TEXT = "§3§lVersteigerer§7: §f%s";
        START_PRICE_TEXT = "§3§lAusgangspreis§7: §f§m%s%s";
        PRESENT_PRICE_TEXT = "§3§lGegenwärtiger Preis§7: §f%s%s";
        BID_TIME_TEXT = "§7The §3%s§7 Bestätigung";
        ORIGINAL_PRICE_TEXT = "§3§lOriginalpreis§7: §f§m%s%s";
        PRICE_TEXT = "§3§lPreis§7: §f%s%s";

        MARKET_RECORD_TEXT = "§e§lTransaktionen";
        SALE_ITEM_TEXT = "§3§lHandelsgegenstände§f: %s";
        REWARD_TEXT = "§3§lBelohnung§f: %s%s";

        TOTAL_SALE_PRICE = "§3§lGesamtbetrag§7: §0%s";
        TOTAL_SALE_AMOUNT = "§3§lInsgesamt§7: §0%s";
        TOTAL_SALE_LIST = "§3§lListe§7: ";

        CIRCULATE_PRICE = "§3§lGesamtbetrag§7: §0%s";
        CIRCULATE_AMOUNT = "§3§lInsgesamt§7: §0%s";
        MAX_PRICE = "§3§lHöchstbetrag§7: §0%s";

        SALE_HEAT = "§3§lWärmegrad§7: §0%s";

        PLAYER_CREDIT = "§3§lKredit§7: §0%s";
        PLAYER_BLACK = "§3§lSchwarze Liste§7: §0%s";
        PLAYER_SELLING = "§3§lIm Verkauf§7: §0%s";
        PLAYER_STATISTIC_AMOUNT = "§3§lStatistische Menge§7: §0%s";
        PLAYER_STATISTIC_MONEY = "§3§lGesamtbetrag§7: §0%s";
        //</editor-fold>
    }

    private static void loadChineseMenuButton() {
        // 加载物品
        //<editor-fold defaultstate="collapsed" desc="loadMenuButton">
        MINE = new ViewItemBuilder(XMaterial.ENDER_CHEST.parseMaterial())
                .name("§3§l我的")
                .command("mine")
                .build();
        MARKET = new ViewItemBuilder(XMaterial.CHEST.parseMaterial())
                .name("§3§l市场")
                .command(" ")
                .build();
        AUCTION = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§3§l拍卖")
                .command("auction")
                .build();
        COLLECT = new ViewItemBuilder(XMaterial.BUCKET.parseMaterial())
                .name("§3§l收购")
                .command("collect")
                .build();
        RETAIL = new ViewItemBuilder(XMaterial.MAP.parseMaterial())
                .name("§3§l零售")
                .command("retail")
                .build();
        TRADE = new ViewItemBuilder(XMaterial.EMERALD.parseMaterial())
                .name("§3§l以物易物")
                .command("trade")
                .build();
        DATA = new ViewItemBuilder(XMaterial.BOOK.parseMaterial())
                .name("§3§l个人数据")
                .command("data")
                .build();
        SALE_DATA = new ViewItemBuilder(XMaterial.KNOWLEDGE_BOOK.parseMaterial())
                .name("§3§l商品数据")
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
        DELETE = new ViewItemBuilder(XMaterial.LAVA_BUCKET.parseMaterial())
                .name("§3§l销毁")
                .command("delete")
                .build();
        SEND = new ViewItemBuilder(XMaterial.END_PORTAL_FRAME.parseMaterial())
                .name("§3§l邮寄")
                .command("send")
                .build();
        MAIL = new ViewItemBuilder(XMaterial.GRINDSTONE.parseMaterial())
                .name("§3§l邮箱")
                .command("mail")
                .build();
        TRANSPORT = new ViewItemBuilder(XMaterial.CHEST_MINECART.parseMaterial())
                .name("§3§l运输车")
                .build();
        POINT = new ViewItemBuilder(XMaterial.DIAMOND.parseMaterial())
                .name("§3§l点券")
                .command("point")
                .build();
        COIN = new ViewItemBuilder(XMaterial.SUNFLOWER.parseMaterial())
                .name("§3§l金币")
                .build();
        ADMIN = new ViewItemBuilder(XMaterial.ITEM_FRAME.parseMaterial())
                .name("§3§l官方")
                .command("admin")
                .build();
        BUY_1 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§l单件")
                .build();
        BUY_8 = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§l部分")
                .build();
        BUY_ALL = new ViewItemBuilder(XMaterial.PRISMARINE_CRYSTALS.parseMaterial())
                .name("§e§l一口价")
                .build();
        CANCEL = new ViewItemBuilder(XMaterial.HOPPER.parseMaterial())
                .name("§3§l下架")
                .build();
        BID_10 = new ViewItemBuilder(XMaterial.GOLD_NUGGET.parseMaterial())
                .name("§e§l叫价")
                .build();
        BID_100 = new ViewItemBuilder(XMaterial.GOLD_INGOT.parseMaterial())
                .name("§e§l叫价")
                .build();
        BID_1000 = new ViewItemBuilder(XMaterial.GOLD_BLOCK.parseMaterial())
                .name("§e§l叫价")
                .build();

        EDIBLE  = new ViewItemBuilder(XMaterial.CARROT.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EDIBLE_TITLE)
                .lore("§f吃货专区")
                .command("classify edible")
                .build();
        ITEM  = new ViewItemBuilder(XMaterial.STICK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_ITEM_TITLE)
                .lore("§f杂七杂八的东西")
                .command("classify item")
                .build();
        BLOCK  = new ViewItemBuilder(XMaterial.QUARTZ_BLOCK.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BLOCK_TITLE)
                .lore("§f搭积木")
                .command("classify block")
                .build();
        BURNABLE  = new ViewItemBuilder(XMaterial.COAL.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_BURNABLE_TITLE)
                .lore("§f燃烧吧，烈焰")
                .command("classify burnable")
                .build();
        INTERACTABLE  = new ViewItemBuilder(XMaterial.FURNACE.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_INTERACTABLE_TITLE)
                .lore("§f似乎是魔法在运作")
                .command("classify interactable")
                .build();
        EQUIP  = new ViewItemBuilder(XMaterial.GOLDEN_SWORD.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_EQUIP_TITLE)
                .lore("§f要生存只能战斗")
                .command("classify equip")
                .build();
        KNOWLEDGE  = new ViewItemBuilder(XMaterial.BOOKSHELF.parseMaterial())
                .name("§3§l" + Language.CLASSIFY_KNOWLEDGE_TITLE)
                .lore("§f知识的力量")
                .command("classify knowledge")
                .build();
        SEARCH  = new ViewItemBuilder(XMaterial.COMPASS.parseMaterial())
                .name("§3§l搜索")
                .command("search")
                .build();
        //</editor-fold>
        // 文本
        //<editor-fold desc="loadMenuText">
        PREPARE_TEXT = "§f[§8预售§f]";
        AUCTION_TEXT = "§f[§e拍卖§f]";
        RETAIL_TEXT = "§f[§7零售§f]";
        UNLIMITED_TEXT = "[§c无限§f]";
        SELLER_TEXT = "§3§l卖家§7: §f%s";
        BUYER_TEXT = "§3§l买家§7: §f%s";
        AUCTIONER_TEXT = "§3§l拍卖人§7: §f%s";
        START_PRICE_TEXT = "§3§l起拍价§7: §f§m%s%s";
        PRESENT_PRICE_TEXT = "§3§l现价§7: §f%s%s";
        BID_TIME_TEXT = "§7第§3%s§7次确认价格";
        ORIGINAL_PRICE_TEXT = "§3§l原价§7: §f§m%s%s";
        PRICE_TEXT = "§3§l价格§7: §f%s%s";

        MARKET_RECORD_TEXT = "§e§l交易记录";
        SALE_ITEM_TEXT = "§3§l交易物品§f: %s";
        REWARD_TEXT = "§3§l收获§f: %s%s";

        TOTAL_SALE_PRICE = "§3§l商品总金额§7: §0%s";
        TOTAL_SALE_AMOUNT = "§3§l商品数量§7: §0%s";
        TOTAL_SALE_LIST = "§3§l商品编号§7: ";

        CIRCULATE_PRICE = "§3§l交易金额§7: §0%s";
        CIRCULATE_AMOUNT = "§3§l交易数量§7: §0%s";
        MAX_PRICE = "§3§l最高交易金额§7: §0%s";

        SALE_HEAT = "§3§l热度§7: §0%s";

        PLAYER_CREDIT = "§3§l信誉度§7: §0%s";
        PLAYER_BLACK = "§3§l黑名单§7: §0%s";
        PLAYER_SELLING = "§3§l在售§7: §0%s";
        PLAYER_STATISTIC_AMOUNT = "§3§l累计交易数量§7: §0%s";
        PLAYER_STATISTIC_MONEY = "§3§l累计交易金额§7: §0%s";
        //</editor-fold>
    }

    public static void loreMailItem(ItemStack item, Mail mail){
        if (!"".equals(mail.getInfo())) ItemUtils.addLore(item, "§7from " + mail.getInfo());
        ItemUtils.addLore(item, "");
        ItemUtils.addLore(item, "§e§m·                         ·");
        ItemUtils.addLore(item, "§f" + TimeUtils.getTime(mail.getAppear()));
        ItemUtils.setItemValue(item, "sign " + mail.getId());
    }

    public static void loreSaleItem(ItemStack item, Sale sale){
        loreSale(item, sale);
        ItemUtils.setItemValue(item, "affair " + sale.getId());
    }

    public static void loreSaleEditItem(ItemStack item, Sale sale){
        loreSale(item, sale);
        ItemUtils.setItemValue(item, "edit " + sale.getId());
    }

    public static void loreSale(ItemStack item, Sale sale){
        ItemUtils.addLore(item, "");

//        if (!"null".equals(sale.getDesc())) ItemUtils.addLore(item, "§f" + sale.getDesc());
        ItemUtils.addLore(item, "§e§m·                         ·");

        String symbol = (sale.isPoint() ? Language.POINT_SYMBOL : Language.COIN_SYMBOL);

        if (sale.getPrice() == -1){ // 预售物品
            ItemUtils.addLore(item, PREPARE_TEXT);
            ItemUtils.addLore(item, String.format(SELLER_TEXT, sale.getOwner()));

        }else if (sale.isAuction()){ // 拍卖物品
            ItemUtils.addLore(item, AUCTION_TEXT);
            ItemUtils.addLore(item, String.format(AUCTIONER_TEXT, sale.getOwner()));
            if(sale.getPrice() != sale.getCost()){
                ItemUtils.addLore(item, String.format(START_PRICE_TEXT, sale.getPrice(), symbol));
                ItemUtils.addLore(item, String.format(PRESENT_PRICE_TEXT, sale.getCost(), symbol));
                ItemUtils.addLore(item, String.format(BID_TIME_TEXT, 3 - sale.getHeat()));
            }else {
                ItemUtils.addLore(item, String.format(START_PRICE_TEXT, sale.getPrice(), symbol));
            }

        }else { // 普通物品
            ItemUtils.addLore(item, RETAIL_TEXT
                    + (sale.isAdmin() ? UNLIMITED_TEXT : ""));
            if (! sale.isAdmin()) ItemUtils.addLore(item, String.format(SELLER_TEXT, sale.getOwner()));
            if(sale.getPrice() != sale.getCost()){
                ItemUtils.addLore(item, String.format(ORIGINAL_PRICE_TEXT, sale.getPrice(), symbol));
                ItemUtils.addLore(item, String.format(PRESENT_PRICE_TEXT, sale.getCost(), symbol));
            }else {
                ItemUtils.addLore(item, String.format(PRICE_TEXT, sale.getPrice(), symbol));
            }
        }
    }

    @NotNull
    public static ItemStack getRecordItem(String itemName, String buyer, double cost, boolean point){
        ItemStack mail = new ViewItemBuilder(XMaterial.WRITTEN_BOOK.parseMaterial())
                .name(MARKET_RECORD_TEXT)
                .lore(String.format(SALE_ITEM_TEXT, itemName))
                .lore(String.format(BUYER_TEXT, buyer))
                .lore(String.format(REWARD_TEXT,  cost, (point ? Language.POINT_SYMBOL : Language.COIN_SYMBOL)))
                .build();
        if (Config.MARKET_RECORD) {
            BookMeta meta = ((BookMeta) mail.getItemMeta());
            if (meta != null) {
                ComponentBuilder componentBuilder = new ComponentBuilder(Language.PLUGIN_NAME)
                        .append("\n")
                        .append("------------------\n")
                        .append(String.format(SALE_ITEM_TEXT.replace("§f", "§0"), itemName)).append("\n")
                        .append(String.format(BUYER_TEXT.replace("§f", "§0"), buyer)).append("\n")
                        .append(String.format(REWARD_TEXT.replace("§f", "§0"), cost, (point ? Language.POINT_SYMBOL : Language.COIN_SYMBOL))).append("\n")
                        .append("\n")
                        .append(TimeUtils.getTimeToday());
                meta.spigot().addPage(componentBuilder.create());
                meta.setTitle(Language.PLUGIN_NAME);
                meta.setAuthor(Language.PLUGIN_NAME);
            }
            mail.setItemMeta(meta);
        }
        return mail;
    }

}
