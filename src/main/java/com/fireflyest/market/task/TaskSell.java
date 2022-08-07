package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.fireflyest.craftgui.util.TranslateUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class TaskSell extends Task{

    private final boolean auction;
    private final boolean point;
    private final double price;
    private final ItemStack item;

    public TaskSell(@NotNull String playerName, boolean auction, boolean point, double price, ItemStack item) {
        super(playerName);
        this.auction =auction;
        this.point = point;
        this.price = price;
        this.item = item;

        this.type = MarketTasks.SALE_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        // 获取物品名称
        String itemName;
        if(item.hasItemMeta() && item.getItemMeta() != null && !"".equals(item.getItemMeta().getDisplayName())){
            itemName = item.getItemMeta().getDisplayName();
        }else {
            itemName = TranslateUtils.translate(item.getType());
        }

        String stack = SerializeUtil.serializeItemStack(item);
        String meta = SerializeUtil.serializeItemMeta(item);
        Sale sale = new Sale(0, stack, meta, "", new Date().getTime(), playerName, "", price, price, 3, itemName, auction, point);

        MarketManager.addSale(sale, item.getType());

        // 增加正在出售的数量
        User user = MarketManager.getUser(playerName);
        user.setSelling(user.getSelling() + 1);
        data.update(user);

        // 广播
        if(!Config.SELL_BROADCAST || price == -1) return then;
        ChatUtils.sendItemButton(item, String.format("/market affair %s", sale.getId()), playerName);

        return then;
    }
}
