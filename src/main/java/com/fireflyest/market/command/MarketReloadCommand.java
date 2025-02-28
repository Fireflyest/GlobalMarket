package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import io.fireflyest.emberlib.command.SubCommand;
import com.fireflyest.market.data.Language;

/**
 * 市场重载命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketReloadCommand extends SubCommand {


    public MarketReloadCommand() {
        // Empty
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.CONFIG_RELOADING.get());

        sender.sendMessage(Language.CONFIG_RELOADED.get());
        return true;
    }
    
}
