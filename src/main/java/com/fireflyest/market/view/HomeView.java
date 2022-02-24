package com.fireflyest.market.view;

import com.fireflyest.gui.api.View;

import java.util.HashMap;
import java.util.Map;

public class HomeView implements View<HomePage> {

    public static final String NORMAL = "导航界面";

    private final Map<String, HomePage> pageMap = new HashMap<>();

    public HomeView(String title) {
        pageMap.put(NORMAL, new HomePage(title));
    }

    @Override
    public HomePage getFirstPage(String target){
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
