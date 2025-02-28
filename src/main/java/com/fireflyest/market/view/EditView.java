package com.fireflyest.market.view;

import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.View;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import com.fireflyest.market.service.MarketService;

/**
 * 编辑界面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class EditView extends View {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;
    
    /**
     * 编辑界面
     * 
     * @param service 服务
     * @param guide 导航
     * @param handler 任务
     */
    public EditView(MarketService service, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    public Page getHomePage(String target) {        
        final int id = NumberConversions.toInt(target);
        return pagesMap.computeIfAbsent(target, 
            k -> new EditPage(target, id, service, guide, handler));
    }
    
}
