package com.fireflyest.market.task;

import java.util.UUID;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;

/**
 * 交易分类修改
 * 
 * @author Fireflyest
 * @since 4.0
 */
public class TaskCategory extends Task {

    private final MarketService service;
    private final ViewGuide guide;

    private final long id;
    private final long category;
    private final int num;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param category 新分类
     * @param num 分类项
     */
    public TaskCategory(UUID uid, MarketService service, 
            ViewGuide guide, long id, long category, int num) {
        super(uid);
        this.service = service;
        this.guide = guide;
        this.id = id;
        this.category = category;
        this.num = num;
    }

    @Override
    public void execute() {
        service.updateTransactionCategory(category, id);

        // 根据分类刷新页面，类型是按二进制存储的
        for (int i = 0; i < 8; i++) {
            if ((num & (1 << i)) != 0) {
                guide.refreshPages(GlobalMarket.CATEGORY_VIEW, "category" + i);
            }
        }
    }
    
}
