package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.command.SubCommand;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

/**
 * 市场商店名称更新命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketStoreCommand extends SubCommand {

    private final MarketService service;

    /**
     * 市场商店名称更新命令
     * 
     * @param service 市场服务
     */
    public MarketStoreCommand(MarketService service) {
        this.service = service;
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
        // 是否有权限
        if (!sender.hasPermission("market.store")) {
            sender.sendMessage(Language.NO_PERMISSION.replace("%permission%", "market.store"));
            return true;
        }

        service.updateMerchantStore(player.getUniqueId(), arg1.replace("&", "§"));
        sender.sendMessage(Language.STORE_SUCCEED);
        return true;
    }
    
}
