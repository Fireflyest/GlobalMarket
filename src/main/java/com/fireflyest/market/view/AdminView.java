package com.fireflyest.market.view;

import com.fireflyest.market.data.Language;
import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class AdminView implements View<AdminPage> {

    public static final String NORMAL = Language.MARKET_ADMIN_NICK;

    private final Map<String, AdminPage> pageMap = new HashMap<>();

    public AdminView(String title) {
        pageMap.put(NORMAL, new AdminPage(title, NORMAL, 1));
    }

    @Override
    public AdminPage getFirstPage(String target){
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
