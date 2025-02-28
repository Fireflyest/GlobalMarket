package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;

/**
 * 主页视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class HomeView extends View {

    private HomePage homePage;

    public HomeView() {
        super();
    }

    @Override
    public Page getHomePage(String target) {
        if (homePage == null) {
            homePage = new HomePage();
        }
        return homePage;
    }

}
