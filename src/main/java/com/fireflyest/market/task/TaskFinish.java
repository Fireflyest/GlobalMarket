package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TaskFinish extends Task{

    private final int id;

    private final Economy economy;
    private final PlayerPointsAPI pointsAPI;

    public TaskFinish(@NotNull String playerName, int id) {
        super(playerName);
        this.id = id;

        this.economy = GlobalMarket.getEconomy();
        this.pointsAPI = GlobalMarket.getPointsAPI();

        this.type = MarketTasks.SALE_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        // 判断操作者是否商品主人
        if(!sale.getOwner().equals(playerName)){
            this.executeInfo(Language.CANCEL_ERROR);
            return then;
        }

        String seller = sale.getOwner();
        String buyer = sale.getBuyer();
        String itemName = sale.getNickname();
        double cost = sale.getCost();
        boolean point = sale.isPoint();

        if(!sale.isAuction()){
            // 如果不是拍卖的物品
            then.add(new TaskCancel(playerName, id));
            return then;
        }
        if("".equalsIgnoreCase(buyer)){
            // 无人拍
            this.executeInfo(Language.AUCTION_FLOW.replace("%id%", String.valueOf(sale.getId())));
            then.add(new TaskCancel(playerName, id));
            return then;
        }
        // 获取最终竞拍者
        User buyUser = MarketManager.getUser(buyer);
        OfflinePlayer buyerPlayer = Bukkit.getOfflinePlayer(UUID.fromString(buyUser.getUuid()));

        User buyerUser = MarketManager.getUser(playerName);
        boolean hasMoney;
        if (sale.isPoint()){
            hasMoney = pointsAPI.look(UUID.fromString(buyerUser.getUuid())) >= cost;
        }else {
            hasMoney = economy.has(player, cost);
        }
        if (!hasMoney){
            // 不够钱，扣信誉
            this.executeInfo(Language.NOT_ENOUGH_MONEY.replace("%target%", buyer));
            buyUser.setCredit(buyUser.getCredit() - 1);
            data.update(buyUser);
            then.add(new TaskCancel(playerName, id));
            return then;
        }
        if (buyerPlayer.isOnline()){
            ((Player) buyerPlayer).sendMessage(Language.BUY_ITEM);
        }
        // 买家扣钱
        if (sale.isPoint()){
            pointsAPI.take(UUID.fromString(buyerUser.getUuid()),  (int)Math.ceil(cost));
        }else {
            economy.withdrawPlayer(buyerPlayer, sale.getCost());
        }
        // 发送物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        then.add(new TaskSend(Language.MAIL_FROM_AUCTION, buyer, item));

        // 更新市场
        MarketManager.removeSale(sale);

        // 发送给卖家
        ItemStack record = MarketButton.getRecordItem(itemName, buyer, cost, point);
        this.executeInfo(Language.AUCTION_FINISH.replace("%id%", String.valueOf(sale.getId())));
        then.add(new TaskSend("", seller, record, cost, point));

        // 更新卖家统计数据
        User user = MarketManager.getUser(seller);
        user.setSelling(user.getSelling() - 1);
        user.setAmount(user.getAmount() + 1);
        user.setMoney(user.getMoney() + cost);
        data.update(user);

        return then;
    }
}
