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
 * set sale admin
 *
 * @author Fireflyest
 * @since 3.3
 */
public class TaskAdmin extends Task {

    private final MarketService service;
    private final ViewGuide guide;
    private final int id;

    /**
     * 构造任务
     *
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     */
    public TaskAdmin(@NotNull UUID uid, MarketService service, ViewGuide guide, int id) {
        super(uid);
        this.service = service;
        this.guide = guide;
        this.id = id;
    }

    @Override
    public void execute() {
        final String currency = service.selectTransactionCurrency(id);
        final String type = service.selectTransactionType(id);

        if (StringUtils.isEmpty(type)) {
            this.info(Language.ERROR_DATA.get());
            return;
        }
        if (!"retail".equals(type) && !"order".equals(type)) {
            this.info(Language.ERROR_TYPE.get());
            return;
        }
        service.updateTransactionType("admin" + type, id);
        this.info(Language.TRANSACTION_EDIT.get());

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.CATEGORY_VIEW);
        guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal", type, currency);
        guide.refreshPages(GlobalMarket.MINE_VIEW, uid.toString());
        guide.refreshPages(GlobalMarket.STORE_VIEW);
        guide.refreshPages(GlobalMarket.VISIT_VIEW, uid.toString());
    }
}
