package com.fireflyest.market.view;

import java.util.Map;

import org.bukkit.inventory.ItemStack;
import io.fireflyest.craftgui.view.TemplatePage;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;

public class ManagePage extends TemplatePage {

    private final MarketYaml yaml;

    protected ManagePage(MarketYaml yaml) {
        super(Language.TITLE_MANAGE_PAGE, "", 0, 27);
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
        buttonMap.put(0, yaml.getItemBuilder("reload").build());
        buttonMap.put(1, yaml.getItemBuilder("check").build());

        buttonMap.put(9, yaml.getItemBuilder("statistic").build());

        buttonMap.put(26, yaml.getItemBuilder("back").build());
    }
    
}
