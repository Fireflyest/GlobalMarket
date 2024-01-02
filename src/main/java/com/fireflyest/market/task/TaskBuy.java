package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.util.SerializationUtil;

public class TaskBuy extends Task {

    private final int id;

    private final int num;

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    public TaskBuy(String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;
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
        if(!"retail".equals(transaction.getType()) && !"adminretail".equals(transaction.getType())){
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
        if ("item".equals(transaction.getCurrency()) && ("".equals(transaction.getExtras())) || transaction.getExtras() == null) {
            this.executeInfo(Language.CURRENCY_ERROR);
            return;
        }

        if (player == null || !player.isOnline()) {
            return;
        }

        double price;
        double cost;
        boolean hasMoney = false;
        boolean admin = transaction.getType().startsWith("admin");
        boolean buyAll = num == 0;
        String symbol = "";
        String itemName = transaction.getNickname();
        ItemStack item = SerializationUtil.deserializeItemStack(transaction.getStack());
        ItemStack currencyItem = null;        

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

        // 判断钱是否足够
        switch(transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(player, cost);
                symbol = Language.COIN_SYMBOL;
                break;
            case "point":
                hasMoney = economy.gPlayerPointsAPI().look(player.getUniqueId()) >= cost;
                symbol = Language.POINT_SYMBOL;
                break;
            case "item":
                currencyItem = SerializationUtil.deserializeItemStack(transaction.getExtras());
                hasMoney = economy.hasItem(player, currencyItem, (int)cost);
                break;
            default:
                break;
        }

        if (!hasMoney){
            this.executeInfo(Language.TRANSACTION_FAIL.replace("%target%", ""));
            guide.refreshPage(playerName);
            return;
        }
        // 判断是否无限
        if (admin){
            // 发货给买家
            if (!buyAll) {
                item.setAmount(num);
            }
            this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_TRANSACTION, service, player.getUniqueId().toString(), item));
        } else {
            if (buyAll){
                // 发货给买家
                this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_TRANSACTION, service, player.getUniqueId().toString(), item));
                service.deleteTransaction(id);
                // 统计数据修改
                service.updateMerchantSelling("-1", transaction.getOwner());
                service.updateMerchantAmount(transaction.getOwner());
            }else {
                ItemStack save = item.clone();
                int saveAmount = item.getAmount() - num;
                if(saveAmount < 0){
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
                    service.updateTransactionPrice(transaction.getPrice() - price, id);
                    service.updateTransactionCost(transaction.getCost() - cost, id);
                }
                // 邮寄给买家
                item.setAmount(num);
                this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_TRANSACTION, service, player.getUniqueId().toString(), item));
            }
        }

        // 买家扣钱并通知
        switch(transaction.getCurrency()) {
            case "coin":
                economy.getEconomy().withdrawPlayer(player, cost);
                break;
            case "point":
                economy.gPlayerPointsAPI().take(player.getUniqueId(),  (int)Math.ceil(cost));
                break;
            case "item":
                economy.deductItem(player, currencyItem, (int)Math.ceil(cost));
                break;
            default:
                break;
        }
        this.executeInfo(Language.TRANSACTION_SUCCEED);

        // 发送给卖家
        ItemStack recordItem = MarketItem.getRecordItem(itemName, playerName, cost, symbol);
        this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_REWARD, service, transaction.getOwner(), recordItem, cost, transaction.getCurrency(), transaction.getExtras()));

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));

    }

}
