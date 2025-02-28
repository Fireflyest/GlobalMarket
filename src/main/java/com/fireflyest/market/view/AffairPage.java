package com.fireflyest.market.view;

import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import com.fireflyest.market.bean.Addend;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.google.gson.Gson;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.inventory.item.SkullItemBuilder;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.util.TimeUtils;
import io.fireflyest.emberlib.util.YamlUtils;

/**
 * 交易页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class AffairPage extends Page {

    private final MarketService service;

    private static final String AMOUNT = "amount";
    private static final int DAY = 1000 * 60 * 60 * 24;

    protected AffairPage(String target, int pageNumber, MarketService service) {
        super(target, pageNumber, 27);
        this.service = service;

        String nickname = service.selectTransactionNickname(NumberConversions.toInt(target));
        if (StringUtils.isEmpty(nickname)) {
            nickname = Language.ERROR_DATA.get();
        }
        this.setup(Language.TITLE_AFFAIR.get().replace("%page%", String.valueOf(pageNumber))
            .replace("%item%", nickname));
    }

    @Override
    public void refreshPage() {
        if (!init) {
            this.initPage();
        }

        this.putItems();
    }

    @Override
    public void initPage() {
        super.initPage();
        final ItemStack blank = MarketItem.getPair("blank").first().build();
        this.slot(0, blank);
        this.slot(1, blank);
        this.slot(2, blank);
        this.slot(9, blank);
        this.slot(11, blank);
        this.slot(18, blank);
        this.slot(19, blank);
        this.slot(20, blank);

        this.slot(17, MarketItem.getPair("transaction"));
        this.slot(26, MarketItem.getPair("back"));
    }

    private void putTransaction(Transaction ta, int amount, String symbol, boolean partial) {
        final String command;
        final Pair<ItemBuilder, Slot> data = MarketItem.getPair("transaction");
        switch (ta.getType()) {
            case "retail":
            case "adminretail":
                command = "market buy " + target;
                if (partial) {
                    final ItemBuilder buy1 = MarketItem.getItemBuilder("buy1");
                    final Slot buy1Slot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " 1");
                    final ItemBuilder buy2 = MarketItem.getItemBuilder("buy8");
                    final Slot buy2Slot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " 8");
                    final ItemBuilder buy3 = MarketItem.getItemBuilder("buy");
                    final Slot buy3Slot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " " + amount);
                    
                    buy1.lore(String.format(
                        Language.GUI_PRICE_NORMAL.get(), ta.getCost() / amount, symbol), 0);
                    buy2.lore(String.format(
                        Language.GUI_PRICE_NORMAL.get(), ta.getCost() / amount * 8, symbol), 0);
                    buy3.amount(amount).replace(AMOUNT, amount).lore(String.format(
                        Language.GUI_PRICE_NORMAL.get(), ta.getCost(), symbol), 0);
                    this.slot(13, buy1.build(), buy1Slot);
                    if (amount > 8) {
                        this.slot(14, buy2.build(), buy2Slot);
                    }
                    this.slot(15, buy3.build(), buy3Slot);
                } else {
                    final ItemBuilder buy = MarketItem.getItemBuilder("buy");
                    final Slot buySlot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " 0");
                    buy.amount(amount).replace(AMOUNT, amount).lore(
                        String.format(Language.GUI_PRICE_NORMAL.get(), ta.getCost(), symbol), 0);
                    this.slot(14, buy.build(), buySlot);
                }
                data.first().lore(String.format(Language.GUI_HEAT.get(), ta.getHeat()), 0);

                if (Config.TRANSACTION_EXPIRATION.get() != -1) {
                    final long time = ta.getAppear() + Config.TRANSACTION_EXPIRATION.get() * DAY 
                        - TimeUtils.getTime();
                    data.first().lore(String.format(
                        Language.GUI_DEADLINE.get(), 
                        TimeUtils.howLong(time)), 1);
                }
                break;
            case "order":
            case "adminorder":
                command = "market sale " + target;
                if (partial) {
                    final ItemBuilder sale1 = MarketItem.getItemBuilder("buy1");
                    final Slot sale1Slot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " 1");
                    final ItemBuilder sale2 = MarketItem.getItemBuilder("buy8");
                    final Slot sale2Slot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " 8");
                    final ItemBuilder sale3 = MarketItem.getItemBuilder("buy");
                    final Slot sale3Slot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " " + amount);

                    sale1.lore(String.format(
                        Language.GUI_PRICE_ORDER.get(), ta.getCost() / amount, symbol), 0);
                    sale2.lore(String.format(
                        Language.GUI_PRICE_ORDER.get(), ta.getCost() / amount * 8, symbol), 0);
                    sale3.amount(amount).replace(AMOUNT, amount).lore(String.format(
                        Language.GUI_PRICE_ORDER.get(), ta.getCost(), symbol), 0);
                    this.slot(13, sale1.build(), sale1Slot);
                    if (amount > 8) {
                        this.slot(14, sale2.build(), sale2Slot);
                    }
                    this.slot(15, sale3.build(), sale3Slot);
                } else {
                    final ItemBuilder sale = MarketItem.getItemBuilder("buy");
                    final Slot saleSlot = new Slot().button(
                        ActionResult.ACTION_PLAYER_COMMAND, 
                        command + " 0");
                    sale.amount(amount).replace(AMOUNT, amount).lore(
                        String.format(Language.GUI_PRICE_ORDER.get(), ta.getCost(), symbol), 0);
                    this.slot(14, sale.build(), saleSlot);
                }
                data.first().lore(String.format(Language.GUI_HEAT.get(), ta.getHeat()), 0);
                if (Config.TRANSACTION_EXPIRATION.get() != -1) {
                    final long time = ta.getAppear() + Config.TRANSACTION_EXPIRATION.get() * DAY 
                        - TimeUtils.getTime();
                    data.first().lore(String.format(
                        Language.GUI_DEADLINE.get(), 
                        time), 1);
                }
                break;
            case "auction":
                command = "market bid " + target;
                final ItemBuilder bid1 = MarketItem.getItemBuilder("bid10");
                final Slot bid1Slot = new Slot().button(
                    ActionResult.ACTION_PLAYER_COMMAND, 
                    command + " 10");
                final ItemBuilder bid2 = MarketItem.getItemBuilder("bid100");
                final Slot bid2Slot = new Slot().button(
                    ActionResult.ACTION_PLAYER_COMMAND, 
                    command + " 100");
                final ItemBuilder bid3 = MarketItem.getItemBuilder("bid1000");
                final Slot bid3Slot = new Slot().button(
                    ActionResult.ACTION_PLAYER_COMMAND, 
                    command + " 1000");

                bid1.lore(String.format(Language.GUI_PRICE_PRESENT.get(), ta.getCost(), symbol), 0);
                bid2.lore(String.format(Language.GUI_PRICE_PRESENT.get(), ta.getCost(), symbol), 0);
                bid3.lore(String.format(Language.GUI_PRICE_PRESENT.get(), ta.getCost(), symbol), 0);

                this.slot(13, bid1.build(), bid1Slot);
                this.slot(14, bid2.build(), bid2Slot);
                this.slot(15, bid3.build(), bid3Slot);

                final Addend info = new Gson().fromJson(ta.getDesc(), Addend.class);

                data.first().lore(String.format(Language.GUI_HEAT.get(), 3 - ta.getHeat()), 0);
                data.first().lore(String.format(Language.GUI_BIDERS.get(), info.getStrings()), 1);
                break;
            case "prepare":
            default:
                this.slot(14, MarketItem.getPair("wait"));
                break;
        }
        this.slot(17, data);
    }

    private void putItems() {
        final Transaction ta = 
            service.selectTransactionById(NumberConversions.toInt(target));
        // 交易不存在
        if (ta == null) {
            this.slot(13, new ItemStack(Material.AIR));
            this.slot(14, MarketItem.getPair("wait"));
            this.slot(15, new ItemStack(Material.AIR));
            return;
        }

        // 交易物品展示
        final ItemStack item = YamlUtils.deserializeItemStack(ta.getStack());
        this.slot(10, item);

        // 商品归属者信息
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(ta.getOwner()));
        final ItemBuilder visit = MarketItem.getItemBuilder("visit");
        ((SkullItemBuilder) visit).setPlayer(offlinePlayer)
            .name("§3§l" + offlinePlayer.getName());
        final Slot visitSlot = new Slot().button(ActionResult.ACTION_PAGE_OPEN, 
            "market.visit." + offlinePlayer.getUniqueId());
        this.slot(8, visit.build(), visitSlot);

        final int amount = item.getAmount();
        String symbol = "";
        boolean partial = Config.TRANSACTION_PARTIAL.get().booleanValue(); 
        switch (ta.getCurrency()) {
            case "coin":
                symbol = Language.SYMBOL_COIN.get();
                break;
            case "point":
                partial = false;
                symbol = Language.SYMBOL_POINT.get();
                break;
            case "item":
                partial = false;
                if (!"".equals(ta.getExtra())) {
                    final ItemStack currencyItem = 
                        YamlUtils.deserializeItemStack(ta.getExtra());
                    this.slot(5, currencyItem);
                }
                break;
            default:
                break;
        }
        this.putTransaction(ta, amount, symbol, partial);
    }
    
}
