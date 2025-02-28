package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

/**
 * 主页视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class MainView extends View {

    private final MarketService service;
    
    public MainView(MarketService service) {
        this.service = service;
    }

    @Override
    public Page getHomePage(String target) {
        final String title;
        switch (String.valueOf(target)) {
            case "normal":
                title = Language.TITLE_MAIN.get();
                break;
            case "retail":
                title = Language.TITLE_RETAIL.get();
                break;
            case "auction":
                title = Language.TITLE_AUCTION.get();
                break;
            case "admin":
                title = Language.TITLE_ADMIN.get();
                break;
            case "point":
                title = Language.TITLE_POINT.get();
                break;
            case "coin":
                title = Language.TITLE_COIN.get();
                break;
            case "item":
                title = Language.TITLE_ITEM.get();
                break;
            default:
                title = Language.TITLE_MAIN.get();
                break;
        }
        
        return pagesMap.computeIfAbsent(target, k -> new MainPage(target, 1, title, service));
    }

}
