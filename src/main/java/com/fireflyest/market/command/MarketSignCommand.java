package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskSign;
import com.fireflyest.market.task.TaskSignAll;

/**
 * 市场签收命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketSignCommand extends SubCommand {
    
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;
    private final TaskHandler handler;

    /**
     * 市场签收命令
     * 
     * @param service 市场服务
     * @param economy 经济服务
     * @param guide 界面导航
     * @param handler 任务处理器
     */
    public MarketSignCommand(MarketService service, MarketEconomy economy, 
            ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        handler.putTasks(
            GlobalMarket.TASK_MAIL, 
            new TaskSignAll(player.getName(), service, economy, guide)
        );
        return true;
    }
 
    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        final int id = NumberConversions.toInt(arg1);
        handler.putTasks(
            GlobalMarket.TASK_MAIL, 
            new TaskSign(player.getName(), service, economy, guide, id)
        );
        return true;
    }

}
