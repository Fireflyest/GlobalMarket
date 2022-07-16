package com.fireflyest.market.task;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskSend extends Task{

    public TaskSend(@NotNull String playerName) {
        super(playerName);
    }

    @Override
    public @NotNull List<Task> execute() {
        return then;
    }
}
