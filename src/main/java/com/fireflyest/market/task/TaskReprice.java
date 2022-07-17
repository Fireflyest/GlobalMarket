package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskReprice extends Task{

    private final int id;

    private final double price;

    public TaskReprice(@NotNull String playerName, int id, double price) {
        super(playerName);
        this.id = id;
        this.price = price;

        this.type = MarketTasks.SALE_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        if(!sale.getOwner().equalsIgnoreCase(playerName)){
            this.executeInfo(Language.BUY_ERROR);
            return then;
        }

        if(price < 0 || price > Config.MAX_PRICE){
            this.executeInfo(Language.COMMAND_ERROR);
            guide.refreshPage(playerName);
            return then;
        }

        sale.setCost(price);

        MarketManager.updateSale(sale);
        guide.refreshPage(playerName);
        return then;
    }
}
