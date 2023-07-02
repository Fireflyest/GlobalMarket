package com.fireflyest.market.command;

import org.bukkit.command.CommandSender;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.command.SubCommand;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

public class MarketBlackCommand extends SubCommand {

    private final MarketService service;

    public MarketBlackCommand(MarketService service) {
        this.service = service;
    }

    @Override
    protected boolean execute(CommandSender sender) {
        sender.sendMessage(Language.ERROR_ARGUMENT);
        return true;
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1) {
        return this.execute(sender, arg1, "1");
    }

    @Override
    protected boolean execute(CommandSender sender, String arg1, String arg2) {
        int black = NumberConversions.toInt(arg2);
        String uid = service.selectMerchantUid(arg1);
        if (!"".equals(uid)) {
            service.updateMerchantBlack(uid, black);
            sender.sendMessage(Language.MERCHANT_BLACK);
        }
        return true;
    }
    
}
