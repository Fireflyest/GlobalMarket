package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskReprice;

/**
 * 市场商品改价命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketRepriceCommand extends SubCommand {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    /**
     * 市场商品改价命令
     * 
     * @param service 市场服务
     * @param guide 界面导航
     * @param handler 任务处理器
     */
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
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        guide.openView(player, GlobalMarket.EDIT_VIEW, arg1);
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        final int id = NumberConversions.toInt(arg1);
        final double num = NumberConversions.toDouble(arg2);
        
        handler.putTasks(
            GlobalMarket.TASK_MARKET, 
            new TaskReprice(player.getName(), service, guide, id, num)
        );
        player.sendMessage(Language.TRANSACTION_REPRICE.replace("%price%", String.valueOf(arg2)));

        return true;
    }

}
