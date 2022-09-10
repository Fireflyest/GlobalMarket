package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskCategory extends Task{

    private final int id;
    private final String category;

    public TaskCategory(@NotNull String playerName, int id, String category) {
        super(playerName);
        this.id = id;
        this.category = category;

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
            this.executeInfo(Language.CANCEL_ERROR);
            return then;
        }

        sale.setClassify(sale.getClassify() + "," + category);

        this.executeInfo(Language.TITLE + sale.getClassify());

        MarketManager.updateSale(sale);

        return then;
    }
}
