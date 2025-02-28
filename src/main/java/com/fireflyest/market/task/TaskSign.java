package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.YamlUtils;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * 签收
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskSign extends Task {

    private final long id;
    private final boolean refresh;
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
     */
    public TaskSign(@NotNull UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide, long id) {
        this(uid, service, economy, guide, id, false);
    }

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     * @param id 交易id
     * @param refresh 是否刷新
     */
    public TaskSign(@NotNull UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide, long id, boolean refresh) {
        super(uid);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.id = id;
        this.refresh = refresh;
    }

    @Override
    public void execute() {
        // 玩家不在线
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            return;
        }

        final Delivery delivery = service.selectDeliveryById(id);

        if (delivery == null) {
            this.info(Language.ERROR_DATA.get());
            return;
        }

        final String msg;
        switch (delivery.getCurrency()) {
            case "coin":
                double deliveryPrice = delivery.getPrice();
                // 手续费
                if (Config.PRICE_COMMISSION_ENABLE.get().booleanValue()
                        && deliveryPrice >= Config.PRICE_COMMISSION_THRESHOLD.get()) {
                    deliveryPrice -= (deliveryPrice * Config.PRICE_COMMISSION_RATE.get());
                }
                economy.getEconomy().depositPlayer(offlinePlayer, deliveryPrice);
                msg = Language.SUCCEED_AFFAIR.get()
                    .replace("%m%", economy.getEconomy().format(deliveryPrice));
                this.info(msg + Language.SYMBOL_COIN.get());
                service.deleteDelivery(id);
                break;
            case "point":
                final int get = (int) Math.floor(delivery.getPrice());
                economy.getPlayerPoints().give(UUID.fromString(delivery.getOwner()), get);
                msg = Language.SUCCEED_AFFAIR.get().replace("%m%", String.valueOf(get));
                this.info(msg + Language.SYMBOL_POINT);
                service.deleteDelivery(id);
                break;
            case "item":
            default:
                // 判断背包是否满
                if (player.getInventory().firstEmpty() == -1) {
                    this.info(Language.ERROR_SIGN.get());
                    return;
                }
                if ("".equals(delivery.getExtra())) {
                    delivery.setExtra(delivery.getStack());
                }
                final ItemStack item = YamlUtils.deserializeItemStack(delivery.getExtra());
                final int price = (int) delivery.getPrice();
                if (0 != price) {
                    item.setAmount(price);
                }
                service.deleteDelivery(id);
                player.getInventory().addItem(item);
                this.info(Language.SUCCEED_SIGN.get());
                break;
        }

        if (refresh) {
            guide.refreshPages(GlobalMarket.MAIL_VIEW, uid.toString());
        }
    }
}
