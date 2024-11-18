package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import io.fireflyest.emberlib.command.SubCommand;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;

/**
 * 市场重载命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketReloadCommand extends SubCommand {

    private MarketYaml yaml;

    public MarketReloadCommand(MarketYaml yaml) {
        this.yaml = yaml;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.RELOADING);
        yaml.reloadConfig();
        sender.sendMessage(Language.RELOADED);
        return true;
    }
    
}
