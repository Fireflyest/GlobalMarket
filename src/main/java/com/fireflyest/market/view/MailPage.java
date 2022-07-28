package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketButton;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.Storage;
import com.fireflyest.market.util.MysqlExecuteUtils;
import com.fireflyest.market.util.SqliteExecuteUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class MailPage implements ViewPage {

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

    public MailPage(String title, String target, int page, int size) {
        this.storage = GlobalMarket.getStorage();
        this.title = title;
        this.target = target;
        this.page = page;
        this.size = size;
        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + String.format(Language.MARKET_MAIL_NICK, target));    // 副标题

        // 界面容器
        this.inventory = Bukkit.createInventory(null, size, guiTitle);

        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Mail.class, "owner", target, 0, 512);
        }else {
            sql = SqliteExecuteUtils.query(Mail.class, "owner", target, 0, 512);
        }

        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        List<Mail> mails = storage.inquiryList(sql, Mail.class);
        List<Mail> items = mails.stream().filter(mail -> !mail.isRecord()).collect(Collectors.toList());
        List<Mail> records = mails.stream().filter(Mail::isRecord).collect(Collectors.toList());
        int i = 0, j = 0, k, l = 0, m = 0, limit = 2;
        if (records.size() == 0) {
            limit = 4;
        }else if (records.size() < 14){
            limit = 3;
        }
        for (Mail mail : items) {
            if (j > 6){ // 到末尾
                if (i < limit){ // 限制行以下就能再加一行，并且转到头
                    i++;
                    j = 0;
                }else { // 超过行了，退出
                    break;
                }
            }
            ItemStack item = SerializeUtil.deserialize(mail.getStack(), mail.getMeta());
            MarketButton.loreMailItem(item, mail);
            crashMap.put(i * 9 + 2 + j, item);
            m++;
            j++;
        }
        if (j != 0){ // 如果这行有东西但是没有满
            for (; j < 7; j++){ // 最后一行没有放满，填充空气
                crashMap.put(i * 9 + 2 + j, MarketButton.AIR.clone());
            }
        }
        if (m == 0){ // 如果没有物品直接首行开始
            k = 0;
        }else { // 有物品换行
            k = i+1;
        }
        for (Mail mail : records) {
            if (l > 6){ // 到末尾
                if (k < 5){ // 五行以下就能再加一行，并且转到头
                    k++;
                    l = 0;
                }else { // 已经五行了，退出
                    break;
                }
            }else if (k == 5 && l > 4){
                break;
            }
            ItemStack item = SerializeUtil.deserialize(mail.getStack(), mail.getMeta());
            MarketButton.loreMailItem(item, mail);
            crashMap.put(k * 9 + 2 + l, item);
            m++;
            l++;
        }
        while (k < 6){
            for (; l < 7; l++){
                if (k == 5 && l > 4){
                    break;
                }
                crashMap.put(k * 9 + 2 + l, MarketButton.AIR.clone());
            }
            l = 0;
            k++;
        }

        // 滞留数量
        ItemStack transport = MarketButton.TRANSPORT.clone();
        int stagnate = (mails.size() - m);
        if (stagnate > 0) {
            ItemUtils.addLore(transport, Language.MAIL_FULL.replace("%amount%", String.valueOf(stagnate)));
        }
        crashMap.put(53, transport);

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
            next = new MailPage(title, target, page+1, size);
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
        itemMap.put(0, MarketButton.MINE);
        itemMap.put(9, MarketButton.SIGN);
        itemMap.put(18, MarketButton.DELETE);
        itemMap.put(45, MarketButton.BACK);
    }

    @Override
    public void updateTitle(String s) {

    }

}
