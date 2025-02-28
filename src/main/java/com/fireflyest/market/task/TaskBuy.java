package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.YamlUtils;

/**
 * 购买任务
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskBuy extends Task {

    private final int id;

    private final int num;

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    /**
     * 构造购买任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     * @param id 交易id
     * @param num 购买数量
     */
    public TaskBuy(UUID uid, MarketService service, MarketEconomy economy, 
            ViewGuide guide, int id, int num) {
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

        // 检查是否符合交易条件
        if (!this.check(transaction, buyAll)) {
            return;
        }

        boolean hasMoney = false;
        String symbol = "";
        ItemStack currencyItem = null;        
        final double price;
        final double cost;
        final boolean admin = transaction.getType().startsWith("admin");
        final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());

        // 计算价格
        if (buyAll) {
            price = transaction.getPrice();
            cost = transaction.getCost();
        } else {
            price = transaction.getPrice() / item.getAmount() * num;
            cost = transaction.getCost() / item.getAmount() * num;
        }

        // 判断钱是否足够
        switch (transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(offlinePlayer, cost);
                symbol = Language.SYMBOL_COIN.get();
                break;
            case "point":
                hasMoney = economy.getPlayerPoints().look(uid) >= cost;
                symbol = Language.SYMBOL_POINT.get();
                break;
            case "item":
                currencyItem = YamlUtils.deserializeItemStack(transaction.getExtra());
                hasMoney = economy.hasItem(offlinePlayer, currencyItem, (int) cost);
                break;
            default:
                break;
        }
        if (!hasMoney) {
            this.info(Language.TRANSACTION_FAIL.get().replace("%target%", ""));
            return;
        }

        // 判断是否无限
        if (admin) {
            // 无限购买
            this.buyAdmin(buyAll, item);
        } else {
            if (buyAll) {
                // 一次性购买
                this.buyAll(transaction, item);
            } else {
                // 部分购买
                final ItemStack save = item.clone();
                final int saveAmount = item.getAmount() - num;
                if (saveAmount < 0) {
                    this.info(Language.TRANSACTION_NUM.get());
                    return;
                }
                this.buyPart(transaction, item, save, saveAmount, price, cost);
            }
        }

        // 买家扣钱并通知
        switch (transaction.getCurrency()) {
            case "coin":
                economy.getEconomy().withdrawPlayer(offlinePlayer, cost);
                break;
            case "point":
                economy.getPlayerPoints().take(uid,  (int) Math.ceil(cost));
                break;
            case "item":
                economy.deductItem(offlinePlayer.getPlayer(), currencyItem, (int) Math.ceil(cost));
                break;
            default:
                break;
        }
        this.info(Language.SUCCEED_TRANSACTION.get());

        // 发送给卖家
        final ItemStack recordItem = MarketItem.getRecordItem(
            ChatUtils.textItemStack(item, "/market mail"), getPlayerName(), cost, symbol);
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

        // 刷新页面
        this.refresh(transaction);
    }

    private void refresh(Transaction transaction) {
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.MAIN_VIEW, 
            "normal", "retail", "adminretail", transaction.getCurrency());
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
        // 零售类型才能购买
        if (!"retail".equals(transaction.getType()) 
                && !"adminretail".equals(transaction.getType())) {
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

    private void buyPart(Transaction transaction, ItemStack item, 
            ItemStack save, int saveAmount, double price, double cost) {
        //判断剩余数量
        if (saveAmount == 0) {
            service.deleteTransaction(id);
            // 统计数据修改
            service.updateMerchantSelling("-1", transaction.getOwner());
            service.updateMerchantAmount(transaction.getOwner());
        } else {
            // 更新数量和价格
            save.setAmount(saveAmount);
            service.updateTransactionStack(YamlUtils.serializeItemStack(save), id);
            service.updateTransactionPrice(transaction.getPrice() - price, id);
            service.updateTransactionCost(transaction.getCost() - cost, id);
        }
        // 邮寄给买家
        item.setAmount(num);
        this.followTasks().add(new TaskSend(
            Language.GUI_MAIL_FROM_TRANSACTION.get(), 
            service, 
            guide, 
            uid.toString(), 
            item));
    }

    private void buyAll(Transaction transaction, ItemStack item) {
        // 发货给买家
        this.followTasks().add(new TaskSend(
            Language.GUI_MAIL_FROM_TRANSACTION.get(), 
            service, 
            guide, 
            uid.toString(), 
            item));
        service.deleteTransaction(id);
        // 统计数据修改
        service.updateMerchantSelling("-1", transaction.getOwner());
        service.updateMerchantAmount(transaction.getOwner());
    }

    private void buyAdmin(boolean buyAll, ItemStack item) {
        // 发货给买家
        if (!buyAll) {
            item.setAmount(num);
        }
        this.followTasks().add(new TaskSend(
            Language.GUI_MAIL_FROM_TRANSACTION.get(), 
            service, 
            guide, 
            uid.toString(), 
            item));
    }

}
