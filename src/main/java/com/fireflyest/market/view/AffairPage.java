package com.fireflyest.market.view;

import com.fireflyest.gui.api.ViewPage;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.ItemUtils;
import com.fireflyest.market.util.SerializeUtil;
import com.fireflyest.market.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class AffairPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

    private final Inventory inventory;
    private final String target;

    private Sale sale;

    public AffairPage(String title, String target) {
        this.target = target;
        this.sale = MarketManager.getSale(ConvertUtils.parseInt(target));

        String guiTitle = title;
        guiTitle += ("§9" + sale.getNickname() + " §7#§8" + sale.getId());

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 27, guiTitle);

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap(){
        Map<Integer, ItemStack> itemStackMap = new HashMap<>(itemMap);
        sale = MarketManager.getSale(ConvertUtils.parseInt(target));
        if (sale == null) {
            return itemStackMap;
        }
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        // 展示物品
        itemMap.put(10, item);

        // 下架按钮
        ItemStack cancel;
        if(sale.isAuction() && sale.getPrice() != sale.getCost()){
            cancel = MarketItem.BOOK.clone();
            ItemUtils.setDisplayName(cancel, "§e§l成交");
            ItemUtils.addLore(cancel, "§3§l售价§7:§f "+sale.getCost());
            ItemUtils.setItemValue(cancel, "finish "+sale.getId());
        }else {
            cancel = MarketItem.CANCEL.clone();
            ItemUtils.setDisplayName(cancel, "§c§l下架");
            ItemUtils.addLore(cancel, "§3§l卖家§7:§f "+sale.getOwner());
            ItemUtils.addLore(cancel, "§3§l上架于§7:§f "+ TimeUtils.getTime(sale.getAppear()));
            ItemUtils.setItemValue(cancel, "cancel "+sale.getId());
        }
        itemMap.put(17, cancel);

        // 添加交易操作按钮
        if(sale.isAuction()){
            // 拍卖
            ItemStack add1 = MarketItem.ADD_NUGGET.clone();
            ItemUtils.setDisplayName(add1, "§e§l加价");
            ItemUtils.addLore(add1, "§3§l增加§7:§f 10");
            ItemUtils.addLore(add1, "§3§l当前§7:§f "+sale.getCost());
            ItemUtils.setItemValue(add1, "add "+sale.getId()+" 10");
            itemMap.put(13, add1);

            ItemStack add2 = MarketItem.ADD_INGOT.clone();
            ItemUtils.setDisplayName(add2, "§e§l加价");
            ItemUtils.addLore(add2, "§3§l增加§7:§f 100");
            ItemUtils.addLore(add2, "§3§l当前§7:§f "+sale.getCost());
            ItemUtils.setItemValue(add2, "add "+sale.getId()+" 100");
            itemMap.put(14, add2);

            ItemStack add3 = MarketItem.ADD_BLOCK.clone();
            ItemUtils.setDisplayName(add3, "§e§l加价");
            ItemUtils.addLore(add3, "§3§l增加§7:§f 1000");
            ItemUtils.addLore(add3, "§3§l当前§7:§f "+sale.getCost());
            ItemUtils.setItemValue(add3, "add "+sale.getId()+" 1000");
            itemMap.put(15, add3);
        }else {
            // 直售
            int amount = item.getAmount();
            ItemStack buy1 = MarketItem.BUY_1.clone();
            ItemUtils.setDisplayName(buy1, "§e§l单件");
            ItemUtils.addLore(buy1, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()/amount));
            ItemUtils.setItemValue(buy1, "buy "+sale.getId()+" 1");
            buy1.setAmount(1);
            itemMap.put(13, buy1);
            if(amount > 8){
                ItemStack buy2 = MarketItem.BUY_8.clone();
                ItemUtils.setDisplayName(buy2, "§e§l部分");
                ItemUtils.addLore(buy2, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()/amount * 8));
                ItemUtils.setItemValue(buy2, "buy "+sale.getId()+" 8");
                buy2.setAmount(8);
                itemMap.put(14, buy2);
            }
            ItemStack buy3 = MarketItem.BUY_ALL.clone();
            ItemUtils.setDisplayName(buy3, "§e§l一口价");
            ItemUtils.addLore(buy3, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()));
            ItemUtils.setItemValue(buy3, "buy "+sale.getId());
            buy3.setAmount(amount);
            itemMap.put(15, buy3);
        }
        return itemStackMap;
    }

    @Override
    public Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
    }

    @Override
    public Inventory getInventory(){
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
        itemMap.put(0, MarketItem.BLANK);
        itemMap.put(1, MarketItem.BLANK);
        itemMap.put(2, MarketItem.BLANK);
        itemMap.put(9, MarketItem.BLANK);
        itemMap.put(11, MarketItem.BLANK);
        itemMap.put(18, MarketItem.BLANK);
        itemMap.put(19, MarketItem.BLANK);
        itemMap.put(20, MarketItem.BLANK);

        itemMap.put(26, MarketItem.MARKET.clone());

        sale = MarketManager.getSale(ConvertUtils.parseInt(target));
        if (sale == null) {
            return;
        }
        ItemStack other = new ItemStack(MarketItem.MINE.getType());
        ItemUtils.setDisplayName(other, "§e§l个人商店");
        ItemUtils.addLore(other, "§f点击查看该商品主人的商店");
        ItemUtils.setItemValue(other, "other "+sale.getOwner());
        itemMap.put(8, other);
    }

}
