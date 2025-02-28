package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import com.fireflyest.market.service.MarketService;

/**
 * 搜索视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class SearchView extends View {

    private final MarketService service;
    
    public SearchView(MarketService service) {
        this.service = service;
    }

    @Override
    public Page getHomePage(String target) {
        return new SearchPage(target, 1, service);
    }
    
}
