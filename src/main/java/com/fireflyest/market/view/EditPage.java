package com.fireflyest.market.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.task.TaskHandler;
import io.fireflyest.emberlib.util.ItemUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskAffirm;
import com.fireflyest.market.task.TaskCategory;
import com.fireflyest.market.task.TaskReprice;
import com.google.common.base.Objects;

/**
 * 编辑页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class EditPage extends Page {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    private Transaction edition;
    private String num = "";
    private boolean prepare;
    private boolean zeroPrice;
    private boolean dot;
    private int typeNum = 0;
    private int currencyNum = 1;

    private static final String RETAIL = "retail";
    private static final String ADMIN_RETAIL = "adminretail";
    private static final String ORDER = "order";
    private static final String ADMIN_ORDER = "adminorder";
    private static final String AUCTION = "auction";
    private static final String COIN = "coin";
    private static final String POINT = "point";
    private static final String ITEM = "item";
    private List<String> types = new ArrayList<>();
    private List<String> currencies = new ArrayList<>();
    private Map<Integer, String> numMap = new HashMap<>();
    private Map<Integer, Integer> categoryNumMap = new HashMap<>();
    private List<ItemBuilder> categories = new ArrayList<>(7);
    private Pair<ItemBuilder, Slot> keyDot;
    private Pair<ItemBuilder, Slot> keyDelete;
    private Pair<ItemBuilder, Slot> key0;
    private Pair<ItemBuilder, Slot> key1;
    private Pair<ItemBuilder, Slot> key2;
    private Pair<ItemBuilder, Slot> key3;
    private Pair<ItemBuilder, Slot> key4;
    private Pair<ItemBuilder, Slot> key5;
    private Pair<ItemBuilder, Slot> key6;
    private Pair<ItemBuilder, Slot> key7;
    private Pair<ItemBuilder, Slot> key8;
    private Pair<ItemBuilder, Slot> key9;

    protected EditPage(String target, int pageNumber, 
            MarketService service, ViewGuide guide, TaskHandler handler) {
        super(target, pageNumber, 54);
        this.service = service;
        this.guide = guide;
        this.handler = handler;

        // 分类按钮
        categories.add(MarketItem.getItemBuilder("category1"));
        categories.add(MarketItem.getItemBuilder("category2"));
        categories.add(MarketItem.getItemBuilder("category3"));
        categories.add(MarketItem.getItemBuilder("category4"));
        categories.add(MarketItem.getItemBuilder("category5"));
        categories.add(MarketItem.getItemBuilder("category6"));
        categories.add(MarketItem.getItemBuilder("category7"));
        // 对应位置对应的键盘数字
        numMap.put(4, "7");
        numMap.put(5, "8");
        numMap.put(6, "9");
        numMap.put(13, "4");
        numMap.put(14, "5");
        numMap.put(15, "6");
        numMap.put(22, "1");
        numMap.put(23, "2");
        numMap.put(24, "3");
        numMap.put(32, "0");
        // 对应位置对应的分类
        for (int i = 1; i <= 7; i++) {
            categoryNumMap.put(44 + i, (int) Math.pow(2, i));
        }

        types.add(RETAIL);
        types.add(ORDER);
        types.add(AUCTION);

        currencies.add(COIN);
        if (Config.CURRENCY_POINT.get().booleanValue()) {
            currencies.add(POINT);
        }
        currencies.add(ITEM);

        String nickname = service.selectTransactionNickname(NumberConversions.toInt(target));
        if (StringUtils.isEmpty(nickname)) {
            nickname = Language.ERROR_DATA.get();
        }
        this.setup(Language.TITLE_EDIT.get().replace("%page%", String.valueOf(pageNumber))
            .replace("%item%", nickname));
    }

    @Override
    public void refreshPage() {
        if (!init) {
            this.initPage();
        }

        this.putTransaction();
    }

    @Override
    public void initPage() {
        super.initPage();
        final Pair<ItemBuilder, Slot> blank = MarketItem.getPair("blank");
        this.slot(0, blank);
        this.slot(1, blank);
        this.slot(2, blank);
        this.slot(9, blank);
        this.slot(11, blank);
        this.slot(18, blank);
        this.slot(19, blank);
        this.slot(20, blank);
        this.slot(27, blank);
        this.slot(29, blank);
        for (int i = 36; i < 45; i++) {
            this.slot(i, blank);
        }
        this.slot(53, MarketItem.getPair("back"));
    }

    @Override
    public ActionResult action(int index, InventoryAction inventoryAction, 
            Player player, ItemStack currentItem, ItemStack cursor) {
        
        final ActionResult action = 
            super.action(index, inventoryAction, player, currentItem, cursor);
        int categoryNum = 0;
        boolean editCategory = false;
        switch (index) {
            case 4:
            case 5:
            case 6:
            case 13:
            case 14:
            case 15:
            case 22:
            case 23:
            case 24:
            case 32:
                num += numMap.get(index);
                action.addAction(ActionResult.ACTION_REFRESH);
                break;
            case 31:
                if (!num.contains(".")) {
                    num += ".";
                }
                action.addAction(ActionResult.ACTION_REFRESH);
                break;
            case 8:
                if (prepare) {
                    // currency 轮换
                    edition.setCurrency(currencies.get(currencyNum++));
                    if (currencyNum >= currencies.size()) {
                        currencyNum = 0;
                    }
                    if (edition.getCurrency().equals(POINT) && num.contains(".")) {
                        num = num.substring(0, num.indexOf("."));
                    }
                }
                action.addAction(ActionResult.ACTION_REFRESH);
                break;
            case 17:
                // type 轮换
                if (prepare) {
                    edition.setType(types.get(typeNum++));
                    if (typeNum >= types.size()) {
                        typeNum = 0;
                    }
                }
                action.addAction(ActionResult.ACTION_REFRESH);
                break;
            case 33:
                if (!num.isEmpty()) {
                    num = num.substring(0, num.length() - 1);
                }
                action.addAction(ActionResult.ACTION_REFRESH);
                break;
            case 35:
                final UUID owner = UUID.fromString(edition.getOwner());
                // 修改价格
                handler.putTasks(
                    GlobalMarket.TASK_MARKET, 
                    new TaskReprice(owner, service, guide, edition.getId(), edition.getCost()));
                // 确认发售
                if (prepare) {
                    handler.putTasks(GlobalMarket.TASK_MARKET, new TaskAffirm(
                        owner, 
                        service, 
                        guide, 
                        edition.getId(), 
                        edition.getType(), 
                        edition.getCurrency(), 
                        edition.getExtra()));
                }
                break;
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
                categoryNum = categoryNumMap.get(index);
                edition.setCategory(this.switchCategory(edition.getCategory(), categoryNum));
                editCategory = true;
                final ItemBuilder categoryNone = MarketItem.getItemBuilder("categoryNone");
                final ItemBuilder categoryItem = categories.get(categoryNum);
                if ((edition.getCategory() & categoryNum) == 0) {
                    final String category = categoryItem.getName();
                    categoryNone.name(category);
                    this.slot(index, categoryNone.build());
                } else {
                    this.slot(index, categoryItem.build());
                }
                break;
            default:
        }
        if (zeroPrice) {
            edition.setPrice(NumberConversions.toDouble(num));
        }
        if (editCategory) {
            handler.putTasks(GlobalMarket.TASK_MARKET, new TaskCategory(
                    UUID.fromString(edition.getOwner()), 
                    service, 
                    guide, 
                    edition.getId(), 
                    edition.getCategory(), 
                    categoryNum));
        }
        edition.setCost(NumberConversions.toDouble(num));
        return action;
    }

    public long switchCategory(long category, long num) {
        return category ^ num;
    }

    private void putTransaction() {
        final Transaction transaction = 
            service.selectTransactionById(NumberConversions.toInt(target));
        // 交易不存在
        if (transaction == null) {
            this.clean();
            this.slot(53, MarketItem.getPair("back"));
            return;
        }

        // 复制一份作为修改版
        if (edition == null) {
            edition = transaction.duplicate();
            if (Bukkit.getOfflinePlayer(UUID.fromString(transaction.getOwner())).isOp()) {
                types.add(ADMIN_RETAIL);
                types.add(ADMIN_ORDER);
            }
            keyDot = MarketItem.getPair("dot");
            keyDelete = MarketItem.getPair("delete");
            key0 = MarketItem.getPair("key0");
            key1 = MarketItem.getPair("key1");
            key2 = MarketItem.getPair("key2");
            key3 = MarketItem.getPair("key3");
            key4 = MarketItem.getPair("key4");
            key5 = MarketItem.getPair("key5");
            key6 = MarketItem.getPair("key6");
            key7 = MarketItem.getPair("key7");
            key8 = MarketItem.getPair("key8");
            key9 = MarketItem.getPair("key9");
        }

        zeroPrice = transaction.getPrice() == 0;

        if (!Objects.equal(edition.getExtra(), transaction.getExtra())) {
            edition.setCurrency("item");
            edition.setExtra(transaction.getExtra());
        }
        switch (transaction.getType()) {
            case RETAIL:
            case ADMIN_RETAIL:
            case ORDER:
            case ADMIN_ORDER:
                this.slot(35, MarketItem.getPair("edit"));
                prepare = false;
                break;
            case AUCTION:
                if (transaction.getPrice() == transaction.getCost()) {
                    this.slot(35, MarketItem.getPair("edit"));
                } else {
                    this.slot(35, MarketItem.getPair("edit_disable"));
                }
                prepare = false;
                break;
            case "prepare":
            default:
                this.slot(35, MarketItem.getPair("done"));
                prepare = true;
                break;
        }

        ItemBuilder currencyItemBuilder = null;
        switch (edition.getCurrency()) {
            case COIN:
                dot = true;
                currencyItemBuilder = MarketItem.getItemBuilder(edition.getCurrency());
                this.slot(8, currencyItemBuilder.build());
                break;
            case POINT:
                currencyItemBuilder = MarketItem.getItemBuilder(edition.getCurrency());
                this.slot(8, currencyItemBuilder.build());
                break;
            case ITEM:
                final Pair<ItemBuilder, Slot> currencySlot = 
                    MarketItem.getPair(edition.getCurrency());
                if (edition.getExtra() != null && !"".equals(edition.getExtra())) {
                    final ItemStack currencyItem = 
                        YamlUtils.deserializeItemStack(edition.getExtra());
                    ItemUtils.addLore(currencyItem, Language.GUI_CURRENCY.get());
                    this.slot(8, currencyItem, currencySlot.second());
                } else {
                    this.slot(8, currencySlot);
                }
                break;
            default:
                break;
        }


        switch (edition.getType()) {
            case RETAIL:
            case ADMIN_RETAIL:
            case ORDER:
            case ADMIN_ORDER:
                this.placeKeyboard();
                this.slot(17, MarketItem.getItemBuilder(edition.getType()).build());
                break;
            case AUCTION:
                if (transaction.getPrice() == transaction.getCost()) {
                    this.placeKeyboard();
                }
                this.slot(17, MarketItem.getItemBuilder(edition.getType()).build());
                break;
            case "prepare":
            default:
                this.slot(17, MarketItem.getItemBuilder("wait").build());
                break;
        }

        // 下架按钮
        final ItemBuilder cancelItem = MarketItem.getItemBuilder("cancel");
        final Slot cancelSlot = new Slot().result(
            InventoryAction.PICKUP_ALL, 
            false, 
            ActionResult.ACTION_PLAYER_COMMAND, 
            "market cancel " + target);
        this.slot(26, cancelItem.build(), cancelSlot);

        // 分类
        this.putCategorys();

        // 交易物品展示
        final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());
        final ItemStack editItem = YamlUtils.deserializeItemStack(edition.getStack());
        MarketItem.loreItemData(item, transaction);
        MarketItem.loreItemData(editItem, edition);
        this.slot(10, item);
        this.slot(28, editItem);

    }

    private void putCategorys() {
        final ItemBuilder categoryNone = MarketItem.getItemBuilder("categoryNone");
        for (int i = 0, j = 2; i < 7; i++, j *= 2) {
            final ItemBuilder categoryItem = categories.get(i);
            if ((edition.getCategory() & j) == 0) {
                final String category = categoryItem.getName();
                categoryNone.name(category);
                this.slot(45 + i, categoryNone.build());
            } else {
                this.slot(45 + i, categoryItem.build());
            }
        }
    }

    /**
     * 放置价格修改键盘
     * @param edition 商品副本
     */
    private void placeKeyboard() {
        // 小数点
        if (dot) {
            if (num.endsWith(".")) {
                keyDot.first().lore("§7" + num + "§70", 0);
            } else if (!num.contains(".")) {
                keyDot.first().lore("§7" + num + "§9.§70", 0);
            } else {
                keyDot.first().lore("§7" + num, 0);
            }
            this.slot(31, keyDot);
        }

        // 删除键
        if (!StringUtils.isEmpty(num)) {
            final StringBuilder insert = new StringBuilder(num).insert(num.length() - 1, "§c");
            if (num.endsWith(".")) {
                keyDelete.first()
                    .lore("§7" + insert, 0);
            } else if (!num.contains(".")) {
                keyDelete.first()
                    .lore("§7" + insert + "§9.§70", 0);
            } else {
                keyDelete.first()
                    .lore("§7" + insert, 0);
            }
        }
        this.slot(33, keyDelete);

        // 数字
        final String end = num.contains(".") ? "" : "§7.0";
        key7.first().lore("§7" + num + "§8§n7" + end, 0);
        key8.first().lore("§7" + num + "§8§n8" + end, 0);
        key9.first().lore("§7" + num + "§8§n9" + end, 0);
        key4.first().lore("§7" + num + "§8§n4" + end, 0);
        key5.first().lore("§7" + num + "§8§n5" + end, 0);
        key6.first().lore("§7" + num + "§8§n6" + end, 0);
        key1.first().lore("§7" + num + "§8§n1" + end, 0);
        key2.first().lore("§7" + num + "§8§n2" + end, 0);
        key3.first().lore("§7" + num + "§8§n3" + end, 0);
        key0.first().lore("§7" + num + "§8§n0" + end, 0);
        this.slot(4, key7);
        this.slot(5, key8);
        this.slot(6, key9);
        this.slot(13, key4);
        this.slot(14, key5);
        this.slot(15, key6);
        this.slot(22, key1);
        this.slot(23, key2);
        this.slot(24, key3);
        this.slot(32, key0);
    }
    
}
