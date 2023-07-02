package com.fireflyest.market.view;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;

import com.fireflyest.market.bean.Info;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;
import com.google.gson.Gson;

public class AffairPage extends TemplatePage {

    private final MarketService service;
    private final MarketYaml yaml;

    protected AffairPage(String title, String target, int page, MarketService service, MarketYaml yaml) {
        super(title, target, page, 27);
        this.service = service;
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Transaction transaction = service.selectTransactionById(NumberConversions.toInt(target));
        // 交易不存在
        if (transaction == null) {
            asyncButtonMap.put(13, new ItemStack(Material.AIR));
            asyncButtonMap.put(14, yaml.getItemBuilder("wait").build());
            asyncButtonMap.put(15, new ItemStack(Material.AIR));
            return asyncButtonMap;
        }

        // 交易物品展示
        ItemStack item = SerializationUtil.deserializeItemStack(transaction.getStack());
        asyncButtonMap.put(10, item);

        ItemStack visit = ((ButtonItemBuilder)yaml.getItemBuilder("visit")).actionOpenPage("market.visit." + target).build();
        ItemUtils.setSkullOwner(visit, Bukkit.getOfflinePlayer(UUID.fromString(transaction.getOwner())));
        ItemUtils.setDisplayName(visit, "§3§l" + transaction.getOwnerName());
        asyncButtonMap.put(8, visit);

        String symbol = "";
        String command;
        int amount = item.getAmount();
        boolean partial = Config.BUY_PARTIAL; 
        switch(transaction.getCurrency()) {
            case "coin":
                symbol = Language.COIN_SYMBOL;
                break;
            case "point":
                partial = false;
                symbol = Language.POINT_SYMBOL;
                break;
            case "item":
                partial = false;
                if (!"".equals(transaction.getExtras())) {
                    ItemStack currencyItem = SerializationUtil.deserializeItemStack(transaction.getExtras());
                    asyncButtonMap.put(5, currencyItem);
                }
                break;
            default:
                break;
        }
        ItemStack data = yaml.getItemBuilder("transaction").build();
        switch (transaction.getType()) {
            case "retail":
            case "adminretail":
                command = "market buy " + target;
                if (partial) {
                    ItemStack buy1 = ((ButtonItemBuilder)yaml.getItemBuilder("buy1")).actionPlayerCommand(command + " 1").build();
                    ItemStack buy2 = ((ButtonItemBuilder)yaml.getItemBuilder("buy8")).actionPlayerCommand(command + " 8").build();
                    ItemStack buy3 = ((ButtonItemBuilder)yaml.getItemBuilder("buy")).actionPlayerCommand(command + " " + amount).build();
                    buy3.setAmount(amount);
                    ItemUtils.addLore(buy1, String.format(Language.GUI_PRICE, transaction.getCost() / amount, symbol));
                    ItemUtils.addLore(buy2, String.format(Language.GUI_PRICE, transaction.getCost() / amount * 8, symbol));
                    ItemUtils.addLore(buy3, String.format(Language.GUI_PRICE, transaction.getCost(), symbol));
                    ItemUtils.setDisplayName(buy3, ItemUtils.getDisplayName(buy3).replace("%amount%", String.valueOf(amount)));
                    asyncButtonMap.put(13, buy1);
                    if (amount > 8) asyncButtonMap.put(14, buy2);
                    asyncButtonMap.put(15, buy3);
                } else {
                    ItemStack buy = ((ButtonItemBuilder)yaml.getItemBuilder("buy")).actionPlayerCommand(command + " " + 0).build();
                    buy.setAmount(amount);
                    ItemUtils.addLore(buy, String.format(Language.GUI_PRICE, transaction.getCost(), symbol));
                    ItemUtils.setDisplayName(buy, ItemUtils.getDisplayName(buy).replace("%amount%", String.valueOf(amount)));
                    asyncButtonMap.put(14, buy);
                }
                ItemUtils.addLore(data, String.format(Language.GUI_HEAT, transaction.getHeat()));
                if (Config.TERM_OF_VALIDITY != -1) {
                    ItemUtils.addLore(data, String.format(Language.GUI_DEADLINE, TimeUtils.duration(transaction.getAppear() + Config.TERM_OF_VALIDITY * 1000 * 60 * 60 * 24 - TimeUtils.getTime())));
                }
                break;
            case "order":
            case "adminorder":
                command = "market sale " + target;
                if (partial) {
                    ItemStack sale1 = ((ButtonItemBuilder)yaml.getItemBuilder("buy1")).actionPlayerCommand(command + " 1").build();
                    ItemStack sale2 = ((ButtonItemBuilder)yaml.getItemBuilder("buy8")).actionPlayerCommand(command + " 8").build();
                    ItemStack sale3 = ((ButtonItemBuilder)yaml.getItemBuilder("buy")).actionPlayerCommand(command + " " + amount).build();
                    sale3.setAmount(amount);
                    ItemUtils.addLore(sale1, String.format(Language.GUI_ORDER_PRICE, transaction.getCost() / amount, symbol));
                    ItemUtils.addLore(sale2, String.format(Language.GUI_ORDER_PRICE, transaction.getCost() / amount * 8, symbol));
                    ItemUtils.addLore(sale3, String.format(Language.GUI_ORDER_PRICE, transaction.getCost(), symbol));
                    ItemUtils.setDisplayName(sale3, ItemUtils.getDisplayName(sale3).replace("%amount%", String.valueOf(amount)));
                    asyncButtonMap.put(13, sale1);
                    if (amount > 8) asyncButtonMap.put(14, sale2);
                    asyncButtonMap.put(15, sale3);
                } else {
                    ItemStack sale = ((ButtonItemBuilder)yaml.getItemBuilder("buy")).actionPlayerCommand(command + " " + 0).build();
                    sale.setAmount(amount);
                    ItemUtils.addLore(sale, String.format(Language.GUI_ORDER_PRICE, transaction.getCost(), symbol));
                    ItemUtils.setDisplayName(sale, ItemUtils.getDisplayName(sale).replace("%amount%", String.valueOf(amount)));
                    asyncButtonMap.put(14, sale);
                }
                ItemUtils.addLore(data, String.format(Language.GUI_HEAT, transaction.getHeat()));
                if (Config.TERM_OF_VALIDITY != -1) {
                    ItemUtils.addLore(data, String.format(Language.GUI_DEADLINE, TimeUtils.duration(transaction.getAppear() + Config.TERM_OF_VALIDITY * 1000 * 60 * 60 * 24 - TimeUtils.getTime())));
                }
                break;
            case "auction":
                command = "market bid " + target;
                ItemStack bid1 = ((ButtonItemBuilder)yaml.getItemBuilder("bid10")).actionPlayerCommand(command + " 10").build();
                ItemStack bid2 = ((ButtonItemBuilder)yaml.getItemBuilder("bid100")).actionPlayerCommand(command + " 100").build();
                ItemStack bid3 = ((ButtonItemBuilder)yaml.getItemBuilder("bid1000")).actionPlayerCommand(command + " 1000").build();
                ItemUtils.addLore(bid1, String.format(Language.GUI_PRESENT_PRICE, transaction.getCost(), symbol));
                ItemUtils.addLore(bid2, String.format(Language.GUI_PRESENT_PRICE, transaction.getCost(), symbol));
                ItemUtils.addLore(bid3, String.format(Language.GUI_PRESENT_PRICE, transaction.getCost(), symbol));
                asyncButtonMap.put(13, bid1);
                asyncButtonMap.put(14, bid2);
                asyncButtonMap.put(15, bid3);
                Info info = new Gson().fromJson(transaction.getDesc(), Info.class);
                ItemUtils.addLore(data, String.format(Language.GUI_AUCTION_CONFIRM, 3 - transaction.getHeat()));
                ItemUtils.addLore(data, String.format(Language.GUI_AUCTION_PLAYERS, info.getStrings()));
                break;
            case "prepare":
            default:
                asyncButtonMap.put(14, yaml.getItemBuilder("wait").build());
                break;
        }
        asyncButtonMap.put(17, data);

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

        buttonMap.put(17, yaml.getItemBuilder("transaction").build());
        buttonMap.put(26, yaml.getItemBuilder("back").build());
    }
    
}
