package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class MineView implements View<MinePage> {

    private final Map<String, MinePage> pageMap = new HashMap<>();

    private final String title;

    public MineView(String title) {
        this.title = title;
    }

    @Override
    public MinePage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new MinePage(title, target, 1, 54));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
