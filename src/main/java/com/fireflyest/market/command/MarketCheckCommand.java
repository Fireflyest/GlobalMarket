package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.data.Latest;
import io.fireflyest.emberlib.util.UpdateUtils;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

/**
 * 检查更新
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketCheckCommand extends SubCommand {

    /**
     * 检查更新
     */
    public MarketCheckCommand() {
        //
    }

    @Override
    protected boolean execute(CommandSender sender) {
        final String version = 
            GlobalMarket.getPlugin().getDescription().getVersion().replace("-beta", "");
        final Latest latest = UpdateUtils.doGet(
            "https://api.github.com/repos/Fireflyest/GlobalMarket/releases/latest", 
            Latest.class
        );
        if (latest != null) {
            String url = null;
            if (UpdateUtils.compareVersion(latest.getName().substring(1), version) == 1) {
                url = latest.getAssets().get(0).getBrowserDownloadUrl();
            }
            sender.sendMessage(
                Language.CHECK_VERSION.replace("%version%", version + " -> " + latest.getName())
                                      .replace("%url%", url)
            );
        }
        return true;
    }
    
}
