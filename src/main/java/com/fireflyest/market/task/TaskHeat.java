package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskHeat extends Task{

    private final int id;
    private final int num;

    public TaskHeat(@NotNull String playerName, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;

        this.type = MarketTasks.SALE_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);

        // 判断是否不存在
        if(null == sale){
            return then;
        }

        // 更新热度
        sale.setHeat(sale.getHeat() + num);
        data.update(sale);
        if (!"".equals(sale.getBuyer())){
            // TODO: 2022/7/18 通知参与竞拍的
            this.executeInfo(Language.AUCTION_STEP
                    .replace("%id%", String.valueOf(sale.getId()))
                    .replace("%money%", String.valueOf(sale.getCost()))
                    .replace("%num%", String.valueOf(3 - sale.getHeat())));
        }

        // 热度为0的拍卖，完成竞拍
        if (sale.getHeat() == 0){
            then.add(new TaskFinish(playerName, id));
        }

        return then;
    }
}
