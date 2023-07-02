package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.jetbrains.annotations.NotNull;

public class TaskDiscount extends Task {

    private final int id;
    private final int num;
    private final MarketService service;
    private final ViewGuide guide;

    public TaskDiscount(@NotNull String playerName, MarketService service, ViewGuide guide, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;
        this.service = service;
        this.guide = guide;
    }

    @Override
    public void execute() {
        String type = service.selectTransactionType(id);

        if("".equals(type)){
            executeInfo(Language.DATA_ERROR);
            return;
        }
        if (!"retail".equals(type) && !"adminretail".equals(type)){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }

        if(!service.selectTransactionOwnerName(id).equalsIgnoreCase(playerName)){
            this.executeInfo(Language.TRANSACTION_ERROR);
            return;
        }
        // 判断打折数值
        if(num >= 10 || num < 0){
            this.executeInfo(Language.DISCOUNT_ERROR);
            return;
        }

        service.updateTransactionCost(service.selectTransactionPrice(id) * num * 0.1, id);

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
    }
}
