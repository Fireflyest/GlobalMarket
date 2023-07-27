package com.fireflyest.market.task;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.util.SerializationUtil;
import org.jetbrains.annotations.NotNull;

public class TaskCancel extends Task{

    private final long id;
    private final boolean refresh;
    private final MarketService service;
    private final ViewGuide guide;

    public TaskCancel(@NotNull String playerName, MarketService service, ViewGuide guide, long id) {
       this(playerName, service, guide, id, false);
    }

    public TaskCancel(@NotNull String playerName, MarketService service, ViewGuide guide, long id, boolean refresh) {
        super(playerName);
        this.id = id;
        this.refresh = refresh;
        this.service = service;
        this.guide = guide;
    }

    @Override
    public void execute() {
        String stack = service.selectTransactionStack(id);
        String type = service.selectTransactionType(id);
        String uid = service.selectMerchantUid(playerName);
        String owner = service.selectTransactionOwner(id);

        if("".equals(stack)){
            executeInfo(Language.DATA_ERROR);
            return;
        }

        // 判断操作者是否商品主人
        if(!owner.equals(uid) && (player != null && !player.isOp())){
            guide.refreshPage(playerName);
            this.executeInfo(Language.CANCEL_ERROR);
            return;
        }

        // 非收购退还商品
        if (!"order".equals(type) && !"adminorder".equals(type)) {
            // 解析物品
            ItemStack item = SerializationUtil.deserializeItemStack(stack);
            // 发送邮件
            this.followTasks().add(new TaskSend(Language.TEXT_MAIL_FROM_CANCEL, service, owner, item));
        }

        service.updateMerchantSelling("-1", owner);
        service.deleteTransaction(id);

        this.executeInfo(Language.CANCEL_SUCCEED);     

        if (refresh) guide.refreshPage(playerName);
    }
}
