package com.fireflyest.market.view;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.inventory.ViewPage;
import io.fireflyest.craftgui.view.TemplatePage;
import io.fireflyest.util.SerializationUtil;

import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class CategoryPage extends TemplatePage {

    private final MarketService service;
    private final MarketYaml yaml;

    protected CategoryPage(String title, String target, int page, MarketService service, MarketYaml yaml) {
        super(title, target, page, 54);
        this.service = service;
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Transaction[] transactions;
        int categoryNum = NumberConversions.toInt(target.substring(target.length() - 1));
        transactions = service.selectTransactionByCategory((int)Math.pow(2, categoryNum), (page - 1) * 45, page * 45);
        // 可以下一页
        if (transactions.length != 0){
            asyncButtonMap.put(46, yaml.getItemBuilder("pageNext").build());
        }

        // 放置商品
        for (int i = 0; i < 45; i++) {
            if(i < transactions.length){
                Transaction transaction = transactions[i];
                ItemStack item = SerializationUtil.deserializeItemStack(transaction.getStack());
                MarketItem.loreItemData(item, transaction);
                asyncButtonMap.put(i, item);
            }else {
                asyncButtonMap.put(i, new ItemStack(Material.AIR));
            }
        }

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        // 上一页
        if (page == 1){
            buttonMap.put(45, yaml.getItemBuilder("pagePreDisable").build());
        }else {
            buttonMap.put(45, yaml.getItemBuilder("pagePre").build());
        }
        // 下一页
        buttonMap.put(46, yaml.getItemBuilder("pageNextDisable").build());

        buttonMap.put(51, yaml.getItemBuilder("mine").build());
        buttonMap.put(52, yaml.getItemBuilder("mail").build());
        buttonMap.put(53, yaml.getItemBuilder("back").build());
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 30){
            next = new CategoryPage(title, target, page+1, service, yaml);
            next.setPre(this);
        }
        return next;
    }
    
}
