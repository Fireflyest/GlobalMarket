package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskEdit extends Task{

    private final int id;
    private final boolean auction;
    private final boolean point;
    private final double price;

    public TaskEdit(@NotNull String playerName, int id, boolean auction, boolean point, double price) {
        super(playerName);
        this.id = id;
        this.auction = auction;
        this.point = point;
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

        if (sale.getPrice() == -1) {
            sale.setPrice(price);
            sale.setAuction(auction);
            sale.setPoint(point);

            if(Config.SELL_BROADCAST){
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ChatUtils.sendItemButton(onlinePlayer, SerializeUtil.deserialize(sale.getStack(), sale.getMeta()), String.format("/market affair %s", sale.getId()), playerName);
                }
            }
        }
        sale.setCost(price);

        MarketManager.updateSale(sale);

        return then;
    }
}
