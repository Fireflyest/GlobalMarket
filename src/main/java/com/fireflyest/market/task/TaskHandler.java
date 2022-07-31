package com.fireflyest.market.task;

import com.fireflyest.market.data.Config;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ArrayBlockingQueue;

public class TaskHandler {

    private boolean enable;
    private static int succeedNum = 0;
    private static int failNum = 0;
    private BukkitTask bukkitTask;
    private final JavaPlugin plugin;
    private final ArrayBlockingQueue<Task> taskQueue = new ArrayBlockingQueue<>(512);

    public TaskHandler(@NotNull JavaPlugin plugin){
        this.plugin = plugin;

        // 多线程任务
        this.enable = true;
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                loop();
            }
        }.runTaskAsynchronously(plugin);
    }

    public static int getFailNum() {
        return failNum;
    }

    public static int getSucceedNum() {
        return succeedNum;
    }

    public void restart(){
        this.stop();
        this.enable = true;
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                loop();
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * 添加任务到执行队列，并解锁
     * @param task 任务
     */
    public void put(Task task){
        if (!enable) {
            if (Config.DEBUG) plugin.getLogger().info("The handler restarting! ");
            this.restart();
        }
        try {
            synchronized (taskQueue){
                taskQueue.put(task);
                taskQueue.notify();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止
     */
    public void stop(){
        // 停止循环
        enable = false;
        // 解锁
        synchronized (taskQueue){
            taskQueue.notify();
        }
        // 关闭线程
        bukkitTask.cancel();
    }

    /**
     * 开启循环执行任务队列
     */
    private void loop(){
        while (enable) {
            try {
                // 如果执行完，就锁住
                if (taskQueue.isEmpty()) {
                    synchronized (taskQueue) {
                        taskQueue.wait();
                    }
                    continue;
                }
                taskQueue.addAll(taskQueue.take().execute());
                // 统计数量
                succeedNum++;
            } catch (InterruptedException e) {
                plugin.getLogger().severe("error on taskQueue take, handler stop!");
                failNum++;
                this.stop();
            } catch (Exception e) {
                plugin.getLogger().severe("error on task execute, handler stop!");
                failNum++;
                this.stop();
            }
        }
    }

}
