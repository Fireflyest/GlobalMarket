package com.fireflyest.market.command;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import io.fireflyest.emberlib.command.args.Argument;

/**
 * 交易id列表
 * 
 * @author Fireflyest
 * @since 4.0
 */
public class TransactionArgs implements Argument {

    private final MarketService service;
    private final String type;

    public TransactionArgs(MarketService service, @Nullable String type) {
        this.service = service;
        this.type = type;
    }

    @Override
    public List<String> tab(CommandSender sender, String arg) {
        final List<String> argList = new ArrayList<>(20);
        final long[] ids;
        if (StringUtils.isEmpty(type)) {
            final Player player = (sender instanceof Player) ? (Player) sender : null;
            if (player == null) {
                sender.sendMessage(Language.COMMAND_PLAYER.get());
                return argList;
            }
            ids = service.selectTransactionIdByOwner(player.getUniqueId());
        } else {
            ids = service.selectTransactionIdByType(type);
        }
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
