package com.fireflyest.market.view;

import org.bukkit.event.inventory.InventoryAction;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.inventory.Slot;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;

/**
 * 主页
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class HomePage extends Page {


    protected HomePage() {
        super("", 0, 27);

        this.setup(Language.TITLE_HOME.get());
    }

    @Override
    public void refreshPage() {

        final Pair<ItemBuilder, Slot> category1 = MarketItem.getPair("category1");
        category1.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category1");
        
        final Pair<ItemBuilder, Slot> category2 = MarketItem.getPair("category2");
        category2.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category2");

        final Pair<ItemBuilder, Slot> category3 = MarketItem.getPair("category3");
        category3.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category3");

        final Pair<ItemBuilder, Slot> category4 = MarketItem.getPair("category4");
        category4.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category4");

        final Pair<ItemBuilder, Slot> category5 = MarketItem.getPair("category5");
        category5.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category5");

        final Pair<ItemBuilder, Slot> category6 = MarketItem.getPair("category6");
        category6.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category6");

        final Pair<ItemBuilder, Slot> category7 = MarketItem.getPair("category7");
        category7.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.category.category7");

        this.slot(0, category1);
        this.slot(1, category2);
        this.slot(2, category3);
        this.slot(3, category4);
        this.slot(4, category5);
        this.slot(5, category6);
        this.slot(6, category7);

        this.slot(8, MarketItem.getPair("search"));

        final Pair<ItemBuilder, Slot> admin = MarketItem.getPair("admin");
        admin.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.main.admin");
        this.slot(18, admin);

        final Pair<ItemBuilder, Slot> auction = MarketItem.getPair("auction");
        auction.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.main.auction");
        this.slot(19, auction);

        final Pair<ItemBuilder, Slot> retail = MarketItem.getPair("retail");
        retail.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.main.retail");
        this.slot(20, retail);
        int pos = 21;
        if (Config.MARKET_ORDER.get().booleanValue()) {
            final Pair<ItemBuilder, Slot> order = MarketItem.getPair("order");
            order.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.main.order");
            this.slot(pos++, order);
        }
        if (Config.CURRENCY_POINT.get().booleanValue()) {
            final Pair<ItemBuilder, Slot> point = MarketItem.getPair("point");
            point.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.main.point");
            this.slot(pos++, point);
        }
        if (Config.MARKET_TRADE.get().booleanValue()) {
            final Pair<ItemBuilder, Slot> trade = MarketItem.getPair("trade");
            trade.second().result(
                InventoryAction.PICKUP_ALL, 
                false, 
                ActionResult.ACTION_PAGE_OPEN, 
                "market.main.trade");
            this.slot(pos, trade);
        }

        final Pair<ItemBuilder, Slot> blank = MarketItem.getPair("blank");
        for (int i = 9; i < 17; i++) {
            this.slot(i, blank);
        }
        this.slot(7, blank);
        this.slot(25, blank);

        this.slot(17, MarketItem.getPair("store"));
        this.slot(26, MarketItem.getPair("back"));

    }

}
