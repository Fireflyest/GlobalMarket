package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import com.fireflyest.market.data.Language;
import org.bukkit.Material;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.SerializeUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class AffairPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();

    private final Inventory inventory;
    private final String target;

    private Sale sale;

    public AffairPage(String title, String target) {
        this.target = target;
        this.sale = MarketManager.getSale(ConvertUtils.parseInt(target));

        String guiTitle = title;
        if (sale != null) {
            guiTitle += ("§9" + sale.getNickname() + " §7#§8" + sale.getId());
        }

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 27, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        sale = MarketManager.getSale(ConvertUtils.parseInt(target));
        if (sale == null) {
            crashMap.put(10, new ItemStack(Material.AIR));
            crashMap.put(13, new ItemStack(Material.AIR));
            crashMap.put(14, new ItemStack(Material.AIR));
            crashMap.put(15, new ItemStack(Material.AIR));
            crashMap.put(8, new ItemStack(Material.AIR));
            return crashMap;
        }
        // 展示物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        crashMap.put(10, item);

        String symbol = (sale.isPoint() ? Language.POINT_SYMBOL : Language.COIN_SYMBOL);

        if (sale.getPrice() != -1){
            // 添加交易操作按钮
            if(sale.isAuction()){
                // 拍卖
                ItemStack add1 = MarketButton.BID_10.clone();
                ItemUtils.addLore(add1, "§3§l增加§7:§f 10");
                ItemUtils.addLore(add1, "§3§l当前§7:§f "+sale.getCost() + symbol);
                ItemUtils.setItemValue(add1, "add "+sale.getId()+" 10");
                crashMap.put(13, add1);

                ItemStack add2 = MarketButton.BID_100.clone();
                ItemUtils.addLore(add2, "§3§l增加§7:§f 100");
                ItemUtils.addLore(add2, "§3§l当前§7:§f "+sale.getCost() + symbol);
                ItemUtils.setItemValue(add2, "add "+sale.getId()+" 100");
                crashMap.put(14, add2);

                ItemStack add3 = MarketButton.BID_1000.clone();
                ItemUtils.addLore(add3, "§3§l增加§7:§f 1000");
                ItemUtils.addLore(add3, "§3§l当前§7:§f "+sale.getCost() + symbol);
                ItemUtils.setItemValue(add3, "add "+sale.getId()+" 1000");
                crashMap.put(15, add3);
            }else {
                int amount = item.getAmount();
                if (Config.BUY_PARTIAL && !sale.isPoint()){
                    // 直售
                    ItemStack buy1 = MarketButton.BUY_1.clone();
                    ItemUtils.addLore(buy1, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()/amount) + symbol);
                    ItemUtils.setItemValue(buy1, "buy "+sale.getId()+" 1");
                    buy1.setAmount(1);
                    crashMap.put(13, buy1);
                    if(amount > 8){
                        ItemStack buy2 = MarketButton.BUY_8.clone();
                        ItemUtils.addLore(buy2, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()/amount * 8) + symbol);
                        ItemUtils.setItemValue(buy2, "buy "+sale.getId()+" 8");
                        buy2.setAmount(8);
                        crashMap.put(14, buy2);
                        ItemStack buy3 = MarketButton.BUY_ALL.clone();
                        ItemUtils.addLore(buy3, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()) + symbol);
                        ItemUtils.setItemValue(buy3, "buy "+sale.getId());
                        buy3.setAmount(amount);
                        crashMap.put(15, buy3);
                    }else {
                        ItemStack buy3 = MarketButton.BUY_ALL.clone();
                        ItemUtils.addLore(buy3, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()) + symbol);
                        ItemUtils.setItemValue(buy3, "buy "+sale.getId());
                        buy3.setAmount(amount);
                        crashMap.put(14, buy3);
                    }
                }else {
                    ItemStack buy3 = MarketButton.BUY_ALL.clone();
                    ItemUtils.addLore(buy3, "§3§l价格§7:§f "+ ConvertUtils.formatDouble(sale.getCost()) + symbol);
                    ItemUtils.setItemValue(buy3, "buy "+sale.getId());
                    buy3.setAmount(amount);
                    crashMap.put(14, buy3);
                }
            }
        }else {
            // 敬请期待
            crashMap.put(14, MarketButton.WAIT);
        }

        // 添加皮肤
        ItemStack head = crashMap.get(8);
        ItemUtils.setDisplayName(head, "§3§l" + String.format(Language.MARKET_OTHER_NICK, sale.getOwner()));
        ItemUtils.setSkullOwner(head, MarketManager.getOfflinePlayer(sale.getOwner()));
        ItemUtils.setItemValue(head, String.format("other %s", sale.getOwner()));

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
        itemMap.put(19, MarketButton.BLANK);
        itemMap.put(20, MarketButton.BLANK);

        ItemStack head = MarketButton.OTHER.clone();
        itemMap.put(8, head);
        itemMap.put(17, MarketButton.MARKET);
        itemMap.put(26, MarketButton.CLOSE);
    }

    @Override
    public void updateTitle(String s) {

    }

}
