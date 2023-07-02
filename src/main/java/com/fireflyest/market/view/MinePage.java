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

import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class MinePage extends TemplatePage {

    private final MarketService service;
    private final MarketYaml yaml;

    protected MinePage(String target, int page, MarketService service, MarketYaml yaml) {
        super(Language.TITLE_MINE_PAGE, target, page, 54);
        this.service = service;
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        Transaction[] transactions = service.selectTransactionByOwnerName(target, (page - 1) * 35, page * 35);

        // 可以下一页
        if (transactions.length != 0){
            asyncButtonMap.put(53, yaml.getItemBuilder("pageNext").build());
        }

        // 放置商品
        int i = 0;
        int j = 0;
        int m = (page - 1) * 35;
        for (Transaction transaction : transactions) {
            if (j > 6){ // 到末尾
                if (i >= 4) { // 四行以下就能再加一行，并且转到头
                    break;
                }
                i++;
                j = 0;
            }
            ItemStack item = SerializationUtil.deserializeItemStack(transaction.getStack());
            MarketItem.loreItemData(item, transaction, "edit");
            asyncButtonMap.put(i * 9 + 2 + j, item);
            m++;
            j++;
        }

        int extra = service.selectMerchantSize(target);
        while (i < 5){
            for (; j < 7; j++){
                ItemStack sell;
                if (m < Config.MAXIMUM_SALE_NUM){
                    sell = yaml.getItemBuilder("create").build();
                }else if (m < Config.MAXIMUM_SALE_NUM + extra){
                    sell = yaml.getItemBuilder("createExtra").build();
                }else {
                    sell = yaml.getItemBuilder("createOver").build();
                }
                asyncButtonMap.put(i * 9 + 2 + j, sell);
                m++;
            }
            j = 0;
            i++;
        }

        // 店铺图标
        ItemStack logo = ((ButtonItemBuilder)yaml.getItemBuilder("logo")).actionPlugin("logo " + target).build();
        logo.setType(Material.getMaterial(service.selectMerchantLogo(target)));
        buttonMap.put(27, logo);

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        ItemStack blank = yaml.getItemBuilder("blank").build();
        for (int i = 1; i < 53; i+=9){
            buttonMap.put(i, blank);
        }

        ItemStack visit = ((ButtonItemBuilder)yaml.getItemBuilder("visit")).actionOpenPage("market.visit." + target).build();
        UUID uid = UUID.fromString(service.selectMerchantUid(target));
        ItemUtils.setSkullOwner(visit, Bukkit.getOfflinePlayer(uid));
        ItemUtils.setDisplayName(visit, ItemUtils.getDisplayName(visit).replace("%target%", target));

        buttonMap.put(0, visit);
        buttonMap.put(9, yaml.getItemBuilder("mail").build());

        ItemStack merchant = yaml.getItemBuilder("merchant").build();
        ItemUtils.addLore(merchant, String.format(Language.GUI_CREDIT, service.selectMerchantCredit(uid)));
        ItemUtils.addLore(merchant, String.format(Language.GUI_AMOUNT, service.selectMerchantAmount(uid)));
        ItemUtils.addLore(merchant, String.format(Language.GUI_SELLER, service.selectMerchantSelling(uid)));
        buttonMap.put(18, merchant);

        // 上一页
        if (page == 1){
            buttonMap.put(52, yaml.getItemBuilder("pagePreDisable").build());
        }else {
            buttonMap.put(52, yaml.getItemBuilder("pagePre").build());
        }
        // 下一页
        buttonMap.put(53, yaml.getItemBuilder("pageNextDisable").build());
        
        buttonMap.put(45, yaml.getItemBuilder("back").build());
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 30){
            next = new MinePage(target, page + 1, service, yaml);
            next.setPre(this);
        }
        return next;
    }
    
}
