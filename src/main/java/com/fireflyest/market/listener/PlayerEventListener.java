package com.fireflyest.market.listener;


import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import io.fireflyest.emberlib.inventory.ViewGuide;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskAffirm;
import com.fireflyest.market.task.TaskCreate;
import com.fireflyest.market.task.TaskSend;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import io.fireflyest.emberlib.task.TaskHandler;
import io.fireflyest.emberlib.util.ChatUtils;
import io.fireflyest.emberlib.util.TimeUtils;
import io.fireflyest.emberlib.util.UpdateUtils;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * 玩家事件监听
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class PlayerEventListener implements Listener {

    private final ViewGuide guide;
    private final MarketService service;
    private final TaskHandler handler;

    /**
     * 构造玩家事件监听
     * 
     * @param service 市场服务
     * @param guide 界面导航
     * @param handler 任务处理器
     */
    public PlayerEventListener(MarketService service, ViewGuide guide, TaskHandler handler){
        this.guide = guide;
        this.service = service;
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String playerName = player.getName();

        // 插入新玩家数据
        final String merchantUid = service.selectMerchantUid(playerName);
        if (StringUtils.isEmpty(merchantUid)) {
            service.insertMerchant(playerName, player.getUniqueId(), TimeUtils.getTime());
        }

        // 邮箱提醒
        final int mailAmount = service.selectDeliveryIdByOwner(player.getUniqueId()).length;
        if (mailAmount > 0) {
            final ComponentBuilder builder = new ComponentBuilder(Language.MAIL_REMIND.get());
            builder.append("[")
                .append(ChatUtils.text(Language.GUI_MAIL_BUTTON.get(), "/market mail", "点击打开"))
                .append("]");
            player.spigot().sendMessage(builder.create());

            if (!Config.MAIL_LIMIT.get().booleanValue()) {
                return;
            }
            if (mailAmount > Config.MAIL_MAXIMUM.get()) {
                player.sendMessage(Language.MAIL_MAXIMUM.get());
            }
        }

        // 监测更新
        if (Config.UPDATE_CHECK.get().booleanValue() && player.isOp()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    final String version = GlobalMarket.getPlugin()
                        .getDescription()
                        .getVersion().replace("-beta", "");
                    final String url = UpdateUtils.checkUpdate("GlobalMarket", version);
                    if (url != null) {
                        player.sendMessage(Language.VERSION_NEW.get().replace("%url%", url));
                    }
                }
            }.runTaskAsynchronously(GlobalMarket.getPlugin());
        }
    }

    // @EventHandler
    // public void onViewPlace(ViewPlaceEvent event) {
    //     int action = event.getAction();
    //     String view = event.getViewName(), value = event.getValue();

    //     // 判断是否本插件相关的事件
    //     if(view == null || !view.startsWith("market")) return;

    //     // 获取点击到的物品，一般来说是有物品
    //     ItemStack placeItem = event.getCursorItem();
    //     ItemStack item = event.getCurrentItem();
    //     if(item == null) return;

    //     // 获取点击的玩家
    //     Player player = (Player)event.getWhoClicked();

    //     // 根据行为做反应
    //     if (action == ButtonAction.ACTION_PLUGIN) {
    //         switch (value) {
    //             case "create":
    //             case "createExtra":
    //             case "createOver":
    //                 handler.putTasks(GlobalMarket.TASK_MARKET, new TaskCreate(player.getName(), service, guide, "prepare", "coin", 0, placeItem.clone()));
    //                 event.setHandBack(false);
    //                 break;
    //             case "search":
    //                 guide.openView(player, GlobalMarket.SEARCH_VIEW, placeItem.getType().name());
    //                 break;
    //             case "destroy":
    //                 event.setHandBack(false);
    //                 break;
    //             default:
    //                 if (value.startsWith("send")) {
    //                     // 是否有权限
    //                     if (!player.hasPermission("market.send")) {
    //                         player.sendMessage(Language.COMMAND_PERMISSION.replace("%permission%", "market.send"));
    //                         event.setHandBack(true);
    //                         return;
    //                     }
    //                     String target = value.split(" ")[1];
    //                     // 禁止发送给自己
    //                     if (target.equals(player.getName()) && !Config.DEBUG) {
    //                         player.sendMessage(Language.COMMAND_ARGUMENT);
    //                         event.setHandBack(true);
    //                         return;
    //                     }
    //                     // 违禁品判断
    //                     if (Config.CONTRABAND_ENABLE) {
    //                         String stack = SerializationUtil.serializeItemStack(placeItem);
    //                         for (String lore : Config.CONTRABAND_LORE.split(",")) {
    //                             if (stack.contains(lore)) {
    //                                 event.setHandBack(true);
    //                                 return;
    //                             }
    //                         }
    //                     }
    //                     String targetUid = service.selectMerchantUid(target);
    //                     // 邮箱数量限制
    //                     if (Config.MAIL_LIMIT && service.selectDeliveryIdByOwner(UUID.fromString(targetUid)).length > Config.MAIL_MAXIMUM) {
    //                         player.sendMessage(Language.MAIL_MAXIMUM);
    //                         event.setHandBack(true);
    //                         return;
    //                     }
    //                     handler.putTasks(GlobalMarket.TASK_MAIL, new TaskSend(player.getName(), service, targetUid, placeItem.clone()));
    //                     event.setHandBack(false);
    //                 } else if (value.startsWith("currency")) {
    //                     long id = NumberConversions.toLong(value.split(" ")[1]);
    //                     ItemStack clone = placeItem.clone();
    //                     clone.setAmount(1);
    //                     handler.putTasks(GlobalMarket.TASK_MARKET, new TaskAffirm(player.getName(), service, guide, id, SerializationUtil.serializeItemStack(clone)));
    //                 } else if (value.startsWith("logo")) {
    //                     String target = value.split(" ")[1];
    //                     service.updateMerchantLogo(target, placeItem.getType().name());
    //                 }
    //                 break;
    //         }
    //     }
    // }

}
