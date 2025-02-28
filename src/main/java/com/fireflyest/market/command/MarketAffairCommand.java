package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.cache.CacheOrganism;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskHeat;

/**
 * 交易详情
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketAffairCommand extends SubCommand {

    private final MarketService service;
    private final ViewGuide guide;
    private final CacheOrganism cache;
    private final TaskHandler handler;

    /**
     * 构造交易详情指令
     * 
     * @param service 市场服务
     * @param guide 界面导航
     * @param cache 状态缓存
     * @param handler 任务处理器
     */
    public MarketAffairCommand(MarketService service, 
            ViewGuide guide, CacheOrganism cache, TaskHandler handler) {
        this.service = service;
        this.guide = guide;
        this.cache = cache;
        this.handler = handler;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.COMMAND_ARGUMENT.get());
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.COMMAND_PLAYER.get());
            return false;
        }
        guide.openView(player, GlobalMarket.AFFAIR_VIEW, arg1);
        // 加热度
        final long id = NumberConversions.toLong(arg1);

        // 访问冷却
        final String key = player.getName() + " affair " + arg1;
        if (cache.exist(key)) {
            return true;
        }
        // 访问商品热度
        cache.setex(key, 60 * 30 * 1000, "affair");
        handler.putTasks(
            GlobalMarket.TASK_MARKET, 
            new TaskHeat(player.getUniqueId(), service, guide, id, 1, false)
        );
        return true;
    }
    
}
