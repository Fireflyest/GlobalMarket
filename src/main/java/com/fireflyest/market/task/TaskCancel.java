package com.fireflyest.market.task;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.SerializeUtil;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskCancel extends Task{

    private final int id;

    public TaskCancel(@NotNull String playerName, int id) {
        super(playerName);
        this.id = id;

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
            this.executeInfo(Language.CANCEL_ERROR);
            return then;
        }

        // 解析物品
        ItemStack item = SerializeUtil.deserialize(sale.getStack(), sale.getMeta());
        if(!"null".equals(sale.getNbt()) && !"".equals(sale.getNbt())) ItemUtils.setItemValue(item, sale.getNbt());

        // 减少正在出售的数量
        User user = MarketManager.getUser(sale.getOwner());
        user.setSelling(user.getSelling() - 1);
        data.update(user);

        // 删除商品
        MarketManager.removeSale(sale);
        this.executeInfo(Language.CANCEL_ITEM);

        // 发送邮件
        then.add(new TaskSend(Language.MAIL_FROM_CANCEL, sale.getOwner(), item));

        return then;
    }
}
