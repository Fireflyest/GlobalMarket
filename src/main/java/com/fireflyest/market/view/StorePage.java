package com.fireflyest.market.view;

import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;

import com.fireflyest.market.bean.Merchant;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class StorePage extends TemplatePage {

    private final MarketService service;
    private final MarketYaml yaml;

    protected StorePage(String target, int page, MarketService service, MarketYaml yaml) {
        super(Language.TITLE_STORE_PAGE, target, page, 54);
        this.service = service;
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Merchant[] merchants = service.selectMerchants((page - 1) * 45, page * 45);;

        // 可以下一页
        if (merchants.length != 0){
            asyncButtonMap.put(46, yaml.getItemBuilder("pageNext").build());
        }

        // 放置店铺
        for (int i = 0; i < 45; i++) {
            if(i < merchants.length){
                Merchant merchant = merchants[i];
                String storeName = "".equals(merchant.getStore()) ? "§f" + merchant.getName() : merchant.getStore();
                ItemStack item = new ButtonItemBuilder(merchant.getLogo())
                        .actionPlayerCommand("market visit " + merchant.getName())
                        .name(storeName)
                        .lore(String.format(Language.GUI_OWNER, merchant.getName()))
                        .lore(String.format(Language.GUI_AMOUNT, merchant.getAmount()))
                        .lore(String.format(Language.GUI_SELLING, merchant.getSelling()))
                        .lore(String.format(Language.GUI_VISIT, merchant.getVisit()))
                        .lore(String.format(Language.GUI_STAR, merchant.getStar()))
                        .build();
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
        
        buttonMap.put(53, yaml.getItemBuilder("back").build());
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 30){
            next = new StorePage(target, page + 1, service, yaml);
            next.setPre(this);
        }
        return next;
    }
    
}
