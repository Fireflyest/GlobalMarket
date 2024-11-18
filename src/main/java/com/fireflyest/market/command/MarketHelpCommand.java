package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import io.fireflyest.emberlib.command.SubCommand;
import com.fireflyest.market.data.Language;

/**
 * 市场帮助命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketHelpCommand extends SubCommand {

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.HELP);
        return true;
    }
    
}
