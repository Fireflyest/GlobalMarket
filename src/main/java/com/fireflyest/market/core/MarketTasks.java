package com.fireflyest.market.core;

public class MarketTasks {

    private static final MarketTasks instance = new MarketTasks();

    public static MarketTasks getInstance() {
        return instance;
    }

    private interface TaskManager{
        void putTask();
    }

    private final TaskManager taskManager = () -> {

    };

    private MarketTasks(){

    }

    public TaskManager getTaskManager() {
        return taskManager;
    }


}
