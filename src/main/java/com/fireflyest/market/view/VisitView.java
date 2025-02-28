package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import com.fireflyest.market.service.MarketService;

/**
 * 访问视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class VisitView extends View {

    private final MarketService service;
    
    public VisitView(MarketService service) {
        this.service = service;
    }

    @Override
    public Page getHomePage(String target) {
        return  pagesMap.computeIfAbsent(target, k -> new VisitPage(target, 1, service));
    }

}
