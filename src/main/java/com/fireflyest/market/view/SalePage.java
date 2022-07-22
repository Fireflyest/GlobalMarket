package com.fireflyest.market.view;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.task.TaskEdit;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.SerializeUtil;
import org.fireflyest.craftgui.api.ViewPage;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class SalePage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private final Inventory inventory;
    private final String target;

    private Sale sale;

    private boolean done;
    private boolean auction = false;
    private boolean point = false;
    private String num = "";

    public SalePage(String title, String target) {
        this.target = target;
        this.sale = MarketManager.getSale(ConvertUtils.parseInt(target));

        String guiTitle = title;
        if (sale != null) {
            guiTitle += ("§7#§8" + sale.getId());
            point = sale.isPoint();
            auction = sale.isAuction();
        }

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 36, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        sale = MarketManager.getSale(ConvertUtils.parseInt(target));
        done = sale.getPrice() != -1;

        if (done){
            crashMap.put(19, MarketButton.EDIT.clone());
        }else {
            crashMap.put(19, MarketButton.DONE.clone());
        }

        if (sale == null) return crashMap;

        // 为了展示
        sale.setAuction(auction);
        sale.setPoint(point);
        double n = ConvertUtils.parseDouble(num);
        if (n != 0){
            sale.setCost(n);
            if (! done) sale.setPrice(n);
        }

        // 展示物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        MarketButton.loreSale(item, sale);
        crashMap.put(10, item);

        ItemStack saleType;
        if (auction){
            saleType = MarketButton.AUCTION.clone();
        }else {
            saleType = MarketButton.RETAIL.clone();
        }
        ItemUtils.setItemValue(saleType, "");
        crashMap.put(8, saleType);

        ItemStack moneyType;
        if (point){
            moneyType = MarketButton.POINT.clone();
        }else {
            moneyType = MarketButton.COIN.clone();
        }
        ItemUtils.setItemValue(moneyType, "");
        crashMap.put(17, moneyType);

        // 下架
        ItemStack cancel = crashMap.get(26);
        ItemUtils.setItemValue(cancel, String.format("cancel %s", sale.getId()));

        String end = (num.contains(".") || point ? "":"§7.0");
        ItemUtils.setLore(crashMap.get(4), "§7" + num + "§8§n7" + end, 0);
        ItemUtils.setLore(crashMap.get(5), "§7" + num + "§8§n8" + end, 0);
        ItemUtils.setLore(crashMap.get(6), "§7" + num + "§8§n9" + end, 0);
        ItemUtils.setLore(crashMap.get(13), "§7" + num + "§8§n4" + end, 0);
        ItemUtils.setLore(crashMap.get(14), "§7" + num + "§8§n5" + end, 0);
        ItemUtils.setLore(crashMap.get(15), "§7" + num + "§8§n6" + end, 0);
        ItemUtils.setLore(crashMap.get(22), "§7" + num + "§8§n1" + end, 0);
        ItemUtils.setLore(crashMap.get(23), "§7" + num + "§8§n2" + end, 0);
        ItemUtils.setLore(crashMap.get(24), "§7" + num + "§8§n3" + end, 0);
        ItemUtils.setLore(crashMap.get(32), "§7" + num + "§8§n0" + end, 0);

        // 点券出售不能小数
        if (point) {
            crashMap.remove(33);
        }else {
            if (num.endsWith(".")) {
                ItemUtils.setLore(crashMap.get(33), "§7" + num + "§70", 0);
            }else if (!num.contains(".")){
                ItemUtils.setLore(crashMap.get(33), "§7" + num + "§9.§70", 0);
            }
        }

        return crashMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new ConcurrentHashMap<>(itemMap);
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        switch (slot){
            case 4:
                num += "7";
                break;
            case 5:
                num += "8";
                break;
            case 6:
                num += "9";
                break;
            case 13:
                num += "4";
                break;
            case 14:
                num += "5";
                break;
            case 15:
                num += "6";
                break;
            case 22:
                num += "1";
                break;
            case 23:
                num += "2";
                break;
            case 24:
                num += "3";
                break;
            case 32:
                num += "0";
                break;
            case 33:
                if (!num.contains(".")) num += ".";
                break;
            case 8:
                if (!done) auction = !auction;
                break;
            case 17:
                if (!done && Config.POINT) {
                    point = !point;
                    if (point){
                        // 点券不能有小数
                        if (num.contains(".")) num = num.substring(0, num.indexOf("."));
                    }
                }
                break;
            case 31:
                if (num.length() > 0) num = num.substring(0, num.length()-1);
                break;
            case 19:
                if (sale == null) break;
                MarketTasks.getTaskManager().putTask(
                        new TaskEdit(sale.getOwner(), sale.getId(), auction, point, ConvertUtils.parseDouble(num)));
                break;
            default:
        }
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
        return 0;
    }

    @Override
    public ViewPage getNext() {
        return null;
    }

    @Override
    public ViewPage getPre() {
        return null;
    }

    @Override
    public void setPre(ViewPage pre) {
    }

    @Override
    public void setNext(ViewPage viewPage) {
    }

    @Override
    public void refreshPage() {
        itemMap.put(0, MarketButton.BLANK);
        itemMap.put(1, MarketButton.BLANK);
        itemMap.put(2, MarketButton.BLANK);
        itemMap.put(9, MarketButton.BLANK);
        itemMap.put(11, MarketButton.BLANK);
        itemMap.put(18, MarketButton.BLANK);
        itemMap.put(20, MarketButton.BLANK);
        itemMap.put(27, MarketButton.BLANK);
        itemMap.put(28, MarketButton.BLANK);
        itemMap.put(29, MarketButton.BLANK);

        itemMap.put(4, MarketButton.NUM7.clone());
        itemMap.put(5, MarketButton.NUM8.clone());
        itemMap.put(6, MarketButton.NUM9.clone());
        itemMap.put(13, MarketButton.NUM4.clone());
        itemMap.put(14, MarketButton.NUM5.clone());
        itemMap.put(15, MarketButton.NUM6.clone());
        itemMap.put(22, MarketButton.NUM1.clone());
        itemMap.put(23, MarketButton.NUM2.clone());
        itemMap.put(24, MarketButton.NUM3.clone());
        itemMap.put(31, MarketButton.CLEAR.clone());
        itemMap.put(32, MarketButton.NUM0.clone());
        itemMap.put(33, MarketButton.DOT.clone());

        itemMap.put(26, MarketButton.CANCEL.clone());
        itemMap.put(35, MarketButton.MINE);
    }

    @Override
    public void updateTitle(String s) {

    }

}
