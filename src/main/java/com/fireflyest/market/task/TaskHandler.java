package com.fireflyest.market.task;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ArrayBlockingQueue;

public class TaskHandler {

    private boolean enable;

    private final BukkitTask bukkitTask;

    private final ArrayBlockingQueue<Task> taskQueue = new ArrayBlockingQueue<>(256);

    public TaskHandler(@NotNull JavaPlugin plugin){
        this.enable = true;

        // 多线程任务
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
                // 执行任务
                taskQueue.addAll(taskQueue.take().execute());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
