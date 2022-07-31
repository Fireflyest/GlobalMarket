package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskCancel extends Task{

    private final int id;
    private final boolean reflash;

    public TaskCancel(@NotNull String playerName, int id) {
       this(playerName, id, false);
    }

    public TaskCancel(@NotNull String playerName, int id, boolean reflash) {
        super(playerName);
        this.id = id;
        this.reflash = reflash;

        this.type = MarketTasks.SALE_TASK;
    }

    @Override
    public @NotNull List<Task> execute() {
        Sale sale = MarketManager.getSale(id);

        // 判断商品是否存在
        if(null == sale){
            this.executeInfo(Language.DATA_NULL);
            return then;
        }
        // 判断操作者是否商品主人
        if(!sale.getOwner().equals(playerName)){
            guide.refreshPage(playerName);
            this.executeInfo(Language.CANCEL_ERROR);
            return then;
        }

        // 解析物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());

        // 减少正在出售的数量
        User user = MarketManager.getUser(sale.getOwner());
        user.setSelling(user.getSelling() - 1);
        data.update(user);

        // 删除商品
        MarketManager.removeSale(sale);
        this.executeInfo(Language.CANCEL_ITEM);

        // 发送邮件
        then.add(new TaskSend(Language.MAIL_FROM_CANCEL, sale.getOwner(), item));

        if (reflash) guide.refreshPage(playerName);

        return then;
    }
}
