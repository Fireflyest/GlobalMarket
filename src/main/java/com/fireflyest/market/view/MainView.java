package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;
import com.fireflyest.market.data.Language;

import java.util.HashMap;
import java.util.Map;

public class MainView implements View<MainPage> {

    public static final String NORMAL = Language.MARKET_MAIN_NORMAL_NICK;
    public static final String RETAIL = Language.MARKET_MAIN_RETAIL_NICK;
    public static final String AUCTION = Language.MARKET_MAIN_AUCTION_NICK;
    public static final String ADMIN = Language.MARKET_MAIN_ADMIN_NICK;
    public static final String POINT = Language.MARKET_MAIN_POINT_NICK;

    private final Map<String, MainPage> pageMap = new HashMap<>();

    public MainView(String title) {
        pageMap.put(NORMAL, new MainPage(title, NORMAL, 1, 54));
        pageMap.put(RETAIL, new MainPage(title, RETAIL, 1, 54));
        pageMap.put(AUCTION, new MainPage(title, AUCTION, 1, 54));
        pageMap.put(ADMIN, new MainPage(title, ADMIN, 1, 54));
        pageMap.put(POINT, new MainPage(title, POINT, 1, 54));
    }

    @Override
    public MainPage getFirstPage(String target){
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
