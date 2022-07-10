package com.fireflyest.market.view;

import com.fireflyest.gui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.core.MarketItem;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */

public class MailPage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

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
        if (page != 0) guiTitle += (" §7#§8" + page);          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, size, guiTitle);

        if(Config.SQL){
            sql = MysqlExecuteUtils.query(Mail.class, "owner", target,(page-1)*45, page*45);
        }else {
            sql = SqliteExecuteUtils.query(Mail.class, "owner", target,(page-1)*45, page*45);
        }

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap(){
        Map<Integer, ItemStack> itemStackMap = new HashMap<>(itemMap);
        List<Mail> mails = storage.inquiryList(sql, Mail.class);
        for (int i = 0; i < 45; i++) {
            if(i < mails.size()){
                Mail mail = mails.get(i);
                ItemStack item = SerializeUtil.deserialize(mail.getStack(), mail.getMeta());
                ItemUtils.loreMailItem(item, mail);
                itemStackMap.put(i, item);
            }else {
                itemStackMap.put(i, MarketItem.AIR.clone());
            }
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
        itemMap.put(45, MarketItem.MARKET);
        itemMap.put(46, MarketItem.SIGN);
        if(Config.PAGE_BUTTON_SPLIT){
            itemMap.put(48, MarketItem.getPrePageItem(page));
            itemMap.put(50, MarketItem.getNextPageItem(page));
        }else {
            itemMap.put(49, MarketItem.getPageItem(page));
        }
        itemMap.put(52, MarketItem.DATA);
        itemMap.put(53, MarketItem.CLOSE);
    }

}
