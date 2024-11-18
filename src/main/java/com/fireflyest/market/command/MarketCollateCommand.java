package com.fireflyest.market.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.command.SubCommand;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.service.MarketService;

/**
 * 整理交易
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class MarketCollateCommand extends SubCommand {

    private final MarketService service;

    /**
     * 整理交易
     * 
     * @param service 市场服务
     */
    public MarketCollateCommand(MarketService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        for (Transaction transaction : service.selectTransactions(0, 1000)) {
            final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());
            if (item.getType().equals(Material.AIR)) {
                service.deleteTransaction(transaction.getId());
            }
        }
        return true;
    }
    
}
