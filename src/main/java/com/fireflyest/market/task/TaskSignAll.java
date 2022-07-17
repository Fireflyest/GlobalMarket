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
        data.query(Mail.class, "owner", playerName).forEach(mail -> then.add(new TaskSign(playerName, mail.getId())));
        if (then.size() == 0) guide.refreshPage(playerName);
        return then;
    }
}
