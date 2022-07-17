package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import net.milkbowl.vault.economy.Economy;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskBid extends Task{

    private final int id;
    private final int num;
    private final Economy economy;

    public TaskBid(@NotNull String playerName, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;

        this.economy = GlobalMarket.getEconomy();

        this.type = MarketTasks.SALE_TASK;
    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);

        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        if(!sale.isAuction()){
            this.executeInfo(Language.TYPE_ERROR);
            return then;
        }
        if(sale.getOwner().equals(player.getName())){
            this.executeInfo(Language.BUY_ERROR);
            if (!Config.DEBUG) {
                guide.refreshPage(playerName);
                return then;
            }
        }
        // 拍卖加价
        double cost = sale.getCost()+num;
        if(economy.has(player, cost)){
            sale.setCost(cost);
            // heat每半小时降一次，到0的时候完成竞拍
            sale.setHeat(3);
            sale.setBuyer(player.getName());
        }else {
            this.executeInfo(String.format(Language.NOT_ENOUGH_MONEY, "你"));
        }

        this.executeInfo(Language.AUCTION_ITEM);

        MarketManager.updateSale(sale);

        guide.refreshPage(playerName);
        return then;
    }
}
