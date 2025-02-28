package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.MaterialLocale;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ItemUtils;
import io.fireflyest.emberlib.util.TimeUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 创建交易任务
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskCreate extends Task {

    private final MarketService service;
    private final ViewGuide guide;
    private final String type;
    private final String currency;
    private final ItemStack item;
    private final double price;

    private String desc;

    /**
     * 构造任务
     * 
     * @param uid 玩家uuid
     * @param service 服务
     * @param guide 导航
     * @param type 类型
     * @param currency 货币
     * @param price 价格
     * @param item 物品
     */
    public TaskCreate(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, String type, String currency, double price, ItemStack item) {
        super(uid);
        this.service = service;
        this.guide = guide;
        this.type = type;
        this.currency = currency;
        this.price = price;
        this.item = item;
    }

    @Override
    public void execute() {
        // 玩家必须在线
        final Player player = offlinePlayer.getPlayer();
        if (player == null) {
            return;
        }
        // 是否黑名单
        if (service.selectMerchantBlack(uid)) {
            this.info(Language.ERROR_CREATE.get());
            return;
        }
        // 交易数量是否上限
        if (Config.TRANSACTION_LIMIT.get().booleanValue()) {
            final int size = 
                Config.TRANSACTION_MAXIMUM.get() + service.selectMerchantSizeByUid(uid);
            if (service.selectMerchantSelling(uid) >= size) {
                this.info(Language.TRANSACTION_MAXIMUM.get());
                player.getInventory().addItem(item);
                return;
            }
        }

        // 邮箱数量限制
        if (Config.MAIL_LIMIT.get().booleanValue() 
                && service.selectDeliveryCountByOwner(uid.toString()) > Config.MAIL_MAXIMUM.get()) {
            this.info(Language.MAIL_MAXIMUM.get());
            player.getInventory().addItem(item);
            return;
        }

        final String stack = YamlUtils.serializeItemStack(item);
        // 是否违禁品
        if (this.contraband(stack)) {
            this.info(Language.TRANSACTION_CONTRABAND.get());
            player.getInventory().addItem(item);
            return;
        }
        
        // 插入数据
        final long id = service.insertTransaction(stack, uid, price, TimeUtils.getTime());
        if (desc != null) {
            service.updateTransactionDesc(desc, id);
        }
        String nickname = ItemUtils.getDisplayName(item);
        if (StringUtils.isEmpty(nickname)) {
            nickname = MaterialLocale.translate(item.getType(), Config.LANG.get());
        }
        service.updateTransactionNickname(nickname, id);

        this.info(Language.TRANSACTION_CREATE.get());

        // 确认发售
        if (!"prepare".equals(type)) {
            this.followTasks().add(new TaskAffirm(uid, service, guide, id, type, currency, ""));
        }

        // 增加正在出售的数量
        service.updateMerchantSelling("+1", player.getUniqueId().toString());

        // 自动分类
        long category = 0;
        if (Config.MARKET_CATEGORY.get().booleanValue()) {
            // TODO: 
        }

        // 刷新界面
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal", type, currency);
        guide.refreshPages(GlobalMarket.MINE_VIEW, uid.toString());
        guide.refreshPages(GlobalMarket.STORE_VIEW);
        guide.refreshPages(GlobalMarket.VISIT_VIEW, uid.toString());
        // 根据分类刷新页面，类型是按二进制存储的
        for (int i = 0; i < 8; i++) {
            if ((category & (1 << i)) != 0) {
                guide.refreshPages(GlobalMarket.CATEGORY_VIEW, "category" + i);
            }
        }
    }

    private boolean contraband(String stack) {
        if (Config.CONTRABAND_ENABLE.get().booleanValue()) {
            for (String lore : Config.CONTRABAND_LORE.get().split(",")) {
                if (stack.contains(lore)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
