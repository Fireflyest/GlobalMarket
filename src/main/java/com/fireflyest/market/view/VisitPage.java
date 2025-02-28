package com.fireflyest.market.view;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.util.YamlUtils;
import io.fireflyest.emberlib.inventory.Slot;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

/**
 * 访问页
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class VisitPage extends Page {

    private final MarketService service;

    protected VisitPage(String target, int pageNumber, MarketService service) {
        super(target, pageNumber, 54);
        this.service = service;

        final OfflinePlayer player = 
            Bukkit.getOfflinePlayer(UUID.fromString(String.valueOf(target)));
        this.setup(Language.TITLE_VISIT.get()
            .replace("%p%", player.getName())
            .replace("%page%", String.valueOf(pageNumber)));
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

        final ItemBuilder star = MarketItem.getItemBuilder("star");
        final Slot starSlot = new Slot().button(
            ActionResult.ACTION_PLAYER_COMMAND, 
            "market star " + target);
        this.slot(51, star.build(), starSlot);
        final Pair<ItemBuilder, Slot> send = MarketItem.getPair("send");
        this.slot(52, send);
        this.slot(53, MarketItem.getPair("back"));
    }

    @Override
    public Page getNext() {
        if (next == null && pageNumber < 30) {
            next = new VisitPage(target, pageNumber + 1, service);
            next.setPre(this);
        }
        return next;
    }
    
    private void putTransactions() {
        final Transaction[] transactions = service.selectTransactionByOwner(
            String.valueOf(target), (pageNumber - 1) * 45, pageNumber * 45);
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
