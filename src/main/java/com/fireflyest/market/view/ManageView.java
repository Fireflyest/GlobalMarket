package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.View;

import com.fireflyest.market.data.MarketYaml;

public class ManageView implements View<ManagePage> {

    private final MarketYaml yaml;
    private ManagePage AdminPage;

    public ManageView(MarketYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public ManagePage getFirstPage(String target) {
        if (AdminPage == null) {
            AdminPage = new ManagePage(yaml);
        }
        return AdminPage;
    }

    @Override
    public void removePage(String target) {
        //
    }
}
