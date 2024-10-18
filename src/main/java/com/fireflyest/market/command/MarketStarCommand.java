package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.StateCache;
import com.fireflyest.market.service.MarketService;

public class MarketStarCommand extends SubCommand {

    private final MarketService service;
    private final StateCache cache;

    public MarketStarCommand(MarketService service, StateCache cache) {
        this.service = service;
        this.cache = cache;
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
        // 点赞冷却
        String key = player.getName() + " star " + arg1;
        if (cache.exist(key)) {
            sender.sendMessage(Language.STAR_ERROR);
            return true;
        }
        // 点赞量
        String uid = service.selectMerchantUid(arg1);
        if (uid != null && !"".equals(uid)) {
            service.updateMerchantStar(uid);
            cache.setex(key, 60 * 30, "star");
            sender.sendMessage(Language.STAR_SUCCEED);
        }
        return true;
    }
    
}
