package com.fireflyest.market.view;

import com.fireflyest.gui.api.View;
import com.fireflyest.market.data.Language;

import java.util.HashMap;
import java.util.Map;

public class MainView implements View<MainPage> {

    public static final String ALL = Language.MARKET_MAIN_ALL_NICK;
    public static final String NORMAL = Language.MARKET_MAIN_NORMAL_NICK;
    public static final String ADMIN = Language.MARKET_MAIN_ADMIN_NICK;
    public static final String POINT = Language.MARKET_MAIN_POINT_NICK;

    private final Map<String, MainPage> pageMap = new HashMap<>();

    public MainView(String title) {
        pageMap.put(ALL, new MainPage(title, ALL, 1, 54));
        pageMap.put(NORMAL, new MainPage(title, NORMAL, 1, 54));
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
