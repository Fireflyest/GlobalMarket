package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
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

public class MainPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private final Storage storage;

    private final Inventory inventory;
    private final String title;
    private final String target;
    private final int page;
    private final int size;
    private final String sqlNormal;
    private final String sqlRetail;
    private final String sqlAuction;
    private final String sqlPoint;
    private final String sqlAdmin;

    private ViewPage next = null;
    private ViewPage pre = null;

    public MainPage(String title, String target, int page, int size) {
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

        if(Config.SQL){
            // sqlAll = MysqlExecuteUtils.query(Sale.class, (page-1)*45, page*45);
            sqlNormal = MysqlExecuteUtils.query(Sale.class, " where admin=0 order by id desc",(page-1)*45, page*45);
            sqlRetail = MysqlExecuteUtils.query(Sale.class, " where auction=0 order by id desc",(page-1)*45, page*45);
            sqlAuction = MysqlExecuteUtils.query(Sale.class, " where auction=1 order by id desc",(page-1)*45, page*45);
            sqlPoint = MysqlExecuteUtils.query(Sale.class, " where point=1 order by id desc", (page-1)*45, page*45);
            sqlAdmin = MysqlExecuteUtils.query(Sale.class, " where admin=1 order by id desc", (page-1)*45, page*45);
        }else {
            // sqlAll = SqliteExecuteUtils.query(Sale.class, (page-1)*45, page*45);
            sqlNormal = SqliteExecuteUtils.query(Sale.class, " where admin=0 order by id desc",(page-1)*45, page*45);
            sqlRetail = SqliteExecuteUtils.query(Sale.class, " where auction=0 order by id desc",(page-1)*45, page*45);
            sqlAuction = SqliteExecuteUtils.query(Sale.class, " where auction=1 order by id desc",(page-1)*45, page*45);
            sqlPoint = SqliteExecuteUtils.query(Sale.class, " where point=1 order by id desc", (page-1)*45, page*45);
            sqlAdmin = SqliteExecuteUtils.query(Sale.class, " where admin=1 order by id desc",  (page-1)*45, page*45);
        }

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        List<Sale> sales;
        if (target.equals(MainView.POINT)){
            sales = storage.inquiryList(sqlPoint, Sale.class);
        } else if (target.equals(MainView.ADMIN)) {
            sales = storage.inquiryList(sqlAdmin, Sale.class);
        } else if (target.equals(MainView.RETAIL)) {
            sales = storage.inquiryList(sqlRetail, Sale.class);
        } else if (target.equals(MainView.AUCTION)) {
            sales = storage.inquiryList(sqlAuction, Sale.class);
        } else {
            sales = storage.inquiryList(sqlNormal, Sale.class);
        }
        // 可以下一页
        if (sales.size() != 0){
            crashMap.put(46, MarketButton.PAGE_NEXT);
        }
        // 放置商品
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
            next = new MainPage(title, target, page+1, size);
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

        if (Config.DISABLE_CLASSIFY) {
            itemMap.put(51, MarketButton.MINE);
            itemMap.put(52, MarketButton.MAIL);
            itemMap.put(53, MarketButton.CLOSE);
        } else {
            itemMap.put(50, MarketButton.MINE);
            itemMap.put(51, MarketButton.MAIL);
            itemMap.put(52, MarketButton.CLASSIFY);
            itemMap.put(53, MarketButton.CLOSE);
        }


    }

    @Override
    public void updateTitle(String s) {

    }

}
