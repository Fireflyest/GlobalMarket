package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;

/**
 * 管理视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class ManageView extends View {

    private ManagePage adminPage;

    public ManageView() {
        super();
    }

    @Override
    public Page getHomePage(String target) {
        if (adminPage == null) {
            adminPage = new ManagePage();
        }
        return adminPage;
    }

}
