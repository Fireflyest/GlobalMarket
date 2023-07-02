package com.fireflyest.market.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftdatabase.yaml.YamlService;
import org.fireflyest.craftitem.builder.ItemBuilder;

public class MarketYaml extends YamlService {

    private final Map<String, ItemBuilder> itemMap = new HashMap<>();

    public MarketYaml(JavaPlugin plugin) {
        super(plugin);
        this.reloadConfig();
    }

    public ItemBuilder getItemBuilder(String name) {
        return itemMap.getOrDefault(name, new ItemBuilder(Material.STONE));
    }

    public void reloadConfig() {
        itemMap.clear();
        this.setupConfig(Config.class);
        this.setupLanguage(Language.class, Config.LANG);
        this.setupItems(itemMap);
    }
    
}
