package com.fireflyest.market.view;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskCreate;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.ActionResult;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.inventory.item.SkullItemBuilder;
import io.fireflyest.emberlib.task.TaskHandler;
import io.fireflyest.emberlib.util.YamlUtils;

/**
 * 我的页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class MinePage extends Page {

    private final MarketService service;
    private final ViewGuide guide;
    private final TaskHandler handler;

    protected MinePage(String target, int pageNumber, MarketService service, 
            ViewGuide guide, TaskHandler handler) {
        super(target, pageNumber, 54);
        this.service = service;
        this.guide = guide;
        this.handler = handler;

        this.setup(Language.TITLE_MINE.get());
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

        final Pair<ItemBuilder, Slot> blank = MarketItem.getPair("blank");
        for (int i = 1; i < 53; i += 9) {
            this.slot(i, blank);
        }

        final UUID uid = UUID.fromString(String.valueOf(target));
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uid);
        final ItemBuilder visit = MarketItem.getItemBuilder("visit");
        ((SkullItemBuilder) visit).setPlayer(offlinePlayer);
        final Slot visitSlot = new Slot().button(
            ActionResult.ACTION_PAGE_OPEN, 
            "market.visit." + target);

        this.slot(0, visit.build(), visitSlot);
        this.slot(9, MarketItem.getPair("mail"));

        final Pair<ItemBuilder, Slot> merchant = MarketItem.getPair("merchant");
        merchant.first().lore(String.format(
            Language.GUI_CREDIT.get(), service.selectMerchantCredit(uid)), 0);
        merchant.first().lore(String.format(
            Language.GUI_AMOUNT.get(), service.selectMerchantAmount(uid)), 1);
        merchant.first().lore(String.format(
            Language.GUI_SELLER.get(), service.selectMerchantSelling(uid)), 2);
        this.slot(18, merchant);

        // 上一页
        if (pageNumber == 1) {
            this.slot(52, MarketItem.getPair("pagePreDisable"));
        } else {
            this.slot(52, MarketItem.getPair("pagePre"));
        }
        // 下一页
        this.slot(53, MarketItem.getPair("pageNextDisable"));
        
        this.slot(45, MarketItem.getPair("back"));
    }

    @Override
    public ActionResult action(int index, InventoryAction inventoryAction, 
            Player player, ItemStack currentItem, ItemStack cursor) {
        final ActionResult action 
            = super.action(index, inventoryAction, player, currentItem, cursor);
        if (action.getActions().isEmpty() || inventoryAction != InventoryAction.SWAP_WITH_CURSOR) {
            return action;
        }
        final Pair<Integer, String> pair = action.getActions().get(0);
        if ("create".equals(pair.second())) {
            handler.putTasks(GlobalMarket.TASK_MARKET, new TaskCreate(
                UUID.fromString(target), 
                service, 
                guide, 
                "prepare", 
                "coin", 
                0, 
                cursor.clone()));
            currentItem.setAmount(0);
        }
        return action;
    }

    @Override
    public Page getNext() {
        if (next == null && pageNumber < 30) {
            next = new MinePage(target, pageNumber + 1, service, guide, handler);
            next.setPre(this);
        }
        return next;
    }
    
    private void putTransactions() {
        final Transaction[] transactions = service.selectTransactionByOwner(
            String.valueOf(target), (pageNumber - 1) * 35, pageNumber * 35);

        // 可以下一页
        if (transactions.length != 0) {
            this.slot(53, MarketItem.getPair("pageNext"));
        }

        // 放置商品
        int i = 0;
        int j = 0;
        int m = (pageNumber - 1) * 35;
        for (Transaction transaction : transactions) {
            if (j > 6) { // 到末尾
                if (i >= 4) { // 四行以下就能再加一行，并且转到头
                    break;
                }
                i++;
                j = 0;
            }
            final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());
            MarketItem.loreItemData(item, transaction);
            final Slot slot = new Slot().button(
                ActionResult.ACTION_PAGE_OPEN, 
                "market.edit." + transaction.getId()
            ).shiftButton(
                ActionResult.ACTION_PLAYER_COMMAND,
                "market cancel " + transaction.getId()
            );
            this.slot(i * 9 + 2 + j, item, slot);
            m++;
            j++;
        }

        final int extra = service.selectMerchantSizeByUid(UUID.fromString(String.valueOf(target)));
        while (i < 5) {
            for (; j < 7; j++) {
                final Pair<ItemBuilder, Slot> sell;
                if (m < Config.TRANSACTION_MAXIMUM.get()) {
                    sell = MarketItem.getPair("create");
                } else if (m < Config.TRANSACTION_MAXIMUM.get() + extra) {
                    sell = MarketItem.getPair("createExtra");
                } else {
                    sell = MarketItem.getPair("createOver");
                }
                sell.second().swap(
                    ActionResult.ACTION_NONE, 
                    "create");
                this.slot(i * 9 + 2 + j, sell);
                m++;
            }
            j = 0;
            i++;
        }

        // 店铺图标
        final ItemBuilder logo = MarketItem.getItemBuilder("logo");
        final Slot logoSlot = new Slot().result(
            InventoryAction.PICKUP_ALL, 
            false, 
            ActionResult.ACTION_PAGE_OPEN, 
            "market.visit." + target);
        logo.material(
            Material.getMaterial(service.selectMerchantLogo(String.valueOf(target))));
        this.slot(27, logo.build(), logoSlot);
    }

}
