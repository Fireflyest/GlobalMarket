package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

/**
 * 市场商品编辑命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketEditCommand extends SubCommand {

    private final ViewGuide guide;

    /**
     * 市场商品编辑命令
     * 
     * @param guide 界面导航
     */
    public MarketEditCommand(ViewGuide guide) {
        this.guide = guide;
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
    
}
