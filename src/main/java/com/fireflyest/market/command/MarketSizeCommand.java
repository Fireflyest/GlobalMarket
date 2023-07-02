package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

public class MarketSizeCommand extends SubCommand {

    private final MarketService service;
    
    public MarketSizeCommand(MarketService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.ERROR_ARGUMENT);
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        sender.sendMessage(Language.TRANSACTION_SIZE.replace("%target%", arg1).replace("%num%", String.valueOf(service.selectMerchantSize(arg1))));
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        String uid = service.selectMerchantUid(arg1);
        if (!"".equals(uid)) {
            service.updateMerchantSize(uid, NumberConversions.toInt(arg2));
            sender.sendMessage(Language.TRANSACTION_SIZE.replace("%target%", arg1).replace("%num%", arg2));
        }
        return true;
    }
    
}
