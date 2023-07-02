package com.fireflyest.market.view;

import java.util.HashMap;

import org.fireflyest.craftgui.api.View;

import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class StoreView implements View<StorePage>{

    private final HashMap<String, StorePage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public StoreView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public StorePage getFirstPage(String target) {
        pageMap.computeIfAbsent(target, k -> new StorePage(target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
