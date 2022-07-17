package com.fireflyest.market.task;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.SerializeUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ItemUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TaskSign extends Task{

    private final int id;

    private final Economy economy;

    public TaskSign(@NotNull String playerName, int id) {
        super(playerName);
        this.id = id;

        this.economy = GlobalMarket.getEconomy();

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
        if(!"null".equals(mail.getNbt()) && !"".equals(mail.getNbt())) ItemUtils.setItemValue(item, mail.getNbt());

        player.getInventory().addItem(item);
        if (mail.isRecord()){
            economy.depositPlayer(player, mail.getPrice());
            player.sendMessage(String.format(Language.AFFAIR_FINISH, mail.getPrice()));
        }else {
            player.sendMessage(Language.SIGN_FINISH);
        }

        MarketManager.removeMail(mail);

        return then;
    }
}
