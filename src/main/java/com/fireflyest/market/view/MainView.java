package com.fireflyest.market.view;

<<<<<<< HEAD
import com.fireflyest.market.data.Language;
import org.fireflyest.craftgui.api.View;

=======
>>>>>>> 9bc3b8e (版本更新)
import java.util.HashMap;

import org.fireflyest.craftgui.api.View;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class MainView implements View<MainPage>{

    private final HashMap<String, MainPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public MainView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public MainPage getFirstPage(String target) {
        String title;
        switch (target) {
            case "normal":
                title = Language.TITLE_MAIN_PAGE;
                break;
            case "retail":
                title = Language.TITLE_RETAIL_PAGE;
                break;
            case "auction":
                title = Language.TITLE_AUCTION_PAGE;
                break;
            case "admin":
                title = Language.TITLE_ADMIN_PAGE;
                break;
            case "point":
                title = Language.TITLE_POINT_PAGE;
                break;
            case "coin":
                title = Language.TITLE_COIN_PAGE;
                break;
            case "item":
                title = Language.TITLE_ITEM_PAGE;
                break;
            default:
                title = Language.TITLE_MAIN_PAGE;
                break;
        }
        pageMap.computeIfAbsent(target, k -> new MainPage(title, target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
