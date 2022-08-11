package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ConvertUtils;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.SerializeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class TaskSign extends Task{

    private final int id;
    private final boolean reflash;

    private final Economy economy;
    private final PlayerPointsAPI pointsAPI;

    public TaskSign(@NotNull String playerName, int id) {
        this(playerName, id, true);
    }

    public TaskSign(@NotNull String playerName, int id, boolean reflash) {
        super(playerName);
        this.id = id;
        this.reflash = reflash;

        this.economy = GlobalMarket.getEconomy();
        this.pointsAPI = GlobalMarket.getPointsAPI();

        this.type = MarketTasks.MAIL_TASK;
    }

    @Override
    public @NotNull List<Task> execute() {
        // 玩家不在线
        if (player == null) {
            return then;
        }

        Mail mail = MarketManager.getMail(id);

        if (mail == null) {
            this.executeInfo(Language.DATA_NULL);
            return then;
        }

        // 判断背包是否满
        if(player.getInventory().firstEmpty() == -1){
            this.executeInfo(Language.SIGN_ERROR);
            return then;
        }

        mail.setSigned(true);
        ItemStack item = SerializeUtil.deserialize(mail.getStack(), mail.getMeta());

        if (mail.isRecord()){
            if (mail.isPoint()){
                User user = MarketManager.getUser(playerName);
                int get = (int)Math.floor(mail.getPrice());
                pointsAPI.give(UUID.fromString(user.getUuid()),  get);
                player.sendMessage(Language.AFFAIR_FINISH.replace("%money%", String.valueOf(get)) + Language.POINT_SYMBOL);
            }else {
                economy.depositPlayer(player, mail.getPrice());
                player.sendMessage(Language.AFFAIR_FINISH.replace("%money%", ConvertUtils.formatDouble(mail.getPrice())) + Language.COIN_SYMBOL);
            }
            MarketManager.removeMail(mail);
            if (reflash) guide.refreshPage(playerName);

            if (Config.MARKET_RECORD) player.getInventory().addItem(item);
        }else {
            MarketManager.removeMail(mail);
            if (reflash) guide.refreshPage(playerName);

            player.sendMessage(Language.SIGN_FINISH);
            player.getInventory().addItem(item);
        }

        return then;
    }
}
