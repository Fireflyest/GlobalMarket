package com.fireflyest.market.task;

import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.util.SerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TaskSign extends Task {

    private final long id;
    private final boolean refresh;
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    public TaskSign(@NotNull String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, long id) {
        this(playerName, service, economy, guide, id, true);
    }

    public TaskSign(@NotNull String playerName, MarketService service, MarketEconomy economy, ViewGuide guide, long id, boolean refresh) {
        super(playerName);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.id = id;
        this.refresh = refresh;
    }

    @Override
    public void execute() {
        // 玩家不在线
        if (player == null || !player.isOnline()) {
            return;
        }

        Delivery delivery = service.selectDeliveryById(id);

        if (delivery == null) {
            this.executeInfo(Language.DATA_ERROR);
            return;
        }

        service.deleteDelivery(id);
        if (refresh) {
            guide.refreshPage(playerName);
        }

        switch (delivery.getCurrency()) {
            case "coin":
                economy.getEconomy().depositPlayer(player, delivery.getPrice());
                player.sendMessage(Language.AFFAIR_SUCCEED.replace("%money%", economy.getEconomy().format(delivery.getPrice())) + Language.COIN_SYMBOL);
                break;
            case "point":
                int get = (int)Math.floor(delivery.getPrice());
                economy.gPlayerPointsAPI().give(UUID.fromString(delivery.getOwner()), get);
                player.sendMessage(Language.AFFAIR_SUCCEED.replace("%money%", String.valueOf(get)) + Language.POINT_SYMBOL);
                break;
            case "item":
                // 判断背包是否满
                if(player.getInventory().firstEmpty() == -1){
                    this.executeInfo(Language.SIGN_ERROR);
                    return;
                }
                ItemStack item = SerializationUtil.deserializeItemStack(delivery.getExtras());
                item.setAmount((int)delivery.getPrice());
                player.getInventory().addItem(item);
                player.sendMessage(Language.SIGN_SUCCEED);
                break;
            default:
                break;
        }

    }
}
