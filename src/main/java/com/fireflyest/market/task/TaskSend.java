package com.fireflyest.market.task;

import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ChatUtils;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class TaskSend extends Task{

    private final String target;
    private final ItemStack item;
    private final double price;
    private final boolean point;

    public TaskSend(@NotNull String playerName, @NotNull String target, @NotNull ItemStack item, double price, boolean point) {
        super(playerName);
        this.target = target;
        this.item = item;
        this.price = price;
        this.point = point;

        this.type = MarketTasks.MAIL_TASK;

    }

    public TaskSend(@NotNull String playerName, @NotNull String target, @NotNull ItemStack item) {
        super(playerName);
        this.target = target;
        this.item = item;
        this.price = 0;
        this.point = false;
    }

    @Override
    public @NotNull List<Task> execute() {
        String stack = SerializeUtil.serializeItemStack(item);
        String meta = SerializeUtil.serializeItemMeta(item);
        Mail mail = new Mail(0, stack, meta, ItemUtils.getItemValue(item), new Date().getTime(), target, "", false, price > 0, price, point);

        MarketManager.addMail(mail);

        // 通知收到邮箱
        Player targetPlayer = Bukkit.getPlayerExact(target);
        if(targetPlayer != null){
            targetPlayer.sendMessage(Language.HAS_MAIL);
            ChatUtils.sendCommandButton(targetPlayer, Language.MAIL_BUTTON, Language.MAIL_HOVER, "/market mail");
        }
        return then;
    }
}
