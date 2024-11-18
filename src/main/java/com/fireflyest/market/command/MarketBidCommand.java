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
import com.fireflyest.market.task.TaskBid;

/**
 * 对拍卖商品出价
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketBidCommand extends SubCommand {

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;
    private final TaskHandler handler;

    /**
     * 构造出价指令
     * 
     * @param service 市场服务
     * @param economy 市场经济
     * @param guide 界面导航
     * @param handler 任务处理器
     */
    public MarketBidCommand(MarketService service, MarketEconomy economy, 
            ViewGuide guide, TaskHandler handler) {
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
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        final int id = NumberConversions.toInt(arg1);
        final int num = NumberConversions.toInt(arg2);
        if (num <= 0) {
            player.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }
        handler.putTasks(
            GlobalMarket.TASK_MARKET, 
            new TaskBid(player.getName(), service, economy, guide, id, num)
        );
        return true;
    }
    
}
