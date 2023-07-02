package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;

import com.fireflyest.market.data.MarketYaml;

public class HomeView implements View<HomePage> {

    private final MarketYaml yaml;
    private HomePage homePage;

    public HomeView(MarketYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    public HomePage getFirstPage(String target) {
        if (homePage == null) {
            homePage = new HomePage(yaml);
        }
        return homePage;
    }

    @Override
    public void removePage(String target) {
        //
    }
}
