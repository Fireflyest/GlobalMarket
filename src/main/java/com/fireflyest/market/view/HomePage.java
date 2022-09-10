package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.util.YamlUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.data.Language;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.item.ViewItemBuilder;
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

        if (Config.CUSTOM_CLASSIFY) {
            FileConfiguration category = YamlUtils.getCategory();
            int pos = 0;
            for (String categoryKey : category.getKeys(false)) {
                String material = category.getString(String.format("%s.button", categoryKey));
                String name = category.getString(String.format("%s.name", categoryKey));
                if (name == null || material == null) continue;
                ViewItemBuilder viewItemBuilder = new ViewItemBuilder(material)
                        .name(name);
                for (String l : category.getStringList(String.format("%s.lore", categoryKey))) {
                    viewItemBuilder.lore(l);
                }
                ItemStack button = viewItemBuilder
                        .command("classify " + categoryKey)
                        .build();
                crashMap.put(pos++, button);
            }
        } else {
            crashMap.put(0, MarketButton.EDIBLE);
            crashMap.put(1, MarketButton.ITEM);
            crashMap.put(2, MarketButton.BLOCK);
            crashMap.put(3, MarketButton.BURNABLE);
            crashMap.put(4, MarketButton.EQUIP);
            crashMap.put(5, MarketButton.KNOWLEDGE);
        }

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
        itemMap.put(8, MarketButton.SEARCH);

        itemMap.put(18, MarketButton.RETAIL);
        itemMap.put(19, MarketButton.AUCTION);
        itemMap.put(20, MarketButton.ADMIN);
        int pos = 21;
        if (Config.POINT) itemMap.put(pos++, MarketButton.POINT);
        if (Config.COLLECT) itemMap.put(pos++, MarketButton.COLLECT);
        if (Config.TRADE) itemMap.put(pos, MarketButton.TRADE);

        for (int i = 9; i < 17; i++) {
            itemMap.put(i, MarketButton.BLANK);
        }
        itemMap.put(7, MarketButton.BLANK);
        itemMap.put(25, MarketButton.BLANK);

        itemMap.put(26, MarketButton.BACK);
    }

    @Override
    public void updateTitle(String s) {

    }

}
