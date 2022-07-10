package com.fireflyest.market.view;

import com.fireflyest.gui.api.ViewPage;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class HomePage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

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
        return new HashMap<>(itemMap);
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
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
        itemMap.put(0, MarketItem.EDIBLE);
        itemMap.put(1, MarketItem.ITEM);
        itemMap.put(2, MarketItem.BLOCK);
        itemMap.put(3, MarketItem.BURNABLE);
        itemMap.put(4, MarketItem.INTERACTABLE);
        itemMap.put(5, MarketItem.EQUIP);
        itemMap.put(6, MarketItem.KNOWLEDGE);
        itemMap.put(8, MarketItem.SEARCH);

        itemMap.put(18, MarketItem.MARKET);
        itemMap.put(20, MarketItem.POINT);
        itemMap.put(21, MarketItem.ADMIN);
        itemMap.put(19, MarketItem.MARKET_ALL);
        itemMap.put(26, MarketItem.CLOSE);
        for (int i = 9; i < 18; i++) {
            itemMap.put(i, MarketItem.BLANK);
        }
    }

}
