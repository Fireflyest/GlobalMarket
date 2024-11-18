package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import io.fireflyest.emberlib.command.ComplexCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

/**
 * 市场命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketCommand extends ComplexCommand {

    private final ViewGuide guide;

    /**
     * 市场命令
     * 
     * @param guide 界面导航
     */
    public MarketCommand(ViewGuide guide) {
        this.guide = guide;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        guide.openView(player, GlobalMarket.MAIN_VIEW, "normal");
        return true;
    }
    
}
