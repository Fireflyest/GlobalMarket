package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.TaskHandler;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskBuy;

public class MarketBuyCommand extends SubCommand {

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;
    private final TaskHandler handler;

    public MarketBuyCommand(MarketService service, MarketEconomy economy, ViewGuide guide, TaskHandler handler) {
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
        sender.sendMessage(Language.ERROR_ARGUMENT);
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        int id = NumberConversions.toInt(arg1);
        int num = NumberConversions.toInt(arg2);
        if (num < 0) {
            player.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }
        if(!Config.BUY_PARTIAL && num != 0){
            player.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }
        handler.putTasks(GlobalMarket.TASK_MARKET, new TaskBuy(player.getName(), service, economy, guide, id, num));
        return true;
    }
    
}
