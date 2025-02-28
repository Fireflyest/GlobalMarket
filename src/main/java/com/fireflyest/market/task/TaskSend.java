package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.TimeUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * 发送邮件
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskSend extends Task {

    private final String by;
    private final MarketService service;
    private final ViewGuide guide;
    private final ItemStack item;
    private final String targetUid;
    private final double price;
    private final String currency;

    private String extra;
    private String info;

    /**
     * 构造任务
     * 
     * @param by 玩家uid
     * @param service 服务
     * @param targetUid 目标uid
     * @param item 物品
     * @param price 价格
     * @param currency 货币
     */
    public TaskSend(@NotNull String by, MarketService service, ViewGuide guide, 
            @NotNull String targetUid, @NotNull ItemStack item, double price, String currency) {
        super(by);
        this.by = by;
        this.service = service;
        this.guide = guide;
        this.item = item;
        this.targetUid = targetUid;
        this.price = price;
        this.currency = currency;
    }

    /**
     * 构造任务
     * 
     * @param by 玩家uid
     * @param service 服务
     * @param targetUid 目标uid
     * @param item 物品
     */
    public TaskSend(@NotNull String by, MarketService service, ViewGuide guide, 
            @NotNull String targetUid, @NotNull ItemStack item) {
        this(by, service, guide, targetUid, item, 0, "item");
        this.extra = YamlUtils.serializeItemStack(item);
    }

    @Override
    public void execute() {
        final String stack = YamlUtils.serializeItemStack(item);

        final long id = service.insertDelivery(
            stack, targetUid, by, TimeUtils.getTime(), price, currency, extra);

        if (info != null) {
            service.updateDeliveryInfo(info, id);
        }

        // 通知收到邮箱
        final Player targetPlayer = Bukkit.getPlayer(UUID.fromString(targetUid));
        if (targetPlayer != null) {
            final String sendInfo = Language.MAIL_RECEIVE.get();
            final BaseComponent[] textItemStack = ChatUtils.textItemStack(item, "/market mail");
            targetPlayer.spigot()
                        .sendMessage(new ComponentBuilder(sendInfo).append(textItemStack).create());
        }

        this.info(Language.MAIL_SEND.get());

        guide.refreshPages(GlobalMarket.MAIL_VIEW, targetUid);
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

}
