package com.fireflyest.market.task;

import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;

import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.jetbrains.annotations.NotNull;

public class TaskSignAll extends Task{

    private final MarketService service;
    private final ViewGuide guide;
    private final MarketEconomy economy;

    public TaskSignAll(@NotNull String playerName, MarketService service, MarketEconomy economy, ViewGuide guide) {
        super(playerName);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() {
        // 玩家不在线
        if (player == null || !player.isOnline()) {
            return;
        }

        long[] ids = service.selectDeliveryIdByOwner(player.getUniqueId());
        for (long id : ids) {
            this.followTasks().add(new TaskSign(playerName, service, economy, guide, id, false));
        }

        if (this.followTasks().isEmpty()) {
            guide.refreshPage(playerName);
        }
    }
}
