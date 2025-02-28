package com.fireflyest.market.task;

import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;

/**
 * 出售商品
 * 
 * @author Fireflyest
 * @since 1.0
 */
public class TaskSale extends Task {

    private final long id;
    private final int num;
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     * @param id 交易id
     * @param num 出售数量
     */
    public TaskSale(UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide, long id, int num) {
        super(uid);
        this.id = id;
        this.num = num;
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() {
        final Transaction transaction = service.selectTransactionById(id);
        final boolean buyAll = num == 0;

        if (!this.check(transaction, buyAll)) {
            return;
        }

        final int amount = NumberConversions.toInt(transaction.getDesc());
        final boolean admin = transaction.getType().startsWith("admin");
        final double price;
        final double cost;
        final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());
        final int saleAmount = buyAll ?  item.getAmount() : num;
        boolean hasMoney = false;
        String symbol = "";
        ItemStack currencyItem = null;      

        // 计算价格
        if (buyAll) {
            price = transaction.getPrice();
            cost = transaction.getCost();
        } else {
            price = transaction.getPrice() / item.getAmount() * num;
            cost = transaction.getCost() / item.getAmount() * num;
        }

        // 判断收购方钱是否足够
        final OfflinePlayer ownerPlayer = 
            Bukkit.getOfflinePlayer(UUID.fromString(transaction.getOwner()));
        switch (transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(ownerPlayer, cost);
                symbol = Language.SYMBOL_COIN.get();
                break;
            case "point":
                hasMoney = economy.getPlayerPoints().look(ownerPlayer.getUniqueId()) >= cost;
                symbol = Language.SYMBOL_POINT.get();
                break;
            case "item":
                final Player player = ownerPlayer.getPlayer();
                currencyItem = YamlUtils.deserializeItemStack(transaction.getExtra());
                if (player == null) {
                    hasMoney = false;
                } else {
                    hasMoney = economy.hasItem(player, currencyItem, (int) cost);
                }
                break;
            default:
                break;
        }

        if (!hasMoney) {
            this.info(Language.TRANSACTION_FAIL.get().replace("%target%", ownerPlayer.getName()));
            return;
        }

        // 供给方扣除物品
        final Player player = offlinePlayer.getPlayer();
        if (player != null && economy.hasItem(player, item, saleAmount)) {
            economy.deductItem(player, item, saleAmount);
        } else {
            this.info(Language.TRANSACTION_FAIL.get().replace("%target%", ""));
            return;
        }

        // 非无限情况下，修改交易剩余数量
        if (!admin && !this.sale(transaction, amount, price, cost, saleAmount, item)) {
            return;
        }

        // 收购方扣钱并通知
        switch (transaction.getCurrency()) {
            case "coin":
                economy.getEconomy().withdrawPlayer(ownerPlayer, cost);
                break;
            case "point":
                economy.getPlayerPoints().take(ownerPlayer.getUniqueId(),  (int) Math.ceil(cost));
                break;
            case "item":
                economy.deductItem(ownerPlayer.getPlayer(), currencyItem, (int) Math.ceil(cost));
                break;
            default:
                break;
        }

        // 通知
        this.info(Language.SUCCEED_TRANSACTION.get());

        // 出售方得到交易记录
        final ItemStack recordItem = MarketItem.getRecordItem(
            ChatUtils.textItemStack(item, "/market mail"), ownerPlayer.getName(), cost, symbol);
        final TaskSend taskSend = new TaskSend(
            Language.GUI_MAIL_FROM_REWARD.get(), 
            service, 
            guide,
            transaction.getOwner(), 
            recordItem, 
            cost, 
            transaction.getCurrency());
        taskSend.setExtra(transaction.getExtra());
        this.followTasks().add(taskSend);

        // 刷新
        this.refresh(transaction);
    }
    
    private void refresh(Transaction transaction) {
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.MAIN_VIEW, 
            "normal", transaction.getType(), transaction.getCurrency());
        guide.refreshPages(GlobalMarket.MINE_VIEW, transaction.getOwner());
        guide.refreshPages(GlobalMarket.STORE_VIEW);
        guide.refreshPages(GlobalMarket.VISIT_VIEW, transaction.getOwner());
        // 根据分类刷新页面，类型是按二进制存储的
        for (int i = 0; i < 8; i++) {
            if ((transaction.getCategory() & (1 << i)) != 0) {
                guide.refreshPages(GlobalMarket.CATEGORY_VIEW, "category" + i);
            }
        }
    }

    private boolean check(Transaction transaction, boolean buyAll) {
        if (null == transaction) {
            this.info(Language.ERROR_DATA.get());
            return false;
        }
        // 玩家在线
        if (!offlinePlayer.isOnline()) {
            return false;
        }
        if (!"order".equals(transaction.getType()) && !"adminorder".equals(transaction.getType())) {
            this.info(Language.ERROR_TYPE.get());
            return false;
        }
        // 自己购买自己
        if (transaction.getOwner().equals(uid.toString())) {
            this.info(Language.ERROR_TRANSACTION.get());
            if (!Config.DEBUG.get().booleanValue()) {
                return false;
            }
        }
        // 物品货币未设置物品
        if ("item".equals(transaction.getCurrency()) 
                && StringUtils.isEmpty(transaction.getExtra())) {
            this.info(Language.ERROR_CURRENCY.get());
            return false;
        }
        // 点券和物品交易不允许零散购买
        if (!buyAll && !"coin".equals(transaction.getCurrency())) {
            this.info(Language.ERROR_AMOUNT.get());
            return false;
        }
        return true;
    }

    private boolean sale(Transaction transaction, int amount, 
            double price, double cost, int saleAmount, ItemStack item) {
        
        final ItemStack save = item.clone();
        final int saveAmount = amount - saleAmount;
        if (saleAmount < 0) {
            this.info(Language.TRANSACTION_NUM.get());
            return false;
        }
        //判断剩余数量
        if (saveAmount <= 0) {
            service.deleteTransaction(id);
            // 统计数据修改
            service.updateMerchantSelling("-1", transaction.getOwner());
            service.updateMerchantAmount(transaction.getOwner());
        } else {
            // 更新数量和价格
            save.setAmount(saveAmount);
            service.updateTransactionStack(YamlUtils.serializeItemStack(save), id);
            service.updateTransactionDesc(String.valueOf(saveAmount), id);
            service.updateTransactionPrice(transaction.getPrice() - price, id);
            service.updateTransactionCost(transaction.getCost() - cost, id);
        }
        item.setAmount(saleAmount);
        // 收购方得到物品
        this.followTasks().add(new TaskSend(
            Language.GUI_MAIL_FROM_ORDER.get(), service, guide, transaction.getOwner(), item));
        return true;
    }

}
