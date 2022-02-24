package com.fireflyest.market.view;

import com.fireflyest.gui.api.View;

import java.util.HashMap;
import java.util.Map;

public class MailView implements View<MailPage> {

    private final Map<String, MailPage> pageMap = new HashMap<>();

    private final String title;

    public MailView(String title) {
        this.title = title;
    }

    @Override
    public MailPage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new MailPage(title, target, 1, 54));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
