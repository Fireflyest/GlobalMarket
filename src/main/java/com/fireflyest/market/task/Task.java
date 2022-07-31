package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.api.ViewGuide;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Task {

    // 产生的新任务
    protected final List<Task> then = new ArrayList<>();

    // GUI
    protected final ViewGuide guide;
    // 数据
    protected final Data data;

    // 触发该任务执行的玩家
    protected final String playerName;
    protected final Player player;

    protected int type = MarketTasks.SALE_TASK;

    public Task(@NotNull String playerName){
        this.playerName = playerName;
        this.player = Bukkit.getPlayerExact(playerName);

        this.data = GlobalMarket.getData();
        this.guide = GlobalMarket.getGuide();
    }

    @NotNull
    public abstract List<Task> execute() throws Exception;

    public void executeInfo(@NotNull String info){
        if (player == null) return;
        player.sendMessage(info);
    }

    public int getType() {
        return type;
    }
}
