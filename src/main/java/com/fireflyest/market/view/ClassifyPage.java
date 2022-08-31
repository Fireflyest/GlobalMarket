package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.data.Language;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Storage;
import com.fireflyest.market.util.MysqlExecuteUtils;
import com.fireflyest.market.util.SqliteExecuteUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class ClassifyPage implements ViewPage {

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

    public ClassifyPage(String title, String target, int page, int size) {
        this.storage = GlobalMarket.getStorage();
        this.title = title;
        this.target = target;
        this.page = page;
        this.size = size;
        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + this.getSubtitle(target));    // 副标题
        if (page != 0) guiTitle += (" §7#§8" + page);          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, size, guiTitle);

        String condition = String.format(" where classify like '%s'", "%" + target +"%");
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
        // 放置物品
        for (int i = 0; i < 45; i++) {
            if(i < sales.size()){
                Sale sale = sales.get(i);
                ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
                MarketButton.loreSaleItem(item, sale);
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
            next = new ClassifyPage(title, target, page+1, size);
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
        itemMap.put(52, MarketButton.CLASSIFY);
        itemMap.put(53, MarketButton.CLOSE);
    }

    @Override
    public void updateTitle(String s) {

    }

    private String getSubtitle(String target){
        switch (target){
            case ClassifyView.EDIBLE:
                return Language.CLASSIFY_EDIBLE_TITLE;
            case ClassifyView.BLOCK:
                return Language.CLASSIFY_BLOCK_TITLE;
            case ClassifyView.BURNABLE:
                return Language.CLASSIFY_BURNABLE_TITLE;
            case ClassifyView.EQUIP:
                return Language.CLASSIFY_EQUIP_TITLE;
            case ClassifyView.ITEM:
                return Language.CLASSIFY_ITEM_TITLE;
            case ClassifyView.KNOWLEDGE:
                return Language.CLASSIFY_KNOWLEDGE_TITLE;
            default:
                return "";
        }
    }

}
