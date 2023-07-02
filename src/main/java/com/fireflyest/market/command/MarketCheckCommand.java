package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.util.Latest;
import org.fireflyest.util.NetworkUtils;
import org.fireflyest.util.StringUtils;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;

public class MarketCheckCommand extends SubCommand {

    public MarketCheckCommand() {
        //
    }

    @Override
    protected boolean execute(CommandSender sender) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String url = "https://github.com/Fireflyest/GlobalMarket";
                String version = GlobalMarket.getPlugin().getDescription().getVersion().replace("-beta", "");
                Latest latest = NetworkUtils.doGet("https://api.github.com/repos/Fireflyest/GlobalMarket/releases/latest", Latest.class);
                if (latest != null) {
                    if (StringUtils.compareVersion(latest.getName().substring(1), version) == 1) {
                        version = version.concat(" -> ").concat(latest.getName());
                        url = latest.getAssets().get(0).getBrowserDownloadUrl();
                    }
                    sender.sendMessage(Language.CHECK_VERSION.replace("%version%", version).replace("%url%", url));
                }
            }
        }.runTaskAsynchronously(GlobalMarket.getPlugin());
        return true;
    }
    
}
