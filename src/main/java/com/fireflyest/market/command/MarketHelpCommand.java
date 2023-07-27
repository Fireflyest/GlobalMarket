package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.fireflyest.craftcommand.command.SubCommand;

import com.fireflyest.market.data.Language;

public class MarketHelpCommand extends SubCommand {

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.HELP);
        return true;
    }
    
}
