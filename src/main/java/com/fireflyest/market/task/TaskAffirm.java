package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * 确认交易任务
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskAffirm extends Task {

    private final long id;
    private final String type;
    private final String currency;
    private final String extra;
    private final MarketService service;
    private final ViewGuide guide;
    private final boolean updateCurrency;

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param type 交易类型
     * @param currency 交易货币
     * @param extra 交易额外信息
     */
    public TaskAffirm(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, long id, String type, String currency, String extra) {
        super(uid);
        this.id = id;
        this.type = type;
        this.currency = currency;
        this.extra = extra;
        this.service = service;
        this.guide = guide;
        this.updateCurrency = false;
    }

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param guide 导航
     * @param id 交易id
     * @param extra 交易额外信息
     */
    public TaskAffirm(@NotNull UUID uid, MarketService service, 
            ViewGuide guide, long id, String extra) {
        super(uid);
        this.id = id;
        this.type = null;
        this.currency = null;
        this.extra = extra;
        this.service = service;
        this.guide = guide;
        this.updateCurrency = true;
    }

    @Override
    public void execute() {
        final String transactionType = service.selectTransactionType(id);

        if (StringUtils.isEmpty(transactionType)) {
            this.info(Language.ERROR_DATA.get());
            return;
        }
        if (!"prepare".equals(transactionType) && !updateCurrency) {
            this.info(Language.ERROR_TYPE.get());
            return;
        }

        if (!service.selectTransactionOwner(id).equals(uid.toString())) {
            this.info(Language.ERROR_TRANSACTION.get());
            return;
        }

        ItemStack item = null;
        if (type != null) {
            service.updateTransactionType(type, id);
            Player player = null;
            // 收购类型要设置数量
            if ("order".equals(type) && (player = offlinePlayer.getPlayer()) != null) {
                item = YamlUtils.deserializeItemStack(service.selectTransactionStack(id));
                service.updateTransactionDesc(String.valueOf(item.getAmount()), id);
                player.getInventory().addItem(item);
            }
        }
        if (currency != null) {
            service.updateTransactionCurrency(currency, id);
        }
        if (extra != null) {
            service.updateTransactionExtra(extra, id);
        }

        if (Config.TRANSACTION_BROADCAST.get().booleanValue() && !updateCurrency) {
            if (item == null) {
                item = YamlUtils.deserializeItemStack(service.selectTransactionStack(id));
            }
            final String info = 
                Language.TRANSACTION_BROADCAST.get().replace("%p%", this.getPlayerName());
            final BaseComponent[] textItemStack = 
                ChatUtils.textItemStack(item, String.format("/market affair %s", id));
            Bukkit.spigot().broadcast(new ComponentBuilder(info).append(textItemStack).create());
        }
        
        // 刷新交易界面
        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.CATEGORY_VIEW);
        guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal", type, currency);
        guide.refreshPages(GlobalMarket.MINE_VIEW, uid.toString());
        guide.refreshPages(GlobalMarket.STORE_VIEW);
        guide.refreshPages(GlobalMarket.VISIT_VIEW, uid.toString());
        
    }
}
