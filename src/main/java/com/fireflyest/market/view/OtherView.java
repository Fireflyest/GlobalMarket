package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class OtherView implements View<OtherPage> {

    private final Map<String, OtherPage> pageMap = new HashMap<>();

    private final String title;

    public OtherView(String title) {
        this.title = title;
    }

    @Override
    public OtherPage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new OtherPage(title, target, 1, 54));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
