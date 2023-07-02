package com.fireflyest.market.view;

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
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);
        return buttonMap;
    }

    @Override
    public void refreshPage() {
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
