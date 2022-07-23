package com.fireflyest.market.command;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.view.AdminView;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.api.ViewGuide;
import org.jetbrains.annotations.NotNull;

public class MarketAdminCommand  implements CommandExecutor {

    private final ViewGuide guide;

    public MarketAdminCommand() {
        this.guide = GlobalMarket.getGuide();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!label.equalsIgnoreCase("marketadmin")
                && !label.equalsIgnoreCase("gma")
                && !label.equalsIgnoreCase("ma")) return true;

//        switch (args.length) {
//            case 1:
//                this.executeCommand(sender, args[0]);
//                break;
//            case 2:
//                this.executeCommand(sender, args[0], args[1]);
//                break;
//            case 3:
//                this.executeCommand(sender, args[0], args[1], args[2]);
//                break;
//            default:
//        }
        this.executeCommand(sender);

        return true;
    }

    private void executeCommand(CommandSender sender){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }

        if(!player.hasPermission("market.admin")){
            player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
            return;
        }

        guide.openView(player, GlobalMarket.ADMIN_VIEW, AdminView.NORMAL);
    }

}
