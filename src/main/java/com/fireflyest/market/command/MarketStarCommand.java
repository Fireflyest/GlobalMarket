package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.cache.CacheOrganism;
import io.fireflyest.emberlib.command.SubCommand;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

/**
 * 市场点赞命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketStarCommand extends SubCommand {

    private final MarketService service;
    private final CacheOrganism cache;

    /**
     * 市场点赞命令
     * 
     * @param service 市场服务
     * @param cache 缓存服务 
     */
    public MarketStarCommand(MarketService service, CacheOrganism cache) {
        this.service = service;
        this.cache = cache;
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
        // 点赞冷却
        final String key = player.getName() + " star " + arg1;
        if (cache.exist(key)) {
            sender.sendMessage(Language.ERROR_STAR.get());
            return true;
        }
        // 点赞量
        final String uid = service.selectMerchantUid(arg1);
        if (uid != null && !"".equals(uid)) {
            service.updateMerchantStar(uid);
            cache.setex(key, 60 * 30 * 1000, "star");
            sender.sendMessage(Language.SUCCEED_STAR.get());
        }
        return true;
    }
    
}
