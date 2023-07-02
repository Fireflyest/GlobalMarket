package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.TaskHandler;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskReprice;

public class MarketRepriceCommand extends SubCommand {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    public MarketRepriceCommand(MarketService service, ViewGuide guide, TaskHandler handler) {
        this.service = service;
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
        guide.openView(player, GlobalMarket.EDIT_VIEW, arg1);
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
        double num = NumberConversions.toDouble(arg2);
        
        handler.putTasks(GlobalMarket.TASK_MARKET, new TaskReprice(player.getName(), service, guide, id, num));
        player.sendMessage(Language.TRANSACTION_REPRICE.replace("%price%", String.valueOf(arg2)));

        return true;
    }

}
