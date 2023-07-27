package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.util.ChatUtils;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.util.SerializationUtil;
import org.jetbrains.annotations.NotNull;

public class TaskAffirm extends Task {

    private final long id;
    private final String type;
    private final String currency;
    private final String extra;
    private final MarketService service;
    private final ViewGuide guide;
    private final boolean updateCurrency;

    public TaskAffirm(@NotNull String playerName, MarketService service, ViewGuide guide, long id, String type, String currency, String extra) {
        super(playerName);
        this.id = id;
        this.type = type;
        this.currency = currency;
        this.extra = extra;
        this.service = service;
        this.guide = guide;
        this.updateCurrency = false;
    }

    public TaskAffirm(@NotNull String playerName, MarketService service, ViewGuide guide, long id, String extra) {
        super(playerName);
        this.id = id;
        this.type = null;
        this.currency = null;
        this.extra = extra;
        this.service = service;
        this.guide = guide;
        this.updateCurrency = true;
    }

    @Override
    public void execute() {
        String transactionType = service.selectTransactionType(id);

        if("".equals(transactionType)){
            executeInfo(Language.DATA_ERROR);
            return;
        }
        if (!"prepare".equals(transactionType) && !updateCurrency){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }

        if(!service.selectTransactionOwnerName(id).equalsIgnoreCase(playerName)){
            this.executeInfo(Language.TRANSACTION_ERROR);
            return;
        }

        if (type != null) {
            service.updateTransactionType(type, id);
        }
        if (currency != null) {
            service.updateTransactionCurrency(currency, id);
        }
        if (extra != null) {
            service.updateTransactionExtras(extra, id);
        }

        if(Config.ACTION_BROADCAST && !updateCurrency){
            ChatUtils.sendItemButton(SerializationUtil.deserializeItemStack(service.selectTransactionStack(id)), 
                    String.format("/market affair %s", id), 
                    playerName);
        }
        
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPage(playerName);
        
    }
}
