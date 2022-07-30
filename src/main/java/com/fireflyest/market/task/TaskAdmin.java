package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * set sale admin
 *
 * @author Fireflyest
 * @since 2022/7/30
 */
public class TaskAdmin extends Task{

    private final int id;

    public TaskAdmin(@NotNull String playerName, int id) {
        super(playerName);
        this.id = id;

        this.type = MarketTasks.SALE_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        if (sale.isAuction()){
            this.executeInfo(Language.TYPE_ERROR);
            return then;
        }
        sale.setAdmin(!sale.isAdmin());
        data.update(sale);
        this.executeInfo(Language.UNLIMITED_ITEM.replace("%unlimited%",  String.valueOf(sale.isAdmin())));
        return then;
    }
}
