package com.fireflyest.market.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.data.Pair;
import io.fireflyest.emberlib.inventory.Page;
import io.fireflyest.emberlib.inventory.Slot;
import io.fireflyest.emberlib.inventory.item.ItemBuilder;
import io.fireflyest.emberlib.util.YamlUtils;
import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

/**
 * 邮件页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class MailPage extends Page {

    private final Map<Long, ItemStack> itemsCache = new HashMap<>();
    private final Map<Long, Delivery> deliveryCache = new LinkedHashMap<>();

    private final MarketService service;
    private int stageAmount;

    protected MailPage(String target, int pageNumber, MarketService service) {
        super(target, pageNumber, 54);
        this.service = service;

        this.setup(Language.TITLE_MAIL.get());
    }

    @Override
    public void refreshPage() {
        if (!init) {
            this.initPage();
        }
        final int mailAmount = service.selectDeliveryCountByOwner(target);
        if (mailAmount == 0) { // 空邮箱清空缓存
            itemsCache.clear();
            deliveryCache.clear();
        }
        if (mailAmount < 10 || Math.abs(mailAmount - deliveryCache.size()) > 3) { // 实际数量与缓存数量差距超过3
            deliveryCache.clear();
            for (Delivery delivery : service.selectDeliveryByOwner(target)) {
                deliveryCache.put(delivery.getId(), delivery);
            }
        } else { // 数量差距小
            final UUID uid = UUID.fromString(target);
            final long[] ids = service.selectDeliveryIdByOwner(uid);
            final List<Long> idList = Arrays.stream(ids)
                                            .boxed()
                                            .collect(Collectors.toList());
            // 多的删除
            deliveryCache.entrySet().removeIf(entry -> !idList.contains(entry.getKey()));
            // 少的补上
            if (idList.size() > deliveryCache.size()) {
                for (long delivery : idList) {
                    if (!deliveryCache.containsKey(delivery)) {
                        deliveryCache.put(delivery, service.selectDeliveryById(delivery));
                    }
                }
            }
        }
        this.putMails();
    }

    @Override
    public void initPage() {
        super.initPage();
        final Pair<ItemBuilder, Slot> blank = MarketItem.getPair("blank");
        for (int i = 1; i < 53; i += 9) {
            this.slot(i, blank);
        }

        this.slot(0, MarketItem.getPair("mine"));
        this.slot(9, MarketItem.getPair("sign"));
        this.slot(18, MarketItem.getPair("destroy"));
        this.slot(45, MarketItem.getPair("back"));
    }

    @Override
    public Page getNext() {
        if (next == null && pageNumber < 30) {
            next = new MailPage(target, pageNumber + 1, service);
            next.setPre(this);
        }
        return next;
    }

    private void putMails() {
        // target为玩家uid
        final List<Delivery> items = new ArrayList<>();
        final List<Delivery> records = new ArrayList<>();
        for (Delivery delivery : deliveryCache.values()) {
            if (Language.GUI_MAIL_FROM_REWARD.get().equals(delivery.getSender())) {
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
        stageAmount = 0;
        final int line = this.putItems(items, limit);
        this.putRecords(records, line);

        // 滞留数量
        final ItemBuilder transport = MarketItem.getItemBuilder("transport");
        final int stagnate = (deliveryCache.size() - stageAmount);
        if (stagnate > 0) {
            transport.replace("amount", stagnate).amount(stagnate)
                .lore("§7" + Language.GUI_MAIL_STAGNATE.get(), 1);
        }
        this.slot(53, transport.build());
    }

    private int putItems(List<Delivery> items, int limit) {
        int i = 0;
        int j = 0;

        for (Delivery mail : items) {
            if (j > 6) { // 到末尾
                if (i >= limit) {
                    break;
                }
                i++;
                j = 0;
            }
            final ItemStack item = this.getItemCache(mail);
            this.slot(i * 9 + 2 + j, item);
            stageAmount++;
            j++;
        }
        if (j != 0) { // 如果这行有东西但是没有满
            for (; j < 7; j++) { // 最后一行没有放满，填充空气
                this.slot(i * 9 + 2 + j, MarketItem.getPair("air"));
            }
        }

        return stageAmount == 0 ? 0 : i + 1; // 如果没有物品直接首行开始
    }

    /**
     * 交易记录
     * @param records 数据
     * @param line 行
     */
    private void putRecords(List<Delivery> records, int line) {
        int l = 0;
        for (Delivery mail : records) {
            if ((l > 6 && line >= 5) || (line == 5 && l > 4)) {
                break;
            }
            if (l > 6) { // 到末尾
                line++;
                l = 0;
            }
            final ItemStack item = this.getItemCache(mail);
            this.slot(line * 9 + 2 + l, item);
            stageAmount++;
            l++;
        }
        while (line < 6) {
            for (; l < 7; l++) {
                if (line == 5 && l > 4) {
                    break;
                }
                this.slot(line * 9 + 2 + l, MarketItem.getPair("air"));
            }
            l = 0;
            line++;
        }
    }

    private ItemStack getItemCache(Delivery delivery) {
        return itemsCache.computeIfAbsent(delivery.getId(), k -> {
            final ItemStack item = YamlUtils.deserializeItemStack(delivery.getStack());
            MarketItem.loreMailData(item, delivery);
            return item;
        });
    }
    
}
