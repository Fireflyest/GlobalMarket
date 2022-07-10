package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class SellView implements View<SellPage> {

    private final String title;

    public SellView(String title) {
        this.title = title;
    }

    @Override
    public SellPage getFirstPage(String target){
        Player player = Bukkit.getPlayer(target);
        if (player == null) {
            return null;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        return new SellPage(target, title, itemStack);
    }

    @Override
    public void removePage(String target) {

    }

}
