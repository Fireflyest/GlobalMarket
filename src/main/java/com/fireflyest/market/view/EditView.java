package com.fireflyest.market.view;

import java.util.HashMap;

import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.TaskHandler;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class EditView implements View<EditPage>{

    private final HashMap<String, EditPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    private final ViewGuide guide;
    private final TaskHandler handler;
    
    public EditView(MarketService service, MarketYaml yaml, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.yaml = yaml;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    public EditPage getFirstPage(String target) {
        String nickname = service.selectTransactionNickname(NumberConversions.toInt(target));
        pageMap.computeIfAbsent(target, k -> new EditPage(Language.TITLE_EDIT_PAGE.replace("item", nickname), target, 1, service, yaml, guide, handler));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
