package com.fireflyest.market.view;

import com.fireflyest.gui.api.View;

import java.util.HashMap;
import java.util.Map;

public class SearchView implements View<SearchPage> {

    private final Map<String, SearchPage> pageMap = new HashMap<>();

    private final String title;

    public SearchView(String title) {
        this.title = title;
    }

    @Override
    public SearchPage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new SearchPage(title, target, 1, 54));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
