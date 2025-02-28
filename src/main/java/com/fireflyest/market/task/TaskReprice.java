package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 重新定价
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TaskReprice extends Task {

    private final int id;

    private double price;
    private final MarketService service;
    private final ViewGuide guide;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param price 价格
     */
    public TaskReprice(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, int id, double price) {
        super(uid);
        this.id = id;
        this.price = price;
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
        if ("auction".equals(type) || "trade".equals(type)) {
            this.info(Language.ERROR_TYPE.get());
            return;
        }

        if (!service.selectTransactionOwner(id).equals(uid.toString())) {
            this.info(Language.ERROR_TRANSACTION.get());
            return;
        }

        if (price < 0 || price > Config.PRICE_MAX.get()) {
            this.info(Language.ERROR_REPRICE.get());
            return;
        }

        if (!"coin".equals(service.selectTransactionCurrency(id))) {
            price = (int) price;
        }
        if (service.selectTransactionPrice(id) == 0) {
            service.updateTransactionPrice(price, id);
        }
        service.updateTransactionCost(price, id);

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
