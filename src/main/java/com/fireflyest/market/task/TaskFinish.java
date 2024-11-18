package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.craftitem.builder.ItemBuilder;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.util.SerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TaskFinish extends Task {

    private final long id;

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    public TaskFinish(@NotNull String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, long id) {
        super(playerName);
        this.id = id;
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() {
        Transaction transaction = service.selectTransactionById(id);

        if(null == transaction){
            this.executeInfo(Language.DATA_ERROR);
            return;
        }
        if(!"auction".equals(transaction.getType())){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }
        if(transaction.getOwnerName().equals(playerName)){
            this.executeInfo(Language.TRANSACTION_ERROR);
            if (!Config.DEBUG) {
                guide.refreshPage(playerName);
                return;
            }
        }

        if("".equalsIgnoreCase(transaction.getTarget())){
            // 流拍
            this.executeInfo(Language.AUCTION_FLOW.replace("%item%", transaction.getNickname()));
            this.followTasks().add(new TaskCancel(playerName, service, guide, id));
            return;
        }

        boolean hasMoney = false;
        String symbol = "";
        String targetUid = service.selectMerchantUid(transaction.getTarget());
        OfflinePlayer buyer = Bukkit.getOfflinePlayer(UUID.fromString(targetUid));
        switch(transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(buyer, transaction.getCost());
                symbol = Language.COIN_SYMBOL;
                break;
            case "point":
                hasMoney = economy.gPlayerPointsAPI().look(buyer.getUniqueId()) >= transaction.getCost();
                symbol = Language.POINT_SYMBOL;
                break;
            case "item":
            default:
                break;
        }

        if (!hasMoney) {
            this.executeInfo(Language.TRANSACTION_FAIL.replace("%target%", transaction.getTarget()));
            service.updateMerchantCredit("-1", targetUid);
            this.followTasks().add(new TaskCancel(playerName, service, guide, id));
            return;
        }

        // 买家扣钱
        switch(transaction.getCurrency()) {
            case "coin":
                economy.getEconomy().withdrawPlayer(buyer, transaction.getCost());
                break;
            case "point":
                economy.gPlayerPointsAPI().take(buyer.getUniqueId(),  (int)Math.ceil(transaction.getCost()));
                break;
            case "item":
            default:
                break;
        }

        // 发送物品
        ItemStack stack = SerializationUtil.deserializeItemStack(transaction.getStack());
        this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_AUCTION, service, targetUid, stack));

        // 提示买家
        if (buyer.isOnline()){
            ((Player) buyer).sendMessage(Language.TRANSACTION_SUCCEED);
        }

        // 删除交易
        service.deleteTransaction(id);
        // 刷新交易界面
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));

        // 发送给卖家
        ItemStack recordBook = this.getRecordItem(transaction.getNickname(), targetUid, transaction.getCost(), symbol);
        this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_REWARD, service, targetUid, recordBook, transaction.getCost(), transaction.getCurrency(), transaction.getExtras()));
        this.executeInfo(Language.AUCTION_FINISH.replace("%item%", transaction.getNickname()));

        // 更新卖家统计数据
        service.updateMerchantSelling("-1", transaction.getOwner());
        service.updateMerchantAmount(transaction.getOwner());
    }

    @NotNull
    public ItemStack getRecordItem(String itemName, String buyer, double cost, String symbol) {
        return new ItemBuilder("WRITTEN_BOOK")
                .name(Language.GUI_MARKET_RECORD)
                .lore(String.format(Language.GUI_TRANSACTION_ITEM, itemName))
                .lore(String.format(Language.GUI_BUYER, buyer))
                .lore(String.format(Language.GUI_REWARD,  cost, symbol))
                .build();
    }

}
