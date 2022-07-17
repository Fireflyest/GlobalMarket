package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ConvertUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SellPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

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
        this.done = MarketButton.DONE.clone();
        this.add1 = MarketButton.ADD_1.clone();
        this.add10 = MarketButton.ADD_10.clone();
        this.add100 = MarketButton.ADD_100.clone();
        this.reduce1 = MarketButton.REDUCE_1.clone();
        this.reduce10 = MarketButton.REDUCE_10.clone();
        this.reduce100 = MarketButton.REDUCE_100.clone();

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
        crashMap.clear();
        crashMap.putAll(itemMap);

        crashMap.put(0, itemStack);

        crashMap.put(2, add1);
        crashMap.put(3, add10);
        crashMap.put(4, add100);
        crashMap.put(11, reduce1);
        crashMap.put(12, reduce10);
        crashMap.put(13, reduce100);

        crashMap.put(8, done);

        return crashMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new ConcurrentHashMap<>(itemMap);
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return crashMap.get(slot);
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
        itemMap.put(1, MarketButton.BLANK.clone());
        itemMap.put(7, MarketButton.BLANK.clone());
        itemMap.put(10, MarketButton.BLANK.clone());
        itemMap.put(16, MarketButton.BLANK.clone());

        itemMap.put(17, MarketButton.CLOSE.clone());
    }

    @Override
    public void updateTitle(String s) {

    }

}
