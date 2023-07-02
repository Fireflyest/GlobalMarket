package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Info;
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

public class TaskHeat extends Task {

    private final long id;
    private final int num;
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    public TaskHeat(@NotNull String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, long id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() {
        String type = service.selectTransactionType(id);

        if("".equals(type)){
            executeInfo(Language.DATA_ERROR);
            return;
        }

        int heat = service.selectTransactionHeat(id) + num;
        String nickname = service.selectTransactionNickname(id);
        
        // 通知参与竞拍的
        if (!"".equals(service.selectTransactionTarget(id)) && "auction".equals(type)) {
            Info info = new Gson().fromJson(service.selectTransactionDesc(id), Info.class);
            Set<String> biders = info.getStrings();
            biders.add(playerName);
            for (String bider : biders) {
                Player pBider = Bukkit.getPlayerExact(bider);
                if (pBider == null) continue;
                pBider.sendMessage(Language.CONFIRM_BID
                        .replace("%item%", nickname)
                        .replace("%num%", String.valueOf(3 - heat)));
            }
        }

        // 热度为0的拍卖，完成竞拍
        if ("auction".equals(type) && heat == 0) {
            this.followTasks().add(new TaskFinish(type, service, economy, guide, id));
            guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
            return;
        }
        
        if (heat <= 0) {
            heat = 0;
        }
        // 更新热度
        service.updateTransactionHeat(heat, id);
    }

}
