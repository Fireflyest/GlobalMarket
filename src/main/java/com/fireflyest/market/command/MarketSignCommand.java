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
import com.fireflyest.market.task.TaskSign;
import com.fireflyest.market.task.TaskSignAll;

public class MarketSignCommand extends SubCommand {
    
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;
    private final TaskHandler handler;

    public MarketSignCommand(MarketService service, MarketEconomy economy, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        handler.putTasks(GlobalMarket.TASK_MAIL, new TaskSignAll(player.getName(), service, economy, guide));
        return true;
    }
 
    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        int id = NumberConversions.toInt(arg1);
        handler.putTasks(GlobalMarket.TASK_MAIL, new TaskSign(player.getName(), service, economy, guide, id));
        return true;
    }

}
