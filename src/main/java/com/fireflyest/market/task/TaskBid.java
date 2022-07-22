package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Info;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.google.gson.Gson;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TaskBid extends Task{

    private final int id;
    private final int num;
    private final Economy economy;
    private final PlayerPointsAPI pointsAPI;

    private final Gson gson = new Gson();

    public TaskBid(@NotNull String playerName, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;

        this.economy = GlobalMarket.getEconomy();
        this.pointsAPI = GlobalMarket.getPointsAPI();

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

        boolean hasMoney;
        if (sale.isPoint()){
            User user = MarketManager.getUser(playerName);
            hasMoney = pointsAPI.look(UUID.fromString(user.getUuid())) >= cost;
        }else {
            hasMoney = economy.has(player, cost);
        }

        if (hasMoney){
            sale.setCost(cost);
            // heat每半小时降一次，到0的时候完成竞拍
            sale.setHeat(3);
            sale.setBuyer(player.getName());

            // 添加投标者
            Info info;
            if ("null".equals(sale.getDesc())){
                info = new Info("auction");
            }else {
                info = gson.fromJson(sale.getDesc(), Info.class);
            }
            Set<String> biders = info.getStrings();
            biders.add(playerName);

            for (String bider : biders) {
                Player pBider = Bukkit.getPlayerExact(bider);
                if (pBider == null) continue;
                pBider.sendMessage(Language.AUCTION_BID
                        .replace("%id%", String.valueOf(sale.getId()))
                        .replace("%money%", cost + (sale.isPoint() ? Language.POINT_SYMBOL : Language.COIN_SYMBOL))
                        .replace("%player%", playerName));
            }

            sale.setDesc(gson.toJson(info));

            MarketManager.updateSale(sale);

            this.executeInfo(Language.AUCTION_ITEM);
        }else {
            this.executeInfo(Language.NOT_ENOUGH_MONEY.replace("%target%", ""));
        }

        guide.refreshPage(playerName);
        return then;
    }
}
