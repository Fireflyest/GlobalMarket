package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.data.Config;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class HomePage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private final Inventory inventory;

    public HomePage(String title) {
        String guiTitle = title;

        guiTitle += ("§9" + Language.MARKET_HOME_NICK);

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 27, guiTitle);

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
        return new HashMap<>(itemMap);
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
        return HomeView.NORMAL;
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
        itemMap.put(0, MarketButton.EDIBLE);
        itemMap.put(1, MarketButton.ITEM);
        itemMap.put(2, MarketButton.BLOCK);
        itemMap.put(3, MarketButton.BURNABLE);
        itemMap.put(4, MarketButton.INTERACTABLE);
        itemMap.put(5, MarketButton.EQUIP);
        itemMap.put(6, MarketButton.KNOWLEDGE);
        itemMap.put(8, MarketButton.SEARCH);

        itemMap.put(18, MarketButton.MARKET);
        itemMap.put(19, MarketButton.RETAIL);
        itemMap.put(20, MarketButton.AUCTION);
        itemMap.put(21, MarketButton.ADMIN);
        int pos = 22;
        if (Config.POINT) itemMap.put(pos++, MarketButton.POINT);
        if (Config.COLLECT) itemMap.put(pos++, MarketButton.COLLECT);
        if (Config.TRADE) itemMap.put(pos, MarketButton.TRADE);

        itemMap.put(26, MarketButton.CLOSE);
        for (int i = 9; i < 17; i++) {
            itemMap.put(i, MarketButton.BLANK);
        }
        itemMap.put(7, MarketButton.BLANK);
        itemMap.put(25, MarketButton.BLANK);

    }

    @Override
    public void updateTitle(String s) {

    }

}
