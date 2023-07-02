package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
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

    public TaskAdmin(@NotNull String playerName, MarketService service, ViewGuide guide, int id) {
        super(playerName);
        this.service = service;
        this.guide = guide;
        this.id = id;
    }

    @Override
    public void execute() {
        String type = service.selectTransactionType(id);

        if("".equals(type)){
            executeInfo(Language.DATA_ERROR);
            return;
        }
        if (!"retail".equals(type) && !"order".equals(type)){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }
        service.updateTransactionType("admin" + type, id);
        this.executeInfo(Language.TRANSACTION_EDIT);

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
    }
}
