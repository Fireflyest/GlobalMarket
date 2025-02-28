package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 折扣
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskDiscount extends Task {

    private final int id;
    private final int num;
    private final MarketService service;
    private final ViewGuide guide;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param num 打折数值
     */
    public TaskDiscount(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, int id, int num) {
        super(uid);
        this.id = id;
        this.num = num;
        this.service = service;
        this.guide = guide;
    }

    @Override
    public void execute() {
        final String currency = service.selectTransactionCurrency(id);
        final String type = service.selectTransactionType(id);
        final long category = service.selectTransactionCategory(id);

        if (StringUtils.isEmpty(type)) {
            this.info(Language.ERROR_DATA.get());
            return;
        }
        if (!"retail".equals(type) && !"adminretail".equals(type)) {
            this.info(Language.ERROR_TYPE.get());
            return;
        }

        if (!service.selectTransactionOwner(id).equals(uid.toString())) {
            this.info(Language.ERROR_TRANSACTION.get());
            return;
        }
        // 判断打折数值
        if (num >= 10 || num < 0) {
            this.info(Language.ERROR_DISCOUNT.get());
            return;
        }

        service.updateTransactionCost(service.selectTransactionPrice(id) * num * 0.1, id);

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal", type, currency);
        guide.refreshPages(GlobalMarket.MINE_VIEW, uid.toString());
        guide.refreshPages(GlobalMarket.VISIT_VIEW, uid.toString());
        // 根据分类刷新页面，类型是按二进制存储的
        for (int i = 0; i < 8; i++) {
            if ((category & (1 << i)) != 0) {
                guide.refreshPages(GlobalMarket.CATEGORY_VIEW, "category" + i);
            }
        }
    }
}
