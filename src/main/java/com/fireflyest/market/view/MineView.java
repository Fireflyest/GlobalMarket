package com.fireflyest.market.view;

import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import com.fireflyest.market.service.MarketService;

/**
 * 个人视图
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class MineView extends View {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;
    
    /**
     * 构造个人视图
     * 
     * @param service 市场服务
     * @param guide 界面导航
     * @param handler 任务处理器
     */
    public MineView(MarketService service, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    public Page getHomePage(String target) {
        return pagesMap.computeIfAbsent(target, 
            k -> new MinePage(target, 1, service, guide, handler));
    }

}
