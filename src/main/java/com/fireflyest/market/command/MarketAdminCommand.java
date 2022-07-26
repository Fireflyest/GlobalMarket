package com.fireflyest.market.command;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.task.TaskCancel;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.view.AdminView;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.fireflyest.craftgui.api.ViewGuide;
import org.jetbrains.annotations.NotNull;

public class MarketAdminCommand  implements CommandExecutor {

    private final ViewGuide guide;
    private final MarketTasks.TaskManager taskManager;
    private final GlobalMarket globalMarket;

    public MarketAdminCommand() {
        this.guide = GlobalMarket.getGuide();
        this.taskManager = MarketTasks.getTaskManager();
        this.globalMarket = GlobalMarket.getPlugin();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!label.equalsIgnoreCase("marketadmin")
                && !label.equalsIgnoreCase("gma")
                && !label.equalsIgnoreCase("ma")) return true;

        switch (args.length) {
            case 1:
                this.executeCommand(sender, args[0]);
                break;
            case 2:
                this.executeCommand(sender, args[0], args[1]);
                break;
//            case 3:
//                this.executeCommand(sender, args[0], args[1], args[2]);
//                break;
            default:
                this.executeCommand(sender);
        }

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

    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;

        if(!sender.hasPermission("market.admin")){
            sender.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
            return;
        }

        switch (var1){
            case "reload":
                sender.sendMessage(Language.RELOADING);
                globalMarket.setupData();
                globalMarket.setupGuide();
                sender.sendMessage(Language.RELOADED);
                break;
            case "test":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                for (int i = 0; i < 60; i++) {
                    player.performCommand(String.format( "market sell %d 1", i));
                }
                break;
            case "version":
                sender.sendMessage(Language.TITLE + "The plugin version is " + Language.VERSION);
                break;
            case "update":
                break;
            default:
        }
        
    }

    private void executeCommand(CommandSender sender, String var1, String var2){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }

        if(!player.hasPermission("market.admin")){
            player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
            return;
        }

        switch (var1){
            case "cancel":
                int saleId = ConvertUtils.parseInt(var2);
                Sale sale = MarketManager.getSale(saleId);
                taskManager.putTask(new TaskCancel(sale.getOwner(), saleId));
                break;
            case "black":
                break;
            default:
        }

    }

}
