package com.fireflyest.market.view;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.service.MarketService;

/**
 * 主页
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class MainPage extends Page {

    private final MarketService service;
    private String originalTitle;

    protected MainPage(String target, int pageNumber, String title, MarketService service) {
        super(target, pageNumber, 54);
        this.service = service;
        this.originalTitle = title;

        this.setup(title.replace("%page%", String.valueOf(pageNumber)));
    }

    @Override
    public void refreshPage() {
        if (!init) {
            this.initPage();
        }

        this.putTransactions();
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

        if (Config.MARKET_NAVIGATION.get().booleanValue()) {
            this.slot(50, MarketItem.getPair("mine"));
            this.slot(51, MarketItem.getPair("mail"));
            this.slot(52, MarketItem.getPair("home"));
        } else {
            this.slot(51, MarketItem.getPair("mine"));
            this.slot(52, MarketItem.getPair("mail"));
        }
        if ("normal".equals(target)) {
            this.slot(53, MarketItem.getPair("close"));
        } else {
            this.slot(53, MarketItem.getPair("back"));
        }
    }

    @Override
    public Page getNext() {
        if (next == null && pageNumber < 30) {
            next = new MainPage(target, pageNumber + 1, originalTitle, service);
            next.setPre(this);
        }
        return next;
    }

    private void putTransactions() {
        final Transaction[] transactions;
        switch (String.valueOf(target)) {
            case "normal":
                transactions = service.selectTransactions((pageNumber - 1) * 45, pageNumber * 45);
                break;
            case "retail":
            case "order":
            case "auction":
            case "admin":
                transactions = service.selectTransactionByType(
                    String.valueOf(target), (pageNumber - 1) * 45, pageNumber * 45);
                break;
            case "point":
            case "coin":
            case "item":
                transactions = service.selectTransactionByCurrency(
                    String.valueOf(target), (pageNumber - 1) * 45, pageNumber * 45);
                break;
            default:
                transactions = service.selectTransactions((pageNumber - 1) * 45, pageNumber * 45);
                break;
        }
        // 可以下一页
        if (transactions.length != 0) {
            this.slot(46, MarketItem.getPair("pageNext"));
        }

        // 放置商品
        for (int i = 0; i < 45; i++) {
            if (i < transactions.length) {
                final Transaction transaction = transactions[i];
                final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());
                MarketItem.loreItemData(item, transaction);
                final Slot slot = new Slot().result(
                    InventoryAction.PICKUP_ALL, 
                    false, 
                    ActionResult.ACTION_PLAYER_COMMAND, 
                    "market affair " + transaction.getId()
                ).result(
                    InventoryAction.MOVE_TO_OTHER_INVENTORY, 
                    false,
                    ActionResult.ACTION_PLAYER_COMMAND,
                    "market cancel " + transaction.getId()
                );
                this.slot(i, item, slot);
            } else {
                this.slot(i, new ItemStack(Material.AIR));
            }
        }
    }

}
