package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Addend;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketEconomy;
import com.fireflyest.market.service.MarketService;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.inventory.ViewGuide;
import io.fireflyest.emberlib.task.Task;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.TextUtils;
import io.fireflyest.emberlib.util.YamlUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import java.util.UUID;

/**
 * 对拍卖商品出价
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class TaskBid extends Task {

    /**
     * 交易类型为拍卖
     */
    public static final String AUCTION = "auction";

    private final int id;
    private final int num;
    private final MarketService service;
    private final MarketEconomy economy;
    private final ViewGuide guide;

    private final Gson gson = new Gson();

    /**
     * 构造任务
     * 
     * @param uid 玩家uid
     * @param service 服务
     * @param economy 经济
     * @param guide 导航
     * @param id 交易id
     * @param num 出价
     */
    public TaskBid(@NotNull UUID uid, MarketService service, 
            MarketEconomy economy, ViewGuide guide, int id, int num) {
        super(uid);
        this.service = service;
        this.economy = economy;
        this.guide = guide;
        this.id = id;
        this.num = num;
    }

    @Override
    public void execute() {
        final Transaction transaction = service.selectTransactionById(id);

        if (null == transaction) {
            this.info(Language.ERROR_DATA.get());
            return;
        }
        if (!AUCTION.equals(transaction.getType())) {
            this.info(Language.ERROR_TYPE.get());
            return;
        }

        if (transaction.getOwner().equals(uid.toString())) {
            this.info(Language.ERROR_TRANSACTION.get());
            if (!Config.DEBUG.get().booleanValue()) {
                return;
            }
        }

        // 拍卖加价
        final double cost = transaction.getCost() + num;
        boolean hasMoney = false;
        String symbol = "";
        switch (transaction.getCurrency()) {
            case "coin":
                hasMoney = economy.getEconomy().has(offlinePlayer, cost);
                symbol = Language.SYMBOL_COIN.get();
                break;
            case "point":
                hasMoney = economy.getPlayerPoints().look(uid) >= cost;
                symbol = Language.SYMBOL_POINT.get();
                break;
            case "item":
            default:
                break;
        }

        if (!hasMoney) {
            this.info(Language.TRANSACTION_FAIL.get().replace("%t%", ""));
            return;
        }

        service.updateTransactionCost(cost, id);
        service.updateTransactionTarget(uid.toString(), id);

        // 添加投标者
        final Addend ad;
        if (StringUtils.isEmpty(transaction.getDesc())) {
            ad = new Addend(AUCTION);
        } else {
            ad = TextUtils.jsonToObj(transaction.getDesc(), Addend.class);
        }
        // 等待次数恢复为3次
        ad.setNum(3);
        // 通知所有参与者
        final Set<String> biders = ad.getStrings();
        biders.add(this.getPlayerName());
        final ItemStack item = YamlUtils.deserializeItemStack(transaction.getStack());
        final String info = Language.BID_REMIND.get()
            .replace("%p%", this.getPlayerName())
            .replace("%m%", cost + symbol);
        final BaseComponent[] textItemStack = 
            ChatUtils.textItemStack(item, String.format("/market affair %s", id));
        for (String bider : biders) {
            final Player pBider = Bukkit.getPlayerExact(bider);
            if (pBider == null) {
                continue;
            }
            pBider.spigot().sendMessage(new ComponentBuilder(info).append(textItemStack).create());
        }

        service.updateTransactionDesc(gson.toJson(ad), id);

        this.info(Language.BID_SUCCEED.get());

        guide.refreshPages(GlobalMarket.AFFAIR_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.EDIT_VIEW, String.valueOf(id));
        guide.refreshPages(GlobalMarket.MAIN_VIEW, "normal", AUCTION, transaction.getCurrency());
        guide.refreshPages(GlobalMarket.MINE_VIEW, transaction.getOwner());
        guide.refreshPages(GlobalMarket.STORE_VIEW);
        guide.refreshPages(GlobalMarket.VISIT_VIEW, transaction.getOwner());
        // 根据分类刷新页面，类型是按二进制存储的
        for (int i = 0; i < 8; i++) {
            if ((transaction.getCategory() & (1 << i)) != 0) {
                guide.refreshPages(GlobalMarket.CATEGORY_VIEW, "category" + i);
            }
        }
    }
}
