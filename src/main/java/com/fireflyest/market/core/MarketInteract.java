package com.fireflyest.market.core;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MarketInteract {

    // 购买
    void affairBuy(Player player, int id, int amount);

    // 下架
    void affairCancel(Player player, int id);
    void affairCancel(int id);

    // 拍卖
    void affairAuction(Player player, int id, int add);

    // 出售
    void affairSell(String seller, boolean auction, boolean point, double price, ItemStack item);

    // 签收
    void affairSign(Player player, int id);

    // 邮寄
    void affairSend(String to, ItemStack item);

    // 完成拍卖
    void affairFinish(Player player, int id);

    // 打折
    void affairDiscount(Player player, int id, int discount);

    // 修改价格
    void affairReprice(Player player, int id, double price);

    // 全部签收
    void affairSignAll(Player player);

}
