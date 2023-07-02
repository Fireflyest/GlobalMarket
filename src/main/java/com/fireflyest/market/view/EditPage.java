package com.fireflyest.market.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.craftitem.builder.ItemBuilder;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskAffirm;
import com.fireflyest.market.task.TaskReprice;

public class EditPage extends TemplatePage {

    private final MarketService service;
    private final MarketYaml yaml;
    private final ViewGuide guide;
    private final TaskHandler handler;

    private Transaction edition;
    private String num = "";
    private boolean prepare;
    private boolean zeroPrice;
    private boolean dot;
    private int typeNum = 0;

    private List<String> types = new ArrayList<>();
    private ItemBuilder[] categorys;
    private ItemStack keyDot;
    private ItemStack keyDelete;
    private ItemStack key0;
    private ItemStack key1;
    private ItemStack key2;
    private ItemStack key3;
    private ItemStack key4;
    private ItemStack key5;
    private ItemStack key6;
    private ItemStack key7;
    private ItemStack key8;
    private ItemStack key9;

    protected EditPage(String title, String target, int page, MarketService service, MarketYaml yaml, ViewGuide guide, TaskHandler handler) {
        super(title, target, page, 54);
        this.service = service;
        this.yaml = yaml;
        this.guide = guide;
        this.handler = handler;

        // 分类按钮
        categorys = new ItemBuilder[] {
            yaml.getItemBuilder("category1"),
            yaml.getItemBuilder("category2"),
            yaml.getItemBuilder("category3"),
            yaml.getItemBuilder("category4"),
            yaml.getItemBuilder("category5"),
            yaml.getItemBuilder("category6"),
            yaml.getItemBuilder("category7")
        };
        types.add("retail");
        types.add("order");
        types.add("auction");

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Transaction transaction = service.selectTransactionById(NumberConversions.toInt(target));
        // 交易不存在
        if (transaction == null) {
            return asyncButtonMap;
        }

        // 复制一份作为修改版
        if (edition == null) {
            edition = transaction.duplicate();
            if (Bukkit.getOfflinePlayer(UUID.fromString(transaction.getOwner())).isOp()) {
                types.add("adminretail");
                types.add("adminorder");
            }
            keyDot = yaml.getItemBuilder("dot").build();
            keyDelete = yaml.getItemBuilder("delete").build();
            key0 = yaml.getItemBuilder("key0").build();
            key1 = yaml.getItemBuilder("key1").build();
            key2 = yaml.getItemBuilder("key2").build();
            key3 = yaml.getItemBuilder("key3").build();
            key4 = yaml.getItemBuilder("key4").build();
            key5 = yaml.getItemBuilder("key5").build();
            key6 = yaml.getItemBuilder("key6").build();
            key7 = yaml.getItemBuilder("key7").build();
            key8 = yaml.getItemBuilder("key8").build();
            key9 = yaml.getItemBuilder("key9").build();
        }

        zeroPrice = transaction.getPrice() == 0;

        if (!edition.getExtras().equals(transaction.getExtras())) {
            edition.setCurrency("item");
            edition.setExtras(transaction.getExtras());
        }
        switch(edition.getCurrency()) {
            case "coin":
                asyncButtonMap.put(8, ((ButtonItemBuilder)yaml.getItemBuilder(edition.getCurrency())).actionEdit().build());
                dot = true;
                break;
            case "point":
                asyncButtonMap.put(8, ((ButtonItemBuilder)yaml.getItemBuilder(edition.getCurrency())).actionEdit().build());
                break;
            case "item":
                ItemStack currencyItem =((ButtonItemBuilder) yaml.getItemBuilder(edition.getCurrency())).actionPlugin("currency " + edition.getId()).build();
                if (!"".equals(edition.getExtras())) {
                    currencyItem = SerializationUtil.deserializeItemStack(edition.getExtras());
                    ItemUtils.setItemNbt(currencyItem, ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PLUGIN);
                    ItemUtils.setItemNbt(currencyItem, ButtonAction.NBT_VALUE_KEY, "currency " + edition.getId());
                    ItemUtils.addLore(currencyItem, Language.GUI_CURRENCY_ITEM);
                }
                asyncButtonMap.put(8, currencyItem);
                break;
            default:
                break;
        }

        switch (edition.getType()) {
            case "retail":
            case "adminretail":
            case "order":
            case "adminorder":
                this.placeKeyboard();
                asyncButtonMap.put(17, ((ButtonItemBuilder)yaml.getItemBuilder(edition.getType())).actionEdit().build());
                break;
            case "auction":
                if (transaction.getPrice() == transaction.getCost()) {
                    this.placeKeyboard();
                }
                asyncButtonMap.put(17, ((ButtonItemBuilder)yaml.getItemBuilder(edition.getType().replace("admin", ""))).actionEdit().build());
                break;
            case "prepare":
            default:
                asyncButtonMap.put(17, ((ButtonItemBuilder)yaml.getItemBuilder("wait")).actionEdit().build());
                break;
        }

        switch (transaction.getType()) {
            case "retail":
            case "adminretail":
            case "order":
            case "adminorder":
                asyncButtonMap.put(35, yaml.getItemBuilder("edit").build());
                break;
            case "auction":
                if (transaction.getPrice() == transaction.getCost()) {
                    asyncButtonMap.put(35, yaml.getItemBuilder("edit").build());
                } else {
                    asyncButtonMap.put(35, yaml.getItemBuilder("edit_disable").build());
                }
                break;
            case "prepare":
            default:
                asyncButtonMap.put(35, yaml.getItemBuilder("done").build());
                prepare = true;
                break;
        }

        // 下架按钮
        asyncButtonMap.put(26, ((ButtonItemBuilder)yaml.getItemBuilder("cancel")).actionPlayerCommand("market cancel " + target).build());

        // 分类
        ItemStack categoryNone = yaml.getItemBuilder("categoryNone").build();
        for (int i = 0, j = 2; i < 7; i++, j *= 2) {
            ItemStack categoryItem = ((ButtonItemBuilder)categorys[i]).actionEdit().build();
            if ((transaction.getCategory() & j) == 0) {
                String category = ItemUtils.getDisplayName(categoryItem);
                categoryItem = categoryNone.clone();
                ItemUtils.setDisplayName(categoryItem, category);
            }
            asyncButtonMap.put(45 + i, categoryItem);
        }

         // 交易物品展示
        ItemStack item = SerializationUtil.deserializeItemStack(transaction.getStack());
        ItemStack editItem = SerializationUtil.deserializeItemStack(edition.getStack());
        MarketItem.loreItemData(item, transaction, "");
        MarketItem.loreItemData(editItem, edition, "");
        asyncButtonMap.put(10, item);
        asyncButtonMap.put(28, editItem);

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        ItemStack blank = yaml.getItemBuilder("blank").build();
        buttonMap.put(0, blank);
        buttonMap.put(1, blank);
        buttonMap.put(2, blank);
        buttonMap.put(9, blank);
        buttonMap.put(11, blank);
        buttonMap.put(18, blank);
        buttonMap.put(19, blank);
        buttonMap.put(20, blank);
        buttonMap.put(27, blank);
        buttonMap.put(29, blank);
        for (int i = 36; i < 45; i++) {
            buttonMap.put(i, blank);
        }
       
        buttonMap.put(53, yaml.getItemBuilder("back").build());
    }

    @Override
    public ItemStack getItem(int slot) {
        switch (slot){
            case 4:
                num += "7";
                break;
            case 5:
                num += "8";
                break;
            case 6:
                num += "9";
                break;
            case 13:
                num += "4";
                break;
            case 14:
                num += "5";
                break;
            case 15:
                num += "6";
                break;
            case 22:
                num += "1";
                break;
            case 23:
                num += "2";
                break;
            case 24:
                num += "3";
                break;
            case 32:
                num += "0";
                break;
            case 31:
                if (!num.contains(".")) num += ".";
                break;
            case 8:
                if (prepare && Config.PLAYER_POINT_MARKET) {
                    if ("coin".equals(edition.getCurrency())) {
                        edition.setCurrency("point");
                        if (num.contains(".")) {
                            num = num.substring(0, num.indexOf("."));
                        }
                    } else if("point".equals(edition.getCurrency())) {
                        edition.setCurrency("item");
                    } else {
                        edition.setCurrency("coin");
                    }
                }
                break;
            case 17:
                if (!prepare) {
                    break;
                }
                edition.setType(types.get(typeNum++));
                if (typeNum >= types.size()) {
                    typeNum = 0;
                }
                break;
            case 33:
                if (num.length() > 0) num = num.substring(0, num.length()-1);
                break;
            case 35:
                // 修改价格
                handler.putTasks(GlobalMarket.TASK_MARKET, new TaskReprice(edition.getOwnerName(), service, guide, edition.getId(), edition.getCost()));
                // 确认发售
                if (prepare) {
                    handler.putTasks(GlobalMarket.TASK_MARKET, new TaskAffirm(edition.getOwnerName(), service, guide, edition.getId(), edition.getType(), edition.getCurrency(), edition.getExtras()));
                }
                break;
            case 45:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 1)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            case 46:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 2)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            case 47:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 3)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            case 48:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 4)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            case 49:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 5)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            case 50:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 6)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            case 51:
                edition.setCategory(this.switchCategory(edition.getCategory(), (int)Math.pow(2, 7)));
                service.updateTransactionCategory(edition.getCategory(), edition.getId());
                break;
            default:
        }
        if (zeroPrice) {
            edition.setPrice(NumberConversions.toDouble(num));
        }
        edition.setCost(NumberConversions.toDouble(num));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, target);
        return super.getItem(slot);
    }

    public boolean isCategory(long category, long num) {
        return (category & num) != 0;
    }

    public long onCategory(long category, long num) {
        return category | num;
    }

    public long offCategory(long category, long num) {
        return category & (~num);
    }

    public long switchCategory(long category, long num) {
        return this.isCategory(category, num) ? this.offCategory(category, num) : this.onCategory(category, num);
    }

    /**
     * 放置价格修改键盘
     * @param edition 商品副本
     */
    private void placeKeyboard() {
        // 小数点
        if (dot) {
            if (num.endsWith(".")) {
                ItemUtils.setLore(keyDot, "§7" + num + "§70", 0);
            } else if (!num.contains(".")){
                ItemUtils.setLore(keyDot, "§7" + num + "§9.§70", 0);
            } else {
                ItemUtils.setLore(keyDot, "§7" + num, 0);
            }
            asyncButtonMap.put(31, keyDot);
        }

        // 删除键
        if (num.length() > 0) {
            if (num.endsWith(".")) {
                ItemUtils.setLore(keyDelete, "§7" + new StringBuilder(num).insert(num.length() - 1, "§c"), 0);
            } else if (!num.contains(".")){
                ItemUtils.setLore(keyDelete, "§7" + new StringBuilder(num).insert(num.length() - 1, "§c") + "§9.§70", 0);
            } else {
                ItemUtils.setLore(keyDelete, "§7" + new StringBuilder(num).insert(num.length() - 1, "§c"), 0);
            }
        }
        asyncButtonMap.put(33, keyDelete);

        // 数字
        asyncButtonMap.put(4, key7);
        asyncButtonMap.put(5, key8);
        asyncButtonMap.put(6, key9);
        asyncButtonMap.put(13, key4);
        asyncButtonMap.put(14, key5);
        asyncButtonMap.put(15, key6);
        asyncButtonMap.put(22, key1);
        asyncButtonMap.put(23, key2);
        asyncButtonMap.put(24, key3);
        asyncButtonMap.put(32, key0);
        String end = num.contains(".") ? "":"§7.0";
        ItemUtils.setLore(key7, "§7" + num + "§8§n7" + end, 0);
        ItemUtils.setLore(key8, "§7" + num + "§8§n8" + end, 0);
        ItemUtils.setLore(key9, "§7" + num + "§8§n9" + end, 0);
        ItemUtils.setLore(key4, "§7" + num + "§8§n4" + end, 0);
        ItemUtils.setLore(key5, "§7" + num + "§8§n5" + end, 0);
        ItemUtils.setLore(key6, "§7" + num + "§8§n6" + end, 0);
        ItemUtils.setLore(key1, "§7" + num + "§8§n1" + end, 0);
        ItemUtils.setLore(key2, "§7" + num + "§8§n2" + end, 0);
        ItemUtils.setLore(key3, "§7" + num + "§8§n3" + end, 0);
        ItemUtils.setLore(key0, "§7" + num + "§8§n0" + end, 0);
    }
    
}
