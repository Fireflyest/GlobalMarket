package com.fireflyest.market.view;

import java.util.HashMap;

import io.fireflyest.emberlib.inventory.View;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class SearchView implements View<SearchPage>{

    private final HashMap<String, SearchPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public SearchView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public SearchPage getFirstPage(String target) {
        String title = Language.TITLE_SEARCH_PAGE.replace("%search%", target);
        pageMap.computeIfAbsent(target, k -> new SearchPage(title, target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
