package com.fireflyest.market.command;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.fireflyest.craftcommand.command.SubCommand;
import org.fireflyest.util.SerializationUtil;

import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.service.MarketService;

public class MarketCollateCommand extends SubCommand {

    private final MarketService service;

    public MarketCollateCommand(MarketService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        for (Transaction transaction : service.selectTransactions(0, 1000)) {
            if (SerializationUtil.deserializeItemStack(transaction.getStack()).getType().equals(Material.AIR)) {
                service.deleteTransaction(transaction.getId());
            }
        }
        return true;
    }
    
}
