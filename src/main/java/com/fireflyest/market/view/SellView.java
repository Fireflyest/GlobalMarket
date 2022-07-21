package com.fireflyest.market.view;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.util.ConvertUtils;
import org.fireflyest.craftgui.api.View;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;


public class SellView implements View<SellPage> {

    private final Map<String, SellPage> pageMap = new HashMap<>();
    private final String title;

    public SellView(String title) {
        this.title = title;
    }

    @Override
    public SellPage getFirstPage(String target){
        if (! pageMap.containsKey(target)){
            Sale sale = MarketManager.getSale(ConvertUtils.parseInt(target));
            if (sale != null) {
                pageMap.put(target, new SellPage(title, target));
            }
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }

}
