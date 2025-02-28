package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import org.apache.commons.lang.StringUtils;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * 热度
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskHeat extends Task {

    private final long id;
    private final int num;
    private final MarketService service;
    private final ViewGuide guide;
    private final boolean refresh;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param num 热度
     * @param refresh 是否刷新
     */
    public TaskHeat(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, long id, int num, boolean refresh) {
        super(uid);
        this.id = id;
        this.num = num;
        this.service = service;
        this.guide = guide;
        this.refresh = refresh;
    }

    @Override
    public void execute() {
        final String type = service.selectTransactionType(id);

        if (StringUtils.isEmpty(type)) {
            this.info(Language.ERROR_DATA.get());
            return;
        }

        final int heat = service.selectTransactionHeat(id) + num;

        // 热度不降为0
        if (heat > 0) {
            service.updateTransactionHeat(heat, id);

            guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
            guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        }

        if (refresh) {
            guide.refreshPages(GlobalMarket.MAIN_VIEW);
            guide.refreshPages(GlobalMarket.CATEGORY_VIEW);
            guide.refreshPages(GlobalMarket.MINE_VIEW);
            guide.refreshPages(GlobalMarket.VISIT_VIEW);
        }
    }

}
