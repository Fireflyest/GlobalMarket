package com.fireflyest.market.view;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import com.fireflyest.market.bean.Merchant;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;

/**
 * 玩家店铺页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class StorePage extends Page {

    private final MarketService service;

    protected StorePage(String target, int pageNumber, MarketService service) {
        super(target, pageNumber, 54);
        this.service = service;

        this.setup(Language.TITLE_STORE.get().replace("%page%", String.valueOf(pageNumber)));
    }

    @Override
    public void refreshPage() {
        if (!init) {
            this.initPage();
        }

        this.putStore();
    }

    @Override
    public void initPage() {
        super.initPage();

        // 上一页
        if (pageNumber == 1) {
            this.slot(45, MarketItem.getPair("pagePreDisable"));
        } else {
            this.slot(45, MarketItem.getPair("pagePre"));
        }
        // 下一页
        this.slot(46, MarketItem.getPair("pageNextDisable"));

        this.slot(53, MarketItem.getPair("back"));
    }

    @Override
    public Page getNext() {
        if (next == null && pageNumber < 30) {
            next = new StorePage(target, pageNumber + 1, service);
            next.setPre(this);
        }
        return next;
    }
    
    private void putStore() {

        final Merchant[] merchants = 
            service.selectMerchants((pageNumber - 1) * 45, pageNumber * 45);

        // 可以下一页
        if (merchants.length != 0) {
            this.slot(46, MarketItem.getPair("pageNext"));
        }

        // 放置店铺
        for (int i = 0; i < 45; i++) {
            if (i < merchants.length) {
                final Merchant merchant = merchants[i];
                final String playerName = merchant.getName();
                final String storeName = StringUtils.isEmpty(merchant.getStore())
                    ? Language.GUI_STORE.get().replace("%p%", playerName) : merchant.getStore();
                final ItemStack item = new ItemBuilder(merchant.getLogo())
                        .name(storeName)
                        .lore(String.format(Language.GUI_OWNER.get(), merchant.getName()))
                        .lore(String.format(Language.GUI_AMOUNT.get(), merchant.getAmount()))
                        .lore(String.format(Language.GUI_SELLING.get(), merchant.getSelling()))
                        .lore(String.format(Language.GUI_VISIT.get(), merchant.getVisit()))
                        .lore(String.format(Language.GUI_STAR.get(), merchant.getStar()))
                        .build();
                final Slot slot = new Slot();
                slot.result(InventoryAction.PICKUP_ALL, 
                    false, 
                    ActionResult.ACTION_PAGE_OPEN, 
                    "market.visit." + merchant.getUid());
                this.slot(i, item, slot);
            } else {
                this.slot(i, new ItemStack(Material.AIR));
            }
        }
    }

}
