package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import com.fireflyest.market.service.MarketService;

/**
 * 玩家商店视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class StoreView extends View {


    private final MarketService service;
    
    public StoreView(MarketService service) {
        this.service = service;
    }

    @Override
    public Page getHomePage(String target) {
        return pagesMap.computeIfAbsent(target, k -> new StorePage(target, 1, service));
    }
    
}
