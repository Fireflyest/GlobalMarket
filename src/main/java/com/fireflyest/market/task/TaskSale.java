package com.fireflyest.market.task;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.crafttask.exception.ExecuteException;
import org.fireflyest.util.SerializationUtil;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;

public class TaskSale extends Task {

    private final long id;
    private final int num;
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    public TaskSale(String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, long id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() throws ExecuteException {
        Transaction transaction = service.selectTransactionById(id);

        if(null == transaction){
            this.executeInfo(Language.DATA_ERROR);
            return;
        }
        if(!"order".equals(transaction.getType()) && !"adminorder".equals(transaction.getType())){
            this.executeInfo(Language.TYPE_ERROR);
            return;
        }
        // 自己购买自己
        if(transaction.getOwnerName().equals(playerName)){
            this.executeInfo(Language.TRANSACTION_ERROR);
            if (!Config.DEBUG) {
                guide.refreshPage(playerName);
                return;
            }
        }

        // 物品货币未设置物品
        if ("item".equals(transaction.getCurrency()) && "".equals(transaction.getExtras())) {
            this.executeInfo(Language.CURRENCY_ERROR);
            return;
        }

        if (player == null || !player.isOnline()) {
            return;
        }

        int amount = NumberConversions.toInt(transaction.getDesc());
        double price;
        double cost;
        boolean hasMoney = false;
        boolean admin = transaction.getType().startsWith("admin");
        boolean buyAll = num == 0;
        String symbol = "";
        String itemName = transaction.getNickname();
        ItemStack item = SerializationUtil.deserializeItemStack(transaction.getStack());
        ItemStack currencyItem = null;      
        int saleAmount = buyAll ?  item.getAmount() : num;

        // 点券和物品交易不允许零散购买
        if (!buyAll && !"coin".equals(transaction.getCurrency())) {
            this.executeInfo(Language.AMOUNT_ERROR);
            return;
        }

        // 计算价格
        if (buyAll){
            price = transaction.getPrice();
            cost = transaction.getCost();
        }else {
            price = transaction.getPrice() / item.getAmount() * num;
            cost = transaction.getCost() / item.getAmount() * num;
        }

        // 判断收购方钱是否足够
        OfflinePlayer ownerPlayer = Bukkit.getOfflinePlayer(UUID.fromString(transaction.getOwner()));
        switch(transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(ownerPlayer, cost);
                symbol = Language.COIN_SYMBOL;
                break;
            case "point":
                hasMoney = economy.gPlayerPointsAPI().look(ownerPlayer.getUniqueId()) >= cost;
                symbol = Language.POINT_SYMBOL;
                break;
            case "item":
                currencyItem = SerializationUtil.deserializeItemStack(transaction.getExtras());
                if (!ownerPlayer.isOnline()) {
                    hasMoney = false;
                } else {
                    hasMoney = economy.hasItem(ownerPlayer.getPlayer(), currencyItem, (int)cost);
                }
                break;
            default:
                break;
        }

        if (!hasMoney){
            this.executeInfo(Language.TRANSACTION_FAIL.replace("%target%", transaction.getOwnerName()));
            guide.refreshPage(playerName);
            return;
        }

        // 供给方扣除物品
        if (economy.hasItem(player, item, saleAmount)) {
            economy.deductItem(player, item, saleAmount);
        } else {
            this.executeInfo(Language.TRANSACTION_FAIL.replace("%target%", ""));
            guide.refreshPage(playerName);
            return;
        }

        // 非无限情况下，修改交易剩余数量
        if (!admin)  {
            ItemStack save = item.clone();
            int saveAmount = amount - saleAmount;
            if (saleAmount < 0) {
                this.executeInfo(Language.TRANSACTION_NUM);
                guide.refreshPage(playerName);
                return;
            }
            //判断剩余数量
            if(saveAmount == 0){
                service.deleteTransaction(id);
                // 统计数据修改
                service.updateMerchantSelling("-1", transaction.getOwner());
                service.updateMerchantAmount(transaction.getOwner());
            } else {
                // 更新数量和价格
                save.setAmount(saveAmount);
                service.updateTransactionStack(SerializationUtil.serializeItemStack(save), id);
                service.updateTransactionDesc(String.valueOf(saveAmount), id);
                service.updateTransactionPrice(transaction.getPrice() - price, id);
                service.updateTransactionCost(transaction.getCost() - cost, id);
            }
            item.setAmount(saleAmount);
            this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_ORDER, service, transaction.getOwner(), item));
        }

        // 收购方扣钱并通知
        switch(transaction.getCurrency()) {
            case "coin":
                economy.getEconomy().withdrawPlayer(ownerPlayer, cost);
                break;
            case "point":
                economy.gPlayerPointsAPI().take(ownerPlayer.getUniqueId(),  (int)Math.ceil(cost));
                break;
            case "item":
                economy.deductItem(ownerPlayer.getPlayer(), currencyItem, (int)Math.ceil(cost));
                break;
            default:
                break;
        }

        // 通知
        this.executeInfo(Language.TRANSACTION_SUCCEED);

        // 交易记录
        ItemStack recordItem = MarketItem.getRecordItem(itemName, transaction.getOwnerName(), cost, symbol);
        this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_REWARD, service, player.getUniqueId().toString(), recordItem, cost, transaction.getCurrency(), transaction.getExtras()));

        // 刷新
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(transaction.getId()));
    }
    
    

}
