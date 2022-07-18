package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskDiscount extends Task{

    private final int id;
    private final int num;

    public TaskDiscount(@NotNull String playerName, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;

        this.type = MarketTasks.SALE_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        if (sale.getPrice() == -1){
            this.executeInfo(Language.NOT_SELLING);
            return then;
        }
        if(!sale.getOwner().equalsIgnoreCase(playerName)){
            this.executeInfo(Language.BUY_ERROR);
            return then;
        }
        // 判断是否拍卖物
        if(sale.isAuction()){
            this.executeInfo(Language.TYPE_ERROR);
            return then;
        }
        // 判断打折数值
        if(num >= 10 || num < 0){
            this.executeInfo(Language.COMMAND_ERROR);
            return then;
        }

        sale.setCost(sale.getPrice()*num*0.1);

        MarketManager.updateSale(sale);

        return then;
    }
}
