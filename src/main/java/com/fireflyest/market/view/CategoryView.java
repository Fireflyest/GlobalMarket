package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

/**
 * 类别查询视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class CategoryView extends View {

    private final MarketService service;
    
    public CategoryView(MarketService service) {
        this.service = service;
    }

    @Override
    public Page getHomePage(String target) {
        final String title;
        switch (target) {
            case "category1":
                title = Language.TITLE_CATEGORY_1.get();
                break;
            case "category2":
                title = Language.TITLE_CATEGORY_2.get();
                break;
            case "category3":
                title = Language.TITLE_CATEGORY_3.get();
                break;
            case "category4":
                title = Language.TITLE_CATEGORY_4.get();
                break;
            case "category5":
                title = Language.TITLE_CATEGORY_5.get();
                break;
            case "category6":
                title = Language.TITLE_CATEGORY_6.get();
                break;
            case "category7":
                title = Language.TITLE_CATEGORY_7.get();
                break;
            default:
                title = "???";
                break;
        }
        return pagesMap.computeIfAbsent(target, k -> new CategoryPage(target, 1, title, service));
    }
    
}
