package com.fireflyest.market.task;

import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.data.Config;
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
        this(playerName, service, economy, guide, id, false);
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

        switch (delivery.getCurrency()) {
            case "coin":
                double deliveryPrice = delivery.getPrice();
                // 手续费
                if (Config.COMMISSION && deliveryPrice >= Config.COMMISSION_THRESHOLD) {
                    deliveryPrice -= (deliveryPrice * Config.COMMISSION_RATE);
                }
                economy.getEconomy().depositPlayer(player, deliveryPrice);
                this.executeInfo(Language.AFFAIR_SUCCEED.replace("%money%", economy.getEconomy().format(deliveryPrice)) + Language.COIN_SYMBOL);
                service.deleteDelivery(id);
                break;
            case "point":
                int get = (int)Math.floor(delivery.getPrice());
                economy.gPlayerPointsAPI().give(UUID.fromString(delivery.getOwner()), get);
                this.executeInfo(Language.AFFAIR_SUCCEED.replace("%money%", String.valueOf(get)) + Language.POINT_SYMBOL);
                service.deleteDelivery(id);
                break;
            case "item":
            default:
                // 判断背包是否满
                if(player.getInventory().firstEmpty() == -1){
                    this.executeInfo(Language.SIGN_ERROR);
                    return;
                }
                if ("".equals(delivery.getExtras())) {
                    delivery.setExtras(delivery.getStack());
                }
                ItemStack item = SerializationUtil.deserializeItemStack(delivery.getExtras());
                int price = (int)delivery.getPrice();
                if (0 != price) {
                    item.setAmount(price);
                }
                service.deleteDelivery(id);
                player.getInventory().addItem(item);
                this.executeInfo(Language.SIGN_SUCCEED);
                break;
        }

        if (refresh) {
            guide.refreshPage(playerName);
        }
    }
}
