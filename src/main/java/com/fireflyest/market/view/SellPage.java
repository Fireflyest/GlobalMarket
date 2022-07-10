package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SellPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();

    private final Inventory inventory;
    private final String target;
    private final ItemStack itemStack;
    private final ItemStack done;
    private final ItemStack add1;
    private final ItemStack add10;
    private final ItemStack add100;
    private final ItemStack reduce1;
    private final ItemStack reduce10;
    private final ItemStack reduce100;

    private final  int maxAmount;
    private double price = 0;

    public SellPage(String target, String title, ItemStack itemStack) {
        this.target = target;
        this.itemStack = itemStack.clone();
        this.maxAmount = itemStack.getAmount();
        this.done = MarketItem.DONE.clone();
        this.add1 = MarketItem.ADD_1.clone();
        this.add10 = MarketItem.ADD_10.clone();
        this.add100 = MarketItem.ADD_100.clone();
        this.reduce1 = MarketItem.REDUCE_1.clone();
        this.reduce10 = MarketItem.REDUCE_10.clone();
        this.reduce100 = MarketItem.REDUCE_100.clone();

        String guiTitle = title + "§9" + Language.MARKET_QUICK_NICK;

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 18, guiTitle);

        this.refreshPage();
    }

    public void addPrice(double money){
        price += money;

        ItemUtils.setLore(add1, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(add10, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(add100, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(reduce1, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(reduce10, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(reduce100, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);

        ItemUtils.setLore(done, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);

        ItemUtils.setItemValue(done, String.format("sell %s %s", ConvertUtils.formatDouble(price), itemStack.getAmount()));
    }

    public void reducePrice(double money){
        price -= money;
        if (price < 0) price = 0.00;

        ItemUtils.setLore(add1, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(add10, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(add100, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(reduce1, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(reduce10, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);
        ItemUtils.setLore(reduce100, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);

        ItemUtils.setLore(done, "§3§l价格§7: §f"+ConvertUtils.formatDouble(price), 0);

        ItemUtils.setItemValue(done, String.format("sell %s %s", ConvertUtils.formatDouble(price), itemStack.getAmount()));
    }

    public void lessen(int amount){
        int a = itemStack.getAmount() - amount;
        if (a < 1) a = 1;
        itemStack.setAmount(a);
    }

    public void raise(int amount){
        int a = itemStack.getAmount() + amount;
        if (a > maxAmount) a = maxAmount;
        Player seller = Bukkit.getPlayer(target);
        if (seller != null) {
            int hand = seller.getInventory().getItemInMainHand().getAmount();
            if (a > hand) a = hand;
        }
        itemStack.setAmount(a);
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        Map<Integer, ItemStack> itemStackMap = new ConcurrentHashMap<>(itemMap);

        itemStackMap.put(0, itemStack);

        itemStackMap.put(2, add1);
        itemStackMap.put(3, add10);
        itemStackMap.put(4, add100);
        itemStackMap.put(11, reduce1);
        itemStackMap.put(12, reduce10);
        itemStackMap.put(13, reduce100);

        itemStackMap.put(8, done);

        return itemStackMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new ConcurrentHashMap<>(itemMap);
    }

    @Override
    public @NotNull Inventory getInventory(){
        return inventory;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public ViewPage getNext() {
        return null;
    }

    @Override
    public ViewPage getPre() {
        return null;
    }

    @Override
    public void setPre(ViewPage pre) {
    }

    @Override
    public void setNext(ViewPage viewPage) {
    }

    @Override
    public void refreshPage() {
        itemMap.put(1, MarketItem.BLANK.clone());
        itemMap.put(7, MarketItem.BLANK.clone());
        itemMap.put(10, MarketItem.BLANK.clone());
        itemMap.put(16, MarketItem.BLANK.clone());

        itemMap.put(17, MarketItem.CLOSE.clone());

        itemMap.put(9, MarketItem.AMOUNT.clone());
    }

}
