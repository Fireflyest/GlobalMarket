package com.fireflyest.market.task;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.util.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TaskSend extends Task{

    private final MarketService service;
    private final ItemStack item;
    private final String targetUid;
    private final double price;
    private final String currency;
    private final String extras;

    private String info;

    public TaskSend(@NotNull String playerName, MarketService service, @NotNull String targetUid, @NotNull ItemStack item, double price, String currency, String extras) {
        super(playerName);
        this.service = service;
        this.item = item;
        this.targetUid = targetUid;
        this.price = price;
        this.currency = currency;
        this.extras = extras;

    }

    public TaskSend(@NotNull String playerName, MarketService service, @NotNull String targetUid, @NotNull ItemStack item) {
        this(playerName, service, targetUid, item, 0, "", "");
    }

    @Override
    public void execute() {
        String stack = SerializationUtil.serializeItemStack(item);

        long id = service.insertDelivery(stack, targetUid, playerName, TimeUtils.getTime(), price, currency, extras);

        if (info != null) {
            service.updateDeliveryInfo(info, id);
        }

        // 通知收到邮箱
        Player targetPlayer = Bukkit.getPlayer(UUID.fromString(targetUid));
        if(targetPlayer != null){
            targetPlayer.sendMessage(Language.RECEIVE_MAIL);
            ChatUtils.sendCommandButton(targetPlayer, Language.TEXT_MAIL_BUTTON, Language.TEXT_MAIL_HOVER, "/market mail");
        }

        this.executeInfo(Language.SEND_MAIL);
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
