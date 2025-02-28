package com.fireflyest.market.command;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.command.args.Argument;

/**
 * 玩家归属的邮件id
 * 
 * @author Fireflyest
 * @since 4.0
 */
public class DeliveryArgs implements Argument {

    private final MarketService service;

    public DeliveryArgs(MarketService service) {
        this.service = service;
    }

    @Override
    public List<String> tab(CommandSender sender, String arg) {
        final List<String> argList = new ArrayList<>(20);
        final Player player = (sender instanceof Player) ? (Player) sender : null;
        if (player == null) {
            sender.sendMessage(Language.COMMAND_PLAYER.get());
            return argList;
        }
        final long[] ids = service.selectDeliveryIdByOwner(player.getUniqueId());
        for (long id : ids) {
            if (String.valueOf(id).startsWith(arg)) {
                argList.add(String.valueOf(id));
            }
            if (argList.size() > 19) {
                break;
            }
        }
        return argList;
    }
    
}
