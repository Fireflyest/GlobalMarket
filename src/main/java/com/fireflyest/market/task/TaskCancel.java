package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.YamlUtils;
import org.jetbrains.annotations.NotNull;

/**
 * 交易取消
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskCancel extends Task {

    private final long id;
    private final boolean refresh;
    private final MarketService service;
    private final ViewGuide guide;

    public TaskCancel(@NotNull UUID uid, MarketService service, ViewGuide guide, long id) {
        this(uid, service, guide, id, false);
    }

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param refresh 是否刷新页面
     */
    public TaskCancel(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, long id, boolean refresh) {
        super(uid);
        this.id = id;
        this.refresh = refresh;
        this.service = service;
        this.guide = guide;
    }

    @Override
    public void execute() {
        final String stack = service.selectTransactionStack(id);
        final String type = service.selectTransactionType(id);
        final String owner = service.selectTransactionOwner(id);
        final String currency = service.selectTransactionCurrency(id);
        final long category = service.selectTransactionCategory(id);

        if (StringUtils.isEmpty(stack)) {
            this.info(Language.ERROR_DATA.get());
            return;
        }

        // 判断操作者是否商品主人
        if (!StringUtils.equals(uid.toString(), owner) && !offlinePlayer.isOp()) {
            this.info(Language.ERROR_CANCEL.get());
            return;
        }

        // 非收购退还商品
        if (!"order".equals(type) && !"adminorder".equals(type)) {
            // 解析物品
            final ItemStack item = YamlUtils.deserializeItemStack(stack);
            // 发送邮件
            this.followTasks().add(
                new TaskSend(Language.GUI_MAIL_FROM_CANCEL.get(), service, guide, owner, item));
        }

        service.updateMerchantSelling("-1", owner);
        service.deleteTransaction(id);

        this.info(Language.SUCCEED_CANCEL.get());     

        if (refresh) {
            guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
            guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
            guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal", type, currency);
            guide.refreshPages(GlobalMarket.MINE_VIEW, owner);
            guide.refreshPages(GlobalMarket.STORE_VIEW);
            guide.refreshPages(GlobalMarket.VISIT_VIEW, owner);
            // 根据分类刷新页面，类型是按二进制存储的
            for (int i = 0; i < 8; i++) {
                if ((category & (1 << i)) != 0) {
                    guide.refreshPages(GlobalMarket.CATEGORY_VIEW, "category" + i);
                }
            }
        }
    }
}
