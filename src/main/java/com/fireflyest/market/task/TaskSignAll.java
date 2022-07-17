package com.fireflyest.market.task;

import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.core.MarketTasks;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskSignAll extends Task{

    public TaskSignAll(@NotNull String playerName) {
        super(playerName);

        this.type = MarketTasks.MAIL_TASK;

    }

    @Override
    public @NotNull List<Task> execute() {
        int max = 36;
        if (player != null){
            max = 36 - player.getInventory().getContents().length;
        }
        data.query(Mail.class, "owner", playerName, 0, max).forEach(mail -> then.add(new TaskSign(playerName, mail.getId())));
        return then;
    }
}
