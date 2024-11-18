package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

/**
 * 市场邮件命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketMailCommand extends SubCommand {

    private final ViewGuide guide;

    public MarketMailCommand(ViewGuide guide) {
        this.guide = guide;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        final String uid = player.getUniqueId().toString();
        guide.openView(player, GlobalMarket.MAIL_VIEW, uid);
        return true;
    }
    
}
