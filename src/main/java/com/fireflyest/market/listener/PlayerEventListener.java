package com.fireflyest.market.listener;

import com.fireflyest.market.util.ChatUtils;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.button.ButtonAction;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.service.MarketService;
import com.fireflyest.market.task.TaskAffirm;
import com.fireflyest.market.task.TaskCreate;
import com.fireflyest.market.task.TaskSend;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.event.ViewPlaceEvent;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.util.NetworkUtils;
import org.fireflyest.util.SerializationUtil;
import org.fireflyest.util.TimeUtils;

public class PlayerEventListener implements Listener {

    private final ViewGuide guide;
    private final MarketService service;
    private final TaskHandler handler;

    public PlayerEventListener(MarketService service, ViewGuide guide, TaskHandler handler){
        this.guide = guide;
        this.service = service;
        this.handler = handler;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        // 插入新玩家数据
        String merchantUid = service.selectMerchantUid(playerName);
        if ("".equals(merchantUid)) {
            service.insertMerchant(playerName, player.getUniqueId(), TimeUtils.getTime());
        }

        // 邮箱提醒
        int mailAmount = service.selectDeliveryIdByOwner(player.getUniqueId()).length;
        if (mailAmount > 0){
            player.sendMessage(Language.REMIND_MAIL);
            ChatUtils.sendCommandButton(player, Language.TEXT_MAIL_BUTTON, Language.TEXT_MAIL_HOVER, "/market mail");

            if (!Config.MAXIMUM_MAIL) return;
            if (mailAmount > Config.MAXIMUM_MAIL_NUM){
                player.sendMessage(Language.MAXIMUM_MAIL);
            }
        }

        // 监测更新
        if (Config.CHECK_UPDATE && player.isOp()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    String version = GlobalMarket.getPlugin().getDescription().getVersion().replace("-beta", "");
                    String url = NetworkUtils.checkUpdate("GlobalMarket", version);
                    if (url != null) {
                        player.sendMessage(Language.CHECK_UPDATE.replace("%url%", url));
                    }
                }
            }.runTaskAsynchronously(GlobalMarket.getPlugin());
        }
    }

    @EventHandler
    public void onViewPlace(ViewPlaceEvent event) {
        int action = event.getAction();
        String view = event.getViewName(), value = event.getValue();

        // 判断是否本插件相关的事件
        if(view == null || !view.startsWith("market")) return;

        // 获取点击到的物品，一般来说是有物品
        ItemStack placeItem = event.getCursorItem();
        ItemStack item = event.getCurrentItem();
        if(item == null) return;

        // 获取点击的玩家
        Player player = (Player)event.getWhoClicked();

        // 根据行为做反应
        if (action == ButtonAction.ACTION_PLUGIN) {
            switch (value) {
                case "create":
                case "createExtra":
                case "createOver":
                    handler.putTasks(GlobalMarket.TASK_MARKET, new TaskCreate(player.getName(), service, guide, "prepare", "coin", 0, placeItem.clone()));
                    event.setHandBack(false);
                    break;
                case "search":
                    guide.openView(player, GlobalMarket.SEARCH_VIEW, placeItem.getType().name());
                    break;
                case "destroy":
                    event.setHandBack(false);
                    break;
                default:
                    if (value.startsWith("send")) {
                        // 是否有权限
                        if (!player.hasPermission("market.send")) {
                            player.sendMessage(Language.NO_PERMISSION.replace("%permission%", "market.send"));
                            event.setHandBack(true);
                            return;
                        }
                        String target = value.split(" ")[1];
                        // 禁止发送给自己
                        if (target.equals(player.getName()) && !Config.DEBUG) {
                            player.sendMessage(Language.ERROR_ARGUMENT);
                            event.setHandBack(true);
                            return;
                        }
                        // 违禁品判断
                        if (Config.CONTRABAND_LORE) {
                            String stack = SerializationUtil.serializeItemStack(placeItem);
                            for (String lore : Config.CONTRABAND_LORE_LIST.split(",")) {
                                if (stack.contains(lore)) {
                                    event.setHandBack(true);
                                    return;
                                }
                            }
                        }
                        String targetUid = service.selectMerchantUid(target);
                        // 邮箱数量限制
                        if (Config.MAXIMUM_MAIL && service.selectDeliveryIdByOwner(UUID.fromString(targetUid)).length > Config.MAXIMUM_MAIL_NUM) {
                            player.sendMessage(Language.MAXIMUM_MAIL);
                            event.setHandBack(true);
                            return;
                        }
                        handler.putTasks(GlobalMarket.TASK_MAIL, new TaskSend(player.getName(), service, targetUid, placeItem.clone()));
                        event.setHandBack(false);
                    } else if (value.startsWith("currency")) {
                        long id = NumberConversions.toLong(value.split(" ")[1]);
                        ItemStack clone = placeItem.clone();
                        clone.setAmount(1);
                        handler.putTasks(GlobalMarket.TASK_MARKET, new TaskAffirm(player.getName(), service, guide, id, SerializationUtil.serializeItemStack(clone)));
                    } else if (value.startsWith("logo")) {
                        String target = value.split(" ")[1];
                        service.updateMerchantLogo(target, placeItem.getType().name());
                    }
                    break;
            }
        }
    }

}
