package com.fireflyest.market.view;

import java.util.HashMap;

import io.fireflyest.emberlib.inventory.View;

import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class MailView implements View<MailPage>{

    private final HashMap<String, MailPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public MailView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public MailPage getFirstPage(String target) {
        pageMap.computeIfAbsent(target, k -> new MailPage(target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
