package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import org.jetbrains.annotations.NotNull;

public class TaskReprice extends Task {

    private final int id;

    private double price;
    private final MarketService service;
    private final ViewGuide guide;

    public TaskReprice(@NotNull String playerName, MarketService service, ViewGuide guide, int id, double price) {
        super(playerName);
        this.id = id;
        this.price = price;
        this.service = service;
        this.guide = guide;
    }

    @Override
    public void execute() {
        String transactionType = service.selectTransactionType(id);

        if("".equals(transactionType)){
            executeInfo(Language.DATA_ERROR);
            return;
        }
        if ("auction".equals(transactionType) || "trade".equals(transactionType)){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }

        if(!service.selectTransactionOwnerName(id).equalsIgnoreCase(playerName)){
            this.executeInfo(Language.TRANSACTION_ERROR);
            return;
        }

        if(price < 0 || price > Config.MAX_PRICE){
            this.executeInfo(Language.REPRICE_ERROR);
            guide.refreshPage(playerName);
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
    }
}
