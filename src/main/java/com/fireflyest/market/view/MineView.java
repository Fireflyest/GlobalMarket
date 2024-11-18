package com.fireflyest.market.view;

import java.util.HashMap;

import io.fireflyest.emberlib.inventory.View;

import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class MineView implements View<MinePage>{

    private final HashMap<String, MinePage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public MineView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public MinePage getFirstPage(String target) {
        pageMap.computeIfAbsent(target, k -> new MinePage(target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
