package com.fireflyest.market.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.util.SerializationUtil;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskSend;

public class MarketSendCommand extends SubCommand {

    private final MarketService service;
    private final TaskHandler handler;

	public MarketSendCommand(MarketService service, TaskHandler handler) {
        this.service = service;
        this.handler = handler;
    }

    @Override
	protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.ERROR_ARGUMENT);
        return true;
	}

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        if(player.getInventory().getItemInMainHand().getType().equals(Material.AIR)){
            player.sendMessage(Language.TRANSACTION_NUM);
            return true;
        }
        return this.execute(sender, arg1, String.valueOf(player.getInventory().getItemInMainHand().getAmount()));
    }
    
    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        return this.execute(sender, arg1, arg2, null);
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2, String arg3) {
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }  
        // 是否有权限
        if (!sender.hasPermission("market.send")) {
            sender.sendMessage(Language.NO_PERMISSION.replace("%permission%", "market.send"));
            return true;
        }
        // 禁止发送给自己
        if (arg1.equals(player.getName()) && !Config.DEBUG) {
            player.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }

        String targetUid = service.selectMerchantUid(arg1);
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(UUID.fromString(targetUid));
        if (!targetPlayer.hasPlayedBefore()) {
            player.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }

        // 邮箱数量限制
        if (Config.MAXIMUM_MAIL && service.selectDeliveryIdByOwner(targetPlayer.getUniqueId()).length > Config.MAXIMUM_MAIL_NUM) {
            player.sendMessage(Language.MAXIMUM_MAIL);
            return true;
        }

        int amount = NumberConversions.toInt(arg2);
        if (amount <= 0 || amount > 64) {
            player.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        
        // 违禁品判断
        if (Config.CONTRABAND_LORE) {
            String stack = SerializationUtil.serializeItemStack(item);
            for (String lore : Config.CONTRABAND_LORE_LIST.split(",")) {
                if (stack.contains(lore)) {
                    sender.sendMessage(Language.TRANSACTION_CONTRABAND);
                    return true;
                }
            }
        }

        // 判断物品是否足够
        int has = item.getAmount();
        if (amount > has) {
            player.sendMessage(Language.TRANSACTION_NUM);
            return true;
        }
        ItemStack sendItem = item.clone();
        sendItem.setAmount(amount);
        item.setAmount(has - amount);

        TaskSend taskSend = new TaskSend(player.getName(), service, targetUid, sendItem, amount, "item", SerializationUtil.serializeItemStack(sendItem));
        taskSend.setInfo(arg3);
        handler.putTasks(GlobalMarket.TASK_MAIL, taskSend);

        return true;
    }

}
