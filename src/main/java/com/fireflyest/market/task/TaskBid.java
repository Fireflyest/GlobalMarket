package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Info;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class TaskBid extends Task{

    private final int id;
    private final int num;
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    private final Gson gson = new Gson();

    public TaskBid(@NotNull String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, int id, int num) {
        super(playerName);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.id = id;
        this.num = num;
    }

    @Override
    public void execute() {
        Transaction transaction = service.selectTransactionById(id);

        if(null == transaction){
            this.executeInfo(Language.DATA_ERROR);
            return;
        }
        if(!"auction".equals(transaction.getType())){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }
        if(transaction.getOwnerName().equals(playerName)){
            this.executeInfo(Language.TRANSACTION_ERROR);
            if (!Config.DEBUG) {
                guide.refreshPage(playerName);
                return;
            }
        }
        // 拍卖加价
        double cost = transaction.getCost() + num;
        boolean hasMoney = false;
        String symbol = "";
        switch(transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(player, cost);
                symbol = Language.COIN_SYMBOL;
                break;
            case "point":
                hasMoney = economy.gPlayerPointsAPI().look(player.getUniqueId()) >= cost;
                symbol = Language.POINT_SYMBOL;
                break;
            case "item":
            default:
                break;
        }

        if (!hasMoney) {
            this.executeInfo(Language.TRANSACTION_FAIL.replace("%target%", ""));
            guide.refreshPage(playerName);
            return;
        }

        service.updateTransactionCost(cost, id);
        service.updateTransactionHeat(3, id);
        service.updateTransactionTarget(playerName, id);

        // 添加投标者
        Info info;
        if ("".equals(transaction.getDesc())){
            info = new Info("auction");
        }else {
            info = gson.fromJson(transaction.getDesc(), Info.class);
        }
        Set<String> biders = info.getStrings();
        biders.add(playerName);

        for (String bider : biders) {
            Player pBider = Bukkit.getPlayerExact(bider);
            if (pBider == null) continue;
            pBider.sendMessage(Language.REMIND_BID
                    .replace("%id%", String.valueOf(id))
                    .replace("%money%", cost + symbol)
                    .replace("%player%", playerName));
        }

        service.updateTransactionDesc(gson.toJson(info), id);

        this.executeInfo(Language.SUCCEED_BID);

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));

    }
}
