package com.fireflyest.market.core;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Fireflyest
 * 2021/3/30 14:41
 */

public class MarketTask {

    private final MarketHandler handler;

    public static final int NONE = 0;
    public static final int BUY = 1;
    public static final int CANCEL = 2;
    public static final int AUCTION = 3;
    public static final int SELL = 4;
    public static final int SIGN = 5;
    public static final int SEND = 6;
    public static final int FINISH = 7;
    public static final int DISCOUNT = 8;
    public static final int REPRICE = 9;
    public static final int SIGN_ALL = 10;

    public MarketTask() {
        handler = MarketHandler.getInstance();
    }

    public int type = NONE;
    public Player player;
    public String  name;
    public int id;
    public int amount;
    public double price;
    public boolean point;
    public boolean auction;
    public ItemStack item;

    public void sendToTarget(){
        handler.sendTask(this);
    }

}
