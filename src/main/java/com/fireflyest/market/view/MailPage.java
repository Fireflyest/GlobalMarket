package com.fireflyest.market.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.view.TemplatePage;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;

import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class MailPage extends TemplatePage {

    private final MarketService service;
    private final MarketYaml yaml;
    private int amount;

    protected MailPage(String target, int page, MarketService service, MarketYaml yaml) {
        super(Language.TITLE_MAIL_PAGE, target, page, 54);
        this.service = service;
        this.yaml = yaml;

        this.refreshPage();
    }

    @Override
    public Map<Integer, ItemStack> getItemMap() {
        asyncButtonMap.clear();
        asyncButtonMap.putAll(buttonMap);

        // target为玩家uid
        Delivery[] deliveries = service.selectDeliveryByOwner(target);;
        List<Delivery> items = new ArrayList<>();
        List<Delivery> records = new ArrayList<>();
        for (Delivery delivery : deliveries) {
            if (Language.TEXT_MAIL_FROM_REWARD.equals(delivery.getSender())) {
                records.add(delivery);
            } else {
                items.add(delivery);
            }
        }

        int limit = 2;
        if (records.isEmpty()) {
            limit = 4;
        } else if (records.size() < 14) {
            limit = 3;
        }
        amount = 0;
        int line = this.putItems(items, limit);
        this.putRecords(records, line);

        // 滞留数量
        ItemStack transport = yaml.getItemBuilder("transport").build();
        int stagnate = (deliveries.length - amount);
        if (stagnate > 0) {
            ItemUtils.addLore(transport, Language.TEXT_MAIL_STAGNATE.replace("%amount%", String.valueOf(stagnate)));
            transport.setAmount(stagnate);
        }
        asyncButtonMap.put(53, transport);

        return asyncButtonMap;
    }

    @Override
    public void refreshPage() {
        ItemStack blank = yaml.getItemBuilder("blank").build();
        for (int i = 1; i < 53; i+=9){
            buttonMap.put(i, blank);
        }

        buttonMap.put(0, yaml.getItemBuilder("mine").build());
        buttonMap.put(9, yaml.getItemBuilder("sign").build());
        buttonMap.put(18, yaml.getItemBuilder("destroy").build());
        buttonMap.put(45, yaml.getItemBuilder("back").build());
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 30){
            next = new MailPage(target, page + 1, service, yaml);
            next.setPre(this);
        }
        return next;
    }

    private int putItems(List<Delivery> items, int limit) {
        int i = 0, j = 0;
      
        for (Delivery mail : items) {
            if (j > 6){ // 到末尾
                if (i >= limit) {
                    break;
                }
                i++;
                j = 0;
            }
            ItemStack item = SerializationUtil.deserializeItemStack(mail.getStack());
            MarketItem.loreMailData(item, mail);
            asyncButtonMap.put(i * 9 + 2 + j, item);
            amount++;
            j++;
        }
        if (j != 0){ // 如果这行有东西但是没有满
            for (; j < 7; j++){ // 最后一行没有放满，填充空气
                asyncButtonMap.put(i * 9 + 2 + j, yaml.getItemBuilder("air").build());
            }
        }

        return amount == 0 ? 0 : i + 1; // 如果没有物品直接首行开始
    }

    /**
     * 交易记录
     * @param records 数据
     * @param line 行
     */
    private void putRecords(List<Delivery> records, int line) {
        int l = 0;
        for (Delivery mail : records) {
            if (l > 6){ // 到末尾
                if (line >= 5) { // 五行以下就能再加一行，并且转到头
                    break;
                }
                line++;
                l = 0;
            }
            if (line == 5 && l > 4){
                break;
            }
            ItemStack item = SerializationUtil.deserializeItemStack(mail.getStack());
            MarketItem.loreMailData(item, mail);
            asyncButtonMap.put(line * 9 + 2 + l, item);
            amount++;
            l++;
        }
        while (line < 6){
            for (; l < 7; l++){
                if (line == 5 && l > 4){
                    break;
                }
                asyncButtonMap.put(line * 9 + 2 + l, yaml.getItemBuilder("air").build());
            }
            l = 0;
            line++;
        }
    }
    
}
