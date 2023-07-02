package com.fireflyest.market.view;

<<<<<<< HEAD
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
=======
>>>>>>> 9bc3b8e (版本更新)
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;

import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;

public class HomePage extends TemplatePage {

    private final MarketYaml yaml;

    protected HomePage(MarketYaml yaml) {
        super(Language.TITLE_HOME_PAGE, "", 0, 27);
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
<<<<<<< HEAD
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
=======
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);
        return buttonMap;
>>>>>>> 9bc3b8e (版本更新)
    }

    @Override
    public void refreshPage() {
<<<<<<< HEAD
        itemMap.put(8, MarketButton.SEARCH);

        itemMap.put(18, MarketButton.RETAIL);
        itemMap.put(19, MarketButton.AUCTION);
        itemMap.put(20, MarketButton.ADMIN);
=======
        buttonMap.put(0, ((ButtonItemBuilder)yaml.getItemBuilder("category1"))
                .actionOpenPage("market.category.category1")
                .build());
        buttonMap.put(1, ((ButtonItemBuilder)yaml.getItemBuilder("category2"))
                .actionOpenPage("market.category.category2")
                .build());
        buttonMap.put(2, ((ButtonItemBuilder)yaml.getItemBuilder("category3"))
                .actionOpenPage("market.category.category3")
                .build());
        buttonMap.put(3, ((ButtonItemBuilder)yaml.getItemBuilder("category4"))
                .actionOpenPage("market.category.category4")
                .build());
        buttonMap.put(4, ((ButtonItemBuilder)yaml.getItemBuilder("category5"))
                .actionOpenPage("market.category.category5")
                .build());
        buttonMap.put(5, ((ButtonItemBuilder)yaml.getItemBuilder("category6"))
                .actionOpenPage("market.category.category6")
                .build());
        buttonMap.put(6, ((ButtonItemBuilder)yaml.getItemBuilder("category7"))
                .actionOpenPage("market.category.category7")
                .build());

        buttonMap.put(8, yaml.getItemBuilder("search").build());

        buttonMap.put(18, ((ButtonItemBuilder)yaml.getItemBuilder("admin"))
                .actionOpenPage("market.main.admin")
                .build());
        buttonMap.put(19, ((ButtonItemBuilder)yaml.getItemBuilder("auction"))
                .actionOpenPage("market.main.auction")
                .build());
        buttonMap.put(20, ((ButtonItemBuilder)yaml.getItemBuilder("retail"))
                .actionOpenPage("market.main.retail")
                .build());
>>>>>>> 9bc3b8e (版本更新)
        int pos = 21;
        if (Config.ORDER_MARKET) {
            buttonMap.put(pos++, ((ButtonItemBuilder)yaml.getItemBuilder("order"))
                    .actionOpenPage("market.main.order")
                    .build());
        }
        if (Config.PLAYER_POINT_MARKET) {
            buttonMap.put(pos++, ((ButtonItemBuilder)yaml.getItemBuilder("point"))
                    .actionOpenPage("market.main.point")
                    .build());
        }
        if (Config.TRADE_MARKET) {
            buttonMap.put(pos, ((ButtonItemBuilder)yaml.getItemBuilder("trade"))
                    .actionOpenPage("market.main.trade")
                    .build());
        }

        ItemStack blank = yaml.getItemBuilder("blank").build();
        for (int i = 9; i < 17; i++) {
            buttonMap.put(i, blank);
        }
        buttonMap.put(7, blank);
        buttonMap.put(25, blank);

        buttonMap.put(17, yaml.getItemBuilder("store").build());
        buttonMap.put(26, yaml.getItemBuilder("back").build());

    }
    
}
