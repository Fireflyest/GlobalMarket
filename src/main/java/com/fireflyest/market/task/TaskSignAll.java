package com.fireflyest.market.task;

import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.RandomUtils;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

/**
 * 全签收
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskSignAll extends Task {

    private final MarketService service;
    private final ViewGuide guide;
    private final MarketEconomy economy;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     */
    public TaskSignAll(@NotNull UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide) {
        super(uid);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() {
        final long[] ids = service.selectDeliveryIdByOwner(uid);
        int num = 0;
        for (long id : ids) {
            num++;
            final boolean refresh = num == ids.length || RandomUtils.randomBoolean();
            this.followTasks().add(
                new TaskSign(uid, service, economy, guide, id, refresh));
        }
    }
}
