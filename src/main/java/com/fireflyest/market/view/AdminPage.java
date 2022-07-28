package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class AdminPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();
    private final Map<Integer, ItemStack> crashMap = new HashMap<>();
    private final String title;
    private final String target;
    private final int page;
    private ViewPage next = null;
    private ViewPage pre = null;

    private final Inventory inventory;

    public AdminPage(String title, String target, int page) {
        this.title = title;
        this.target = target;
        this.page = page;

        String guiTitle = title;
        guiTitle += ("§9" + target);
        guiTitle += (" §7#§8" + page);          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, 54, guiTitle);

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        int pos = 18, num = -1;
        for (Field field : MarketButton.class.getDeclaredFields()) {
            if (field.getType() != ItemStack.class || "AIR".equals(field.getName())) continue;
            num++;
            if (num < (page-1)*33){
                continue;
            }
            String key = field.getName().toLowerCase();
            ItemStack button;
            try {
                button = ((ItemStack) field.get(null)).clone();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            ItemUtils.setItemValue(button, "button " + key);
            crashMap.put(pos, button);
            pos++;
            if (pos > 50) break;
        }

        // 可以下一页
        if (pos > 18){
            crashMap.put(53, MarketButton.PAGE_NEXT);
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
        return HomeView.NORMAL;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 30){
            next = new AdminPage(title, target, page+1);
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
    public void setNext(ViewPage next) {
        this.next = next;
    }

    @Override
    public void refreshPage() {
        for (int i = 9; i < 18; i++) itemMap.put(i, MarketButton.BLANK);
        // 上一页
        if (page == 1){
            itemMap.put(52, MarketButton.PAGE_PRE_DISABLE);
        }else {
            itemMap.put(52, MarketButton.PAGE_PRE);
        }
        // 下一页
        itemMap.put(53, MarketButton.PAGE_NEXT_DISABLE);

        itemMap.put(0, MarketButton.STATISTIC);
        itemMap.put(8, MarketButton.BACK.clone());
    }

    @Override
    public void updateTitle(String s) {

    }

}
