package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.StateCache;
import com.fireflyest.market.service.MarketService;

/**
 * 市场访问命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketVisitCommand extends SubCommand {
    
    private final MarketService service;
    private final ViewGuide guide;
    private final StateCache cache;

    /**
     * 市场访问命令
     * 
     * @param service 市场服务
     * @param guide 界面导航
     * @param cache 状态缓存
     */
    public MarketVisitCommand(MarketService service, ViewGuide guide, StateCache cache) {
        this.service = service;
        this.guide = guide;
        this.cache = cache;
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
        guide.openView(player, GlobalMarket.VISIT_VIEW, arg1);
        // 访问冷却
        String key = player.getName() + " visit " + arg1;
        if (cache.exist(key)) {
            return true;
        }
        // 访问量
        String uid = service.selectMerchantUid(arg1);
        if (!"".equals(uid)) {
            service.updateMerchantVisit(uid);
            cache.setex(key, 60 * 30, "visit");
        }
        return true;
    }

}
