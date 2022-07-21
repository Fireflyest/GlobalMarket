package com.fireflyest.market.view;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketButton;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

    private Sale sale;

    public SellPage(String target, String title) {
        this.target = target;

        String guiTitle = title + "§9" + Language.MARKET_QUICK_NICK;

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 18, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);



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
