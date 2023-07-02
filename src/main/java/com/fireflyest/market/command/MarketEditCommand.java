package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

public class MarketEditCommand extends SubCommand {

    private final ViewGuide guide;

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
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        guide.openView(player, GlobalMarket.EDIT_VIEW, arg1);
        return true;
    }
    
}
