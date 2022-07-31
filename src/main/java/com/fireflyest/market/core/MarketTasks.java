package com.fireflyest.market.core;

import com.fireflyest.market.task.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class MarketTasks {

    public static final int SALE_TASK = 1;
    public static final int MAIL_TASK = 2;

    private static final Map<Integer, TaskHandler> handlerMap = new HashMap<>();

    public interface TaskManager{
        void putTask(Task task);
    }

    private static final TaskManager taskManager = task -> handlerMap.get(task.getType()).put(task);

    private MarketTasks(){
    }

    public static TaskManager getTaskManager() {
        return taskManager;
    }

    public static void initMarketTasks(JavaPlugin plugin){
        handlerMap.put(SALE_TASK, new TaskHandler(plugin));
        handlerMap.put(MAIL_TASK, new TaskHandler(plugin));
    }

    public static void close(){
        for (TaskHandler handler : handlerMap.values()) {
            handler.stop();
        }
    }


}
