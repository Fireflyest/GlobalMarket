package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Config;
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

public class SearchPage implements ViewPage {

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

    public SearchPage(String title, String target, int page, int size) {
        this.storage = GlobalMarket.getStorage();
        this.title = title;
        this.target = target;
        this.page = page;
        this.size = size;
        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题
        if (page != 0) guiTitle += (" §7#§8" + page);          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, size, guiTitle);

        String condition = String.format(" where nickname like '%s' or meta like '%s'", "%" + target +"%", "%" + target +"%");
        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Sale.class, condition,(page-1)*45, page*45);
        }else {
            sql = SqliteExecuteUtils.query(Sale.class, condition,(page-1)*45, page*45);
        }

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        List<Sale> sales = storage.inquiryList(sql, Sale.class);
        // 可以下一页
        if (sales.size() != 0){
            crashMap.put(46, MarketButton.PAGE_NEXT);
        }
        for (int i = 0; i < 45; i++) {
            if(i < sales.size()){
                Sale sale = sales.get(i);
                ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
                ItemUtils.loreSaleItem(item, sale);
                crashMap.put(i, item);
            }else {
                crashMap.put(i, MarketButton.AIR.clone());
            }
        }
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
        if(next == null && page < 30){
            next = new SearchPage(title, target, page+1, size);
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
        // 上一页
        if (page == 1){
            itemMap.put(45, MarketButton.PAGE_PRE_DISABLE);
        }else {
            itemMap.put(45, MarketButton.PAGE_PRE);
        }
        // 下一页
        itemMap.put(46, MarketButton.PAGE_NEXT_DISABLE);

        itemMap.put(51, MarketButton.MARKET);
        itemMap.put(52, MarketButton.SEARCH);
        itemMap.put(53, MarketButton.CLOSE);
    }

    @Override
    public void updateTitle(String s) {

    }

}
