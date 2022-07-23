package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TaskBuy extends Task{

    private final int id;

    private final int num;

    private final Economy economy;
    private final PlayerPointsAPI pointsAPI;

    public TaskBuy(String playerName, int id, int num) {
        super(playerName);
        this.id = id;
        this.num = num;

        this.economy = GlobalMarket.getEconomy();
        this.pointsAPI = GlobalMarket.getPointsAPI();

        this.type = MarketTasks.SALE_TASK;
    }

    @Override
    public @NotNull List<Task> execute() {
        // 获取商品
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        if (sale.getPrice() == -1){
            this.executeInfo(Language.NOT_SELLING);
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
        if(point && !buyAll){
            this.executeInfo(Language.COMMAND_ERROR);
            return then;
        }
        double price, cost;
        if (buyAll){
            price = sale.getPrice();
            cost = sale.getCost();
        }else {
            price = sale.getPrice() / item.getAmount() * num;
            cost = sale.getCost() / item.getAmount() * num;
        }

        // 判断钱是否足够
        User buyerUser = MarketManager.getUser(playerName);
        boolean hasMoney;
        if (sale.isPoint()){
            hasMoney = pointsAPI.look(UUID.fromString(buyerUser.getUuid())) >= cost;
        }else {
            hasMoney = economy.has(player, cost);
        }
        if (!hasMoney){
            this.executeInfo(Language.NOT_ENOUGH_MONEY.replace("%target%", ""));
            guide.refreshPage(playerName);
            return then;
        }
        // 获取出售者的玩家信息
        User user = MarketManager.getUser(seller);
        // 判断是否无限
        if(sale.isAdmin()){
            // 发货给买家
            if (!buyAll) item.setAmount(num);
            then.add(new TaskSend(Language.MAIL_FROM_BUY, buyer, item));
        }else {
            if (buyAll){
                // 发货给买家
                then.add(new TaskSend(Language.MAIL_FROM_BUY, buyer, item));
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
                then.add(new TaskSend(Language.MAIL_FROM_BUY, buyer, item));

            }
        }

        // 买家扣钱并通知
        if (point){
            pointsAPI.take(UUID.fromString(buyerUser.getUuid()),  (int)Math.ceil(cost));
        }else {
            economy.withdrawPlayer(player, cost);
        }
        this.executeInfo(Language.BUY_ITEM);

        // 发送给卖家
        ItemStack record = MarketButton.getRecordItem(itemName, buyer, cost, point);
        then.add(new TaskSend("", seller, record, cost, point));

        // 更新卖家统计数据
        user.setMoney(user.getMoney() + cost);
        data.update(user);

        guide.refreshPage(playerName);

        return then;
    }
}
