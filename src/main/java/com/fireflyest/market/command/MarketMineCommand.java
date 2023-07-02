package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

public class MarketMineCommand extends SubCommand {

    private final ViewGuide guide;

    public MarketMineCommand(ViewGuide guide) {
        this.guide = guide;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        String playerName = player.getName();
        guide.openView(player, GlobalMarket.MINE_VIEW, playerName);
        return true;
    }
    
}
