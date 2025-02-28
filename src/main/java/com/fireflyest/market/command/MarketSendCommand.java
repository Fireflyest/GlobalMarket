package com.fireflyest.market.command;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskSend;

/**
 * 物品邮寄命令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketSendCommand extends SubCommand {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    private final String[] contrabandList;

        
    /**
     * 物品邮寄命令
     * 
     * @param service 市场服务
     * @param handler 任务处理器
     */
    public MarketSendCommand(MarketService service, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.guide = guide;
        this.handler = handler;

        this.contrabandList = Config.CONTRABAND_LORE.get().split(",");
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.COMMAND_ARGUMENT.get());
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.COMMAND_PLAYER.get());
            return false;
        }
        if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
            player.sendMessage(Language.TRANSACTION_NUM.get());
            return true;
        }

        final int amount = player.getInventory().getItemInMainHand().getAmount();

        return this.execute(sender, arg1, String.valueOf(amount));
    }
    
    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        return this.execute(sender, arg1, arg2, null);
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2, String arg3) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.COMMAND_PLAYER.get());
            return false;
        }
        // 是否有权限
        if (!sender.hasPermission("market.send")) {
            sender.sendMessage(
                Language.COMMAND_PERMISSION.get().replace("%perm%", "market.send"));
            return true;
        }
        // 禁止发送给自己
        if (arg1.equals(player.getName()) && !Config.DEBUG.get().booleanValue()) {
            player.sendMessage(Language.COMMAND_ARGUMENT.get());
            return true;
        }

        final String targetUid = service.selectMerchantUid(arg1);
        OfflinePlayer targetPlayer = null;
        if (!"".equals(targetUid)) {
            targetPlayer = Bukkit.getOfflinePlayer(UUID.fromString(targetUid));
        }
        if (targetPlayer == null || !targetPlayer.hasPlayedBefore()) {
            player.sendMessage(Language.COMMAND_ARGUMENT.get());
            return true;
        }

        // 邮箱数量限制
        final int mailAmount = service.selectDeliveryCountByOwner(targetUid);
        if (Config.MAIL_LIMIT.get().booleanValue() && mailAmount > Config.MAIL_MAXIMUM.get()) {
            player.sendMessage(Language.MAIL_MAXIMUM.get());
            return true;
        }

        final int amount = NumberConversions.toInt(arg2);
        if (amount <= 0 || amount > 64) {
            player.sendMessage(Language.COMMAND_ARGUMENT.get());
            return true;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();
        
        // 违禁品判断
        if (this.contraband(item)) {
            sender.sendMessage(Language.TRANSACTION_CONTRABAND.get());
            return true;
        }

        // 判断物品是否足够
        final int has = item.getAmount();
        if (amount > has) {
            player.sendMessage(Language.TRANSACTION_NUM.get());
            return true;
        }
        final ItemStack sendItem = item.clone();
        sendItem.setAmount(amount);
        item.setAmount(has - amount);

        final TaskSend taskSend = new TaskSend(
            player.getName(), 
            service, 
            guide,
            targetUid, 
            sendItem, 
            amount, 
            "item"
        );
        taskSend.setExtra(YamlUtils.serializeItemStack(sendItem));
        taskSend.setInfo(arg3);
        handler.putTasks(GlobalMarket.TASK_MAIL, taskSend);

        return true;
    }

    private boolean contraband(ItemStack item) {
        if (Config.CONTRABAND_ENABLE.get().booleanValue()) {
            final String stack = YamlUtils.serializeItemStack(item);
            for (String lore : contrabandList) {
                if (stack.contains(lore)) {
                    return true;
                }
            }
        }
        return false;
    }

}
