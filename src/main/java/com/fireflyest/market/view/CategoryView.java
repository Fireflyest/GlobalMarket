package com.fireflyest.market.view;

import java.util.HashMap;

import org.fireflyest.craftgui.api.View;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class CategoryView implements View<CategoryPage>{

    private final HashMap<String, CategoryPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public CategoryView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public CategoryPage getFirstPage(String target) {
        String title;
        switch (target) {
            case "category1":
                title = Language.TITLE_CATEGORY1_PAGE;
                break;
            case "category2":
                title = Language.TITLE_CATEGORY2_PAGE;
                break;
            case "category3":
                title = Language.TITLE_CATEGORY3_PAGE;
                break;
            case "category4":
                title = Language.TITLE_CATEGORY4_PAGE;
                break;
            case "category5":
                title = Language.TITLE_CATEGORY5_PAGE;
                break;
            case "category6":
                title = Language.TITLE_CATEGORY6_PAGE;
                break;
            case "category7":
                title = Language.TITLE_CATEGORY7_PAGE;
                break;
            default:
                title = "???";
                break;
        }
        pageMap.computeIfAbsent(target, k -> new CategoryPage(title, target, 1, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
