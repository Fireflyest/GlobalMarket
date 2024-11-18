package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.TaskHandler;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskCreate;

/**
 * 上架拍卖商品指令
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketAuctionCommand extends SubCommand {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    /**
     * 构造拍卖指令
     * 
     * @param service 市场服务
     * @param guide 界面导航
     * @param handler 任务处理器
     */
    public MarketAuctionCommand(MarketService service, ViewGuide guide, TaskHandler handler) {
        this.service = service;
        this.guide = guide;
        this.handler = handler;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        guide.openView(player, GlobalMarket.MAIN_VIEW, "auction");
        return true;
    }
    
    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        final String var2 = String.valueOf(player.getInventory().getItemInMainHand().getAmount());
        return this.execute(sender, arg1, var2);
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        return this.execute(sender, arg1, arg2, "coin");
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2, String arg3) {
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return false;
        }
        // 是否有权限
        if (!sender.hasPermission("market.auction")) {
            sender.sendMessage(Language.NO_PERMISSION.replace("%permission%", "market.auction"));
            return true;
        }
        final double price = NumberConversions.toDouble(arg1);
        final int amount = NumberConversions.toInt(arg2);
        if (price <= 0 || price > Config.MAX_PRICE || amount <= 0 || amount > 64) {
            sender.sendMessage(Language.ERROR_ARGUMENT);
            return true;
        }

        final ItemStack item = player.getInventory().getItemInMainHand();
        
        // 判断物品是否足够
        final int has = item.getAmount();
        if (amount > has) {
            player.sendMessage(Language.TRANSACTION_NUM);
            return true;
        }

        final ItemStack saleItem = item.clone();
        saleItem.setAmount(amount);
        item.setAmount(has - amount);

        player.sendMessage(Language.TRANSACTION_CREATE);

        handler.putTasks(
            GlobalMarket.TASK_MARKET, 
            new TaskCreate(player.getName(), service, guide, "auction", arg3, price, saleItem)
        );

        return true;
    }

}
