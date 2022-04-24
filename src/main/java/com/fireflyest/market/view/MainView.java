package com.fireflyest.market.view;

import com.fireflyest.gui.api.View;

import java.util.HashMap;
import java.util.Map;

public class MainView implements View<MainPage> {

    public static final String ALL = "全部交易";
    public static final String NORMAL = "玩家交易";
    public static final String ADMIN = "系统交易";
    public static final String POINT = "点券交易";

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
