package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.core.MarketManager;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.Storage;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.MysqlExecuteUtils;
import com.fireflyest.market.util.SerializeUtil;
import com.fireflyest.market.util.SqliteExecuteUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class MinePage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private final Inventory inventory;
    private final String title;
    private final String target;
    private final int page;
    private final int size;
    private final String sql;

    private final Storage storage;

    private ViewPage next = null;
    private ViewPage pre = null;

    public MinePage(String title, String target, int page, int size) {
        this.storage = GlobalMarket.getStorage();
        this.title = title;
        this.target = target;
        this.page = page;
        this.size = size;
        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + Language.MARKET_MINE_NICK);    // 副标题
        if (page != 0) guiTitle += (" §7#§8" + page);          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, size, guiTitle);

        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Sale.class, "owner", target,(page-1)*35, page*35);
        }else {
            sql = SqliteExecuteUtils.query(Sale.class, "owner", target,(page-1)*35, page*35);
        }

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        List<Sale> sales = storage.inquiryList(sql, Sale.class);
        int i = 0, j = 0, m = (page-1)*35;
        for (Sale sale : sales) {
            if (j > 6){ // 到末尾
                if (i < 4){ // 四行以下就能再加一行，并且转到头
                    i++;
                    j = 0;
                }else {
                    break;
                }
            }
            ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
            ItemUtils.loreSaleItem(item, sale);
            crashMap.put(i * 9 + 2 + j, item);
            m++;
            j++;
        }

        while (i < 5){
            for (; j < 7; j++){
                ItemStack sell;
                if (m < Config.LIMIT_AMOUNT_NUM){
                    sell = MarketButton.SELL.clone();
                }else if (m < Config.LIMIT_AMOUNT_NUM_VIP){
                    sell = MarketButton.SELL_VIP.clone();
                }else {
                    sell = MarketButton.SELL_OP.clone();
                }
                crashMap.put(i * 9 + 2 + j, sell);
                m++;
            }
            j = 0;
            i++;
        }
        // 可以下一页
        if (sales.size() != 0){
            crashMap.put(53, MarketButton.PAGE_NEXT);
        }
        // 添加皮肤
        ItemStack mine = crashMap.get(0);
        org.fireflyest.craftgui.util.ItemUtils.setSkullOwner(mine, MarketManager.getOfflinePlayer(target));
        return crashMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return crashMap.get(slot);
    }

    @Override
    public @NotNull Inventory getInventory(){
        return inventory;
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 5){
            next = new MinePage(title, target, page+1, size);
            next.setPre(this);
        }
        return next;
    }

    @Override
    public ViewPage getPre() {
        return pre;
    }

    @Override
    public void setPre(ViewPage pre) {
        this.pre = pre;
    }

    @Override
    public void setNext(ViewPage viewPage) {

    }

    @Override
    public void refreshPage() {
        for (int i = 1; i < 53; i+=9){
            itemMap.put(i, MarketButton.BLANK);
        }
        ItemStack head = MarketButton.OTHER.clone();
        org.fireflyest.craftgui.util.ItemUtils.setDisplayName(head, "§3§l" + String.format(Language.MARKET_OTHER_NICK, target));
        ItemUtils.setItemValue(head, String.format("other %s", target));
        itemMap.put(0, head);
        itemMap.put(9, MarketButton.MARKET);
        itemMap.put(18, MarketButton.MAIL);

        // 上一页
        if (page == 1){
            itemMap.put(52, MarketButton.PAGE_PRE_DISABLE);
        }else {
            itemMap.put(52, MarketButton.PAGE_PRE);
        }
        // 下一页
        itemMap.put(53, MarketButton.PAGE_NEXT_DISABLE);

        itemMap.put(45, MarketButton.CLOSE);
    }

    @Override
    public void updateTitle(String s) {

    }

}
