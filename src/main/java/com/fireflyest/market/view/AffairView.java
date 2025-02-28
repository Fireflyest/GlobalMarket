package com.fireflyest.market.view;

import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import com.fireflyest.market.service.MarketService;

/**
 * 交易界面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class AffairView extends View {

    private final MarketService service;
    
    public AffairView(MarketService service) {
        this.service = service;
    }

    @Override
    public Page getHomePage(String target) {
        final int id = NumberConversions.toInt(target);
        return pagesMap.computeIfAbsent(target, k -> new AffairPage(target, id, service));
    }
    
}
