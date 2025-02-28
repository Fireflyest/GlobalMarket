package com.fireflyest.market.view;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.service.MarketService;

/**
 * 类别查询页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class CategoryPage extends Page {

    private final MarketService service;
    private final String originalTitle;

    protected CategoryPage(String target, int pageNumber, String title, MarketService service) {
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
        this.slot(51, MarketItem.getPair("mine"));
        this.slot(52, MarketItem.getPair("mail"));
        this.slot(53, MarketItem.getPair("back"));
    }

    @Override
    public Page getNext() {
        if (next == null && pageNumber < 30) {
            next = new CategoryPage(target, pageNumber + 1, originalTitle, service);
            next.setPre(this);
        }
        return next;
    }
    
    private void putTransactions() {
        final int categoryNum = NumberConversions.toInt(target.substring(target.length() - 1));
        final Transaction[] transactions = service.selectTransactionByCategory(
            (int) Math.pow(2, categoryNum), (pageNumber - 1) * 45, pageNumber * 45);
        
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
                final Slot slot = new Slot().button(
                    ActionResult.ACTION_PLAYER_COMMAND, 
                    "market affair " + transaction.getId()
                ).shiftButton(
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
