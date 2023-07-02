package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.TaskHandler;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskHeat;

public class MarketAffairCommand extends SubCommand {

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;
    private final TaskHandler handler;

    public MarketAffairCommand(MarketService service, MarketEconomy economy, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.ERROR_ARGUMENT);
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        guide.openView(player, GlobalMarket.AFFAIR_VIEW, arg1);
        // 加热度
        long id = NumberConversions.toLong(arg1);
        String type = service.selectTransactionType(id);
        if("".equals(type) || "auction".equals(type)){
            return true;
        }
        handler.putTasks(GlobalMarket.TASK_MARKET, new TaskHeat(player.getName(), service, economy, guide, id, 1));
        return true;
    }
    
}
