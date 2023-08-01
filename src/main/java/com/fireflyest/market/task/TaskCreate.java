package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.util.TranslateUtils;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.util.ItemUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;
import org.jetbrains.annotations.NotNull;


public class TaskCreate extends Task {

    private final MarketService service;
    private final ViewGuide guide;
    private final String type;
    private final String currency;
    private final ItemStack item;
    private final double price;

    private String desc;

    public TaskCreate(@NotNull String playerName, MarketService service, ViewGuide guide, String type, String currency, double price, ItemStack item) {
        super(playerName);
        this.service = service;
        this.guide = guide;
        this.type = type;
        this.currency = currency;
        this.price = price;
        this.item = item;
    }

    @Override
    public void execute() {
        if (player == null || !player.isOnline()) {
            return;
        }

        // 是否黑名单
        if (service.selectMerchantBlack(player.getUniqueId())) {
            this.executeInfo(Language.CREATE_ERROR);
            return;
        }

        // 交易数量是否上限
        if (Config.MAXIMUM_SALE) {
            int size = Config.MAXIMUM_SALE_NUM + service.selectMerchantSize(player.getName());
            if (service.selectMerchantSelling(player.getUniqueId()) >= size) {
                this.executeInfo(Language.MAXIMUM_TRANSACTION);
                player.getInventory().addItem(item);
                return;
            }
        }

        // 邮箱数量限制
        if (Config.MAXIMUM_MAIL && service.selectDeliveryIdByOwner(player.getUniqueId()).length > Config.MAXIMUM_MAIL_NUM) {
            this.executeInfo(Language.MAXIMUM_MAIL);
            player.getInventory().addItem(item);
            return;
        }

        String stack = SerializationUtil.serializeItemStack(item);

        // 是否违禁品
        if (Config.CONTRABAND_LORE) {
            for (String lore : Config.CONTRABAND_LORE_LIST.split(",")) {
                if (stack.contains(lore)) {
                    this.executeInfo(Language.TRANSACTION_CONTRABAND);
                    player.getInventory().addItem(item);
                    return;
                }
            }
        }

         // 获取物品名称
         String itemName = ItemUtils.getDisplayName(item);
         if("".equals(itemName)) {
             itemName = TranslateUtils.translate(item.getType());
         }
        
        // 插入数据
        long id = service.insertTransaction(stack, player.getUniqueId(), playerName, itemName, price, TimeUtils.getTime());
        if (desc != null) {
            service.updateTransactionDesc(desc, id);
        }

        // 确认发售
        if (!"prepare".equals(type)) {
            this.followTasks().add(new TaskAffirm(playerName, service, guide, id, type, currency, ""));
        }

        // 增加正在出售的数量
        service.updateMerchantSelling("+1", player.getUniqueId().toString());

        // 刷新界面
        guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal");
        guide.refreshPage(playerName);
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
