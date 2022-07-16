package com.fireflyest.market.core;

import com.fireflyest.market.task.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MarketTasks {

    public static final int SALE_TASK = 1;
    public static final int MAIL_TASK = 2;

    private static TaskHandler saleHandler;
    private static TaskHandler mailHandler;

    public interface TaskManager{
        void putTask(Task task);
    }

    private static final TaskManager taskManager = task -> {
        if (task.getType() == SALE_TASK){
            saleHandler.put(task);
        }else if (task.getType() == MAIL_TASK){
            mailHandler.put(task);
        }
    };

    private MarketTasks(){

    }

    public static TaskManager getTaskManager() {
        return taskManager;
    }

    public static void initMarketTasks(JavaPlugin plugin){
        saleHandler = new TaskHandler(plugin);
        mailHandler = new TaskHandler(plugin);
    }

    public static void close(){
        if (saleHandler != null) saleHandler.stop();
        if (mailHandler != null) mailHandler.stop();
    }


}
