package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.ComplexCommand;
import org.fireflyest.craftgui.api.ViewGuide;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

public class MarketCommand extends ComplexCommand {

    private final ViewGuide guide;

    public MarketCommand(ViewGuide guide) {
        this.guide = guide;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if (player != null) {
            guide.openView(player, GlobalMarket.MAIN_VIEW, "normal");
        }else {
            sender.sendMessage(Language.PLAYER_COMMAND);
        }
        return true;
    }
    
}
