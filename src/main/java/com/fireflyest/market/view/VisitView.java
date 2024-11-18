package com.fireflyest.market.view;

import java.util.HashMap;

import io.fireflyest.emberlib.inventory.View;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class VisitView implements View<VisitPage>{

    private final HashMap<String, VisitPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public VisitView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public VisitPage getFirstPage(String target) {
        String title = Language.TITLE_VISIT_PAGE.replace("%player%", target);
        pageMap.computeIfAbsent(target, k -> new VisitPage(title, target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
