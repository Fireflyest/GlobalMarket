package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.TaskHandler;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskCreate;

public class MarketOrderCommand extends SubCommand {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    public MarketOrderCommand(MarketService service, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        guide.openView(player, GlobalMarket.MAIN_VIEW, "order");
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        String var2 = String.valueOf(player.getInventory().getItemInMainHand().getAmount());
        return this.execute(sender, arg1, var2);
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        return this.execute(sender, arg1, arg2, "coin");
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2, String arg3) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        // 是否有权限
        if (!sender.hasPermission("market.order")) {
            sender.sendMessage(Language.NO_PERMISSION.replace("%permission%", "market.sell"));
            return true;
        }
        double price = NumberConversions.toDouble(arg1);
        int amount = NumberConversions.toInt(arg2);
        if (price <= 0 || price > Config.MAX_PRICE || amount <= 0) {
            sender.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        ItemStack saleItem = item.clone();
        saleItem.setAmount(amount);

        player.sendMessage(Language.TRANSACTION_CREATE);

        TaskCreate taskCreate = new TaskCreate(player.getName(), service, guide, "order", arg3, price, saleItem);
        taskCreate.setDesc(arg2);
        handler.putTasks(GlobalMarket.TASK_MARKET, taskCreate);

        return true;
    }
    
}
