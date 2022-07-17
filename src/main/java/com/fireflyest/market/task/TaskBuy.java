package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskBuy extends Task{

    private final int id;

    private final int num;

    private final Economy economy;

    public TaskBuy(String playerName, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;

        this.economy = GlobalMarket.getEconomy();

        this.type = MarketTasks.SALE_TASK;
    }

    @Override
    public @NotNull List<Task> execute() {
        // TODO: 2022/7/16 点券
        // 获取商品
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        String buyer = playerName;
        String seller = sale.getOwner();
        // 拍卖的物品不能直接购买
        if(sale.isAuction()){
            this.executeInfo(Language.TYPE_ERROR);
            return then;
        }
        // 正常情况下不能购买自己出售的东西
        if(seller.equals(buyer)){
            this.executeInfo(Language.BUY_ERROR);
            if (!Config.DEBUG) {
                guide.refreshPage(playerName);
                return then;
            }
        }
        // 解析物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        if(!"null".equals(sale.getNbt()) && !"".equals(sale.getNbt())) ItemUtils.setItemValue(item, sale.getNbt());
        // 购买数量
        String itemName = sale.getNickname();
        boolean buyAll = num == 0, point = sale.isPoint();
        double price, cost;
        if (buyAll){
            price = sale.getPrice();
            cost = sale.getCost();
        }else {
            price = sale.getPrice() / item.getAmount() * num;
            cost = sale.getCost() / item.getAmount() * num;
        }
        // 判断钱是否足够
        if (!economy.has(player, cost)){
            this.executeInfo(String.format(Language.NOT_ENOUGH_MONEY, "你"));
            guide.refreshPage(playerName);
            return then;
        }
        // 获取出售者的玩家信息
        User user = MarketManager.getUser(seller);
        // 判断是否无限
        if(sale.isAdmin()){
            // 发货给买家
            if (!buyAll) item.setAmount(num);
            then.add(new TaskSend("", buyer, item));
        }else {
            if (buyAll){
                // 发货给买家
                then.add(new TaskSend("", buyer, item));
                MarketManager.removeSale(sale);

                // 统计数据修改
                user.setSelling(user.getSelling() - 1);
                user.setAmount(user.getAmount() + 1);
            }else {
                ItemStack save = item.clone();
                int saveAmount = item.getAmount() - num;
                if(saveAmount < 0){
                    this.executeInfo(Language.NOT_ENOUGH_ITEM);
                    guide.refreshPage(playerName);
                    return then;
                }

                //判断剩余数量
                if(saveAmount == 0){
                    MarketManager.removeSale(sale);

                    // 统计数据修改
                    user.setSelling(user.getSelling() - 1);
                    user.setAmount(user.getAmount() + 1);
                }else {
                    // 更新数量和价格
                    save.setAmount(saveAmount);
                    sale.setStack(SerializeUtil.serializeItemStack(save));
                    sale.setPrice(sale.getPrice() - price);
                    sale.setCost(sale.getCost() - cost);

                    MarketManager.updateSale(sale);

                }
                // 邮寄给买家
                item.setAmount(num);
                then.add(new TaskSend("", buyer, item));

            }
        }

        // 买家扣钱并通知
        economy.withdrawPlayer(player, cost);
        this.executeInfo(Language.BUY_ITEM);

        // 发送给卖家
        ItemStack record = ItemUtils.getRecordItem(itemName, buyer, cost, point);
        then.add(new TaskSend("", seller, record, cost, point));

        // 更新卖家统计数据
        user.setMoney(user.getMoney() + cost);
        data.update(user);

        guide.refreshPage(playerName);

        return then;
    }
}
