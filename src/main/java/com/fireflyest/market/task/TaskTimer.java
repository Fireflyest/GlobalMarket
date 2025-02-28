package com.fireflyest.market.task;

import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.fireflyest.market.bean.Addend;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.TextUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * 市场定时任务
 * 
 * @author Fireflyest
 * @since 4.0
 */
public class TaskTimer extends Task {

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;
    private final long[] ids;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     * @param ids 交易id
     */
    public TaskTimer(UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide, long[] ids) {
        super(uid);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.ids = ids;
    }

    @Override
    public void execute() {
        int num = 0;
        for (long id : ids) {
            num++;
            final UUID owner = UUID.fromString(service.selectTransactionOwner(id));
            // 拍卖确认一次
            if ("auction".equals(service.selectTransactionType(id))) {
                this.processAutcion(id);
            }
            // 商品降1热度
            this.followTasks().add(
                new TaskHeat(owner, service, guide, id, -1, num == ids.length));
            
        }
    }
    
    private void processAutcion(long id) {
        final String desc = service.selectTransactionDesc(id);
        final Addend ad;
        if (StringUtils.isEmpty(desc)) {
            ad = new Addend("auction");
            ad.setNum(3);
        } else {
            ad = TextUtils.jsonToObj(desc, Addend.class);
        }
        // 更新确认次数
        ad.setNum(ad.getNum() - 1);
        // 通知出价者
        final Set<String> biders = ad.getStrings();
        if (!biders.isEmpty()) {
            final ItemStack item = 
                YamlUtils.deserializeItemStack(service.selectTransactionStack(id));
            final String info = Language.BID_CONFIRM.get()
                .replace("%n%", String.valueOf(3 - ad.getNum()));
            final BaseComponent[] textItemStack = 
                ChatUtils.textItemStack(item, String.format("/market affair %s", id));
            for (String bider : biders) {
                final Player pBider = Bukkit.getPlayerExact(bider);
                if (pBider == null) {
                    continue;
                }
                pBider.spigot()
                    .sendMessage(new ComponentBuilder(info).append(textItemStack).create());
            }
        }
        // 确认次数为0，结束拍卖
        if (ad.getNum() <= 0) {
            this.followTasks().add(new TaskFinish(uid, service, economy, guide, id));
        } else {
            service.updateTransactionDesc(TextUtils.toJson(ad), id);
        }
    }

}
