package com.fireflyest.market.service;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import com.fireflyest.market.GlobalMarket;
import net.milkbowl.vault.economy.Economy;

/**
 * 市场经济
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class MarketEconomy {
    
    private Economy economy;

    public MarketEconomy() {
        //
    }

    /**
     * 点券插件api
     * @return api
     */
    public PlayerPointsAPI getPlayerPoints() {
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
        final RegisteredServiceProvider<Economy> rsp = 
            Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            GlobalMarket.getPlugin().getLogger().warning("Economy not found!");
            return null;
        }
        economy = rsp.getProvider();
        return economy;
    }

    /**
     * 判断玩家是否有足够物品
     * @param offlinePlayer 玩家
     * @param item 物品
     * @param amount 数量
     * @return 是否足够
     */
    public boolean hasItem(@NotNull OfflinePlayer offlinePlayer, ItemStack item, int amount) {
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            return false;
        }
        return player.getInventory().contains(item.getType(), amount);
    }

    /**
     * 扣除玩家物品
     * @param player 玩家
     * @param item 物品
     * @param amount 数量
     */
    public void deductItem(Player player, ItemStack item, int amount) {
        if (player == null) {
            throw new NullPointerException("Player is null");
        }
        int index;
        while (amount > 0 && (index = player.getInventory().first(item.getType())) != -1) {
            final ItemStack had = player.getInventory().getItem(index);
            if (had == null) {
                throw new NullPointerException("ItemStack is null");
            }
            if (had.getAmount() >= amount) {
                had.setAmount(had.getAmount() - amount);
                return;
            }
            amount -= had.getAmount();
            had.setAmount(0);
        }
    }

}
