package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

/**
 * 完成交易
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskFinish extends Task {

    private final long id;

    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     * @param id 交易id
     */
    public TaskFinish(@NotNull UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide, long id) {
        super(uid);
        this.id = id;
        this.service = service;
        this.economy = economy;
        this.guide = guide;
    }

    @Override
    public void execute() {
        final Transaction transaction = service.selectTransactionById(id);

        if (null == transaction) {
            this.info(Language.ERROR_DATA.get());
            return;
        }
        if (!"auction".equals(transaction.getType())) {
            this.info(Language.ERROR_TYPE.get());
            return;
        }
        if (transaction.getOwner().equals(uid.toString())) {
            this.info(Language.ERROR_TRANSACTION.get());
            if (!Config.DEBUG.get().booleanValue()) {
                return;
            }
        }

        final ItemStack stack = YamlUtils.deserializeItemStack(transaction.getStack());
        final Player player = offlinePlayer.getPlayer();
        if (StringUtils.isEmpty(transaction.getTarget())) {
            // 流拍提示
            if (player != null) {
                final String info = Language.AUCTION_FLOW.get();
                final BaseComponent[] textItemStack = 
                    ChatUtils.textItemStack(stack, "/market mail");
                player.spigot().sendMessage(
                    new ComponentBuilder(info).append(textItemStack).create());
            }
            // 取消交易
            this.followTasks().add(new TaskCancel(uid, service, guide, id));
            return;
        }

        boolean hasMoney = false;
        String symbol = "";
        final UUID biderUid = UUID.fromString(transaction.getTarget());
        final OfflinePlayer bider = Bukkit.getOfflinePlayer(biderUid);
        switch (transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(bider, transaction.getCost());
                symbol = Language.SYMBOL_COIN.get();
                break;
            case "point":
                hasMoney = economy.getPlayerPoints().look(biderUid) >= transaction.getCost();
                symbol = Language.SYMBOL_POINT.get();
                break;
            case "item":
            default:
                break;
        }

        if (!hasMoney) {
            this.info(Language.TRANSACTION_FAIL.get().replace("%t%", transaction.getTarget()));
            service.updateMerchantCredit("-1", biderUid);
            this.followTasks().add(new TaskCancel(uid, service, guide, id));
            return;
        }

        // 买家扣钱
        switch (transaction.getCurrency()) {
            case "coin":
                economy.getEconomy().withdrawPlayer(bider, transaction.getCost());
                break;
            case "point":
                economy.getPlayerPoints().take(biderUid,  (int) Math.ceil(transaction.getCost()));
                break;
            case "item":
                break;
            default:
                break;
        }

        // 发送物品
        this.followTasks().add(new TaskSend(
            Language.GUI_MAIL_FROM_AUCTION.get(), service, guide, biderUid.toString(), stack));

        // 提示买家
        final Player biderPlayer = bider.getPlayer();
        if (biderPlayer != null) {
            biderPlayer.sendMessage(Language.SUCCEED_TRANSACTION.get());
        }

        // 删除交易
        service.deleteTransaction(id);

        // 发送交易记录
        final BaseComponent[] textItemStack = 
                ChatUtils.textItemStack(stack, "/market mail");
        final ItemStack recordBook = MarketItem.getRecordItem(
            textItemStack, biderUid.toString(), transaction.getCost(), symbol);
        final TaskSend taskSend = new TaskSend(
            Language.GUI_MAIL_FROM_REWARD.get(), 
            service, 
            guide, 
            biderUid.toString(), 
            recordBook, 
            transaction.getCost(), 
            transaction.getCurrency());
        taskSend.setExtra(transaction.getExtra());
        this.followTasks().add(taskSend);
        
        
        if (player != null) {
            final String info = Language.AUCTION_FINISH.get();
            player.spigot().sendMessage(
                new ComponentBuilder(info).append(textItemStack).create());
        }

        // 更新卖家统计数据
        service.updateMerchantSelling("-1", transaction.getOwner());
        service.updateMerchantAmount(transaction.getOwner());

        // 刷新页面
        this.refresh(transaction.getType(), transaction.getCurrency(), transaction.getCategory());
    }

    private void refresh(String type, String currency, long category) {
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

}
