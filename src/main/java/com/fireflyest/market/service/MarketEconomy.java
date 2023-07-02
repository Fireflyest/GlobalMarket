package com.fireflyest.market.service;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.fireflyest.market.GlobalMarket;

import net.milkbowl.vault.economy.Economy;

public class MarketEconomy {
    
    private Economy economy;

    public MarketEconomy() {
        //
    }

    /**
     * 点券插件api
     * @return api
     */
    public PlayerPointsAPI gPlayerPointsAPI() {
        return PlayerPoints.getInstance().getAPI();
    }

    /**
     * 经济插件api
     * @return api
     */
    public Economy getEconomy() {
        if (economy != null) {
            return economy;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            GlobalMarket.getPlugin().getLogger().warning("Economy not found!");
            return null;
        }
        economy = rsp.getProvider();
        return economy;
    }

    /**
     * 判断玩家是否有足够物品
     * @param player 玩家
     * @param item 物品
     * @param amount 数量
     * @return 是否足够
     */
    public boolean hasItem(Player player, ItemStack item, int amount) {
        return player.getInventory().contains(item.getType(), amount);
    }

    /**
     * 扣除玩家物品
     * @param player 玩家
     * @param item 物品
     * @param amount 数量
     */
    public void deductItem(Player player, ItemStack item, int amount) {
        int index;
        while (amount > 0 && (index = player.getInventory().first(item.getType())) != -1) {
            ItemStack had = player.getInventory().getItem(index);
            if (had.getAmount() >= amount) {
                had.setAmount(had.getAmount() - amount);
                return;
            }
            amount -= had.getAmount();
            had.setAmount(0);
        }
    }

}
