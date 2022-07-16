package com.fireflyest.market.task;

import org.bukkit.inventory.ItemStack;

public abstract class TaskFactory<T extends Task> {

    public String playerName;
    public String targetName;
    public long id;
    public int num;
    public double price;
    public boolean point;
    public boolean auction;
    public ItemStack item;

    public TaskFactory(){
    }

    public TaskFactory<T> setPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public TaskFactory<T> setTargetName(String targetName) {
        this.targetName = targetName;
        return this;
    }

    public TaskFactory<T> setId(long id) {
        this.id = id;
        return this;
    }

    public TaskFactory<T> setNum(int num) {
        this.num = num;
        return this;
    }

    public TaskFactory<T> setPrice(double price) {
        this.price = price;
        return this;
    }

    public TaskFactory<T> setPoint(boolean point) {
        this.point = point;
        return this;
    }

    public TaskFactory<T> setAuction(boolean auction) {
        this.auction = auction;
        return this;
    }

    public TaskFactory<T> setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    abstract T createTask();
}
