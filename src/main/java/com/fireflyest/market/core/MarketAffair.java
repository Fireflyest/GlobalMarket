package com.fireflyest.market.core;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Fireflyest
 * 2021/3/27 21:43
 */

public class MarketAffair implements MarketInteract{

    private MarketAffair(){}

    private MarketHandler marketHandler;

    private static final MarketAffair marketAffair = new MarketAffair();

    public static MarketAffair getInstance() {
        return marketAffair;
    }

    public void initPages() {

        marketHandler = MarketHandler.getInstance();
    }

    @Override
    public void affairBuy(Player player, int id, int amount) {
        marketHandler.obtainBuyTask(MarketTask.BUY, player, id, amount).sendToTarget();
    }

    @Override
    public void affairCancel(Player player, int id) {
        marketHandler.obtainTask(MarketTask.CANCEL, player, id).sendToTarget();
    }

    @Override
    public void affairCancel(int id) {
        marketHandler.obtainTask(MarketTask.CANCEL, id).sendToTarget();
    }

    @Override
    public void affairAuction(Player player, int id, int add) {
        marketHandler.obtainBuyTask(MarketTask.AUCTION, player, id, add).sendToTarget();
    }

    @Override
    public void affairSell(String seller, boolean auction, boolean point, double price, ItemStack item) {
        marketHandler.obtainSellTask(seller, auction, point, price, item).sendToTarget();
    }

    @Override
    public void affairSign(Player player, int id) {
        marketHandler.obtainTask(MarketTask.SIGN, player, id).sendToTarget();
    }

    @Override
    public void affairSend(String to, ItemStack item) {
        marketHandler.obtainMailTask(to, item).sendToTarget();
    }

    @Override
    public void affairFinish(Player player, int id) {
        marketHandler.obtainTask(MarketTask.FINISH, player, id).sendToTarget();
    }

    @Override
    public void affairDiscount(Player player, int id, int discount) {
        marketHandler.obtainDiscountTask(player, id, discount).sendToTarget();
    }

    @Override
    public void affairReprice(Player player, int id, double price) {
        marketHandler.obtainRepriceTask(player, id, price).sendToTarget();
    }

    @Override
    public void affairSignAll(Player player) {
        marketHandler.obtainSignTask(player).sendToTarget();
    }
}
