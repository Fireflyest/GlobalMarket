package com.fireflyest.market.listener;

import com.cryptomorin.xseries.XSound;
import com.fireflyest.market.bean.Button;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.task.*;
import org.bukkit.Material;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.event.ViewClickEvent;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.data.Config;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ChatUtils;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.util.TimeUtils;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.event.ViewPlaceEvent;
import org.fireflyest.craftgui.util.ItemUtils;
import org.fireflyest.craftgui.util.TranslateUtils;

public class PlayerEventListener implements Listener {

    private final Sound clickSound;
    private final Sound cancelSound;
    private final Sound pageSound;

    private final ViewGuide guide;
    private final Data data;

    private final MarketTasks.TaskManager taskManager;

    public PlayerEventListener(){

        this.clickSound = XSound.BLOCK_STONE_BUTTON_CLICK_OFF.parseSound();
        this.cancelSound = XSound.BLOCK_ANVIL_PLACE.parseSound();
        this.pageSound = XSound.ITEM_BOOK_PAGE_TURN.parseSound();

        this.guide = GlobalMarket.getGuide();
        this.data = GlobalMarket.getData();

        this.taskManager = MarketTasks.getTaskManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        User user = MarketManager.getUser(playerName);
        if (user == null) {
            user = new User(playerName, player.getUniqueId().toString(), 100, 0.0, 0, false, TimeUtils.getDate(), 0);
            MarketManager.addUser(user);
        }

        int mailAmount = MarketManager.getMailAmount(playerName);
        if (mailAmount > 0){
            player.sendMessage(Language.HAS_MAIL);
            ChatUtils.sendCommandButton(player, Language.MAIL_BUTTON, Language.MAIL_HOVER, "/market mail");
            if (!Config.LIMIT_MAIL) return;
            if (mailAmount > Config.LIMIT_MAIL_NUM - 5){
                player.sendMessage(Language.LIMIT_MAIL);
            }
            if(mailAmount > Config.LIMIT_MAIL_NUM){
                taskManager.putTask(new TaskSignAll(playerName));
            }
        }
    }

    @EventHandler
    public void onViewClick(ViewClickEvent event){
        if(!event.getView().getTitle().contains(Language.PLUGIN_NAME)) return;

        ItemStack item = event.getCurrentItem();
        if(item == null) return;

        Player player = (Player)event.getWhoClicked();

        String value = ItemUtils.getItemValue(item);
        if("".equals(value))return;

        // 翻页
        if (value.contains("page")){
            // 不用刷新
            event.setRefresh(false);

            if (Config.DEBUG) GlobalMarket.getPlugin().getLogger().info("action -> " + value);
            player.playSound(player.getLocation(), pageSound, 1F, 1F);
            if (value.contains("pre")){
                guide.prePage(player);
            }else if (value.contains("next")){
                guide.nextPage(player);
            }
            return;
        }

        if (value.startsWith("send")){
            if (clickSound != null) player.playSound(player.getLocation(), clickSound, 1F, 1F);
            player.performCommand("market send");
            return;
        }
        if (value.startsWith("button")){
            if (clickSound != null) player.playSound(player.getLocation(), clickSound, 1F, 1F);
            return;
        }

        // 其他按钮点击，执行指令
        if (event.isShiftClick()){ // 下架
            if (value.contains("affair") || value.contains("edit")){
                event.setRefresh(false);

                if (player.hasPermission("market.admin") && value.contains("affair")){
                    player.performCommand("marketadmin cancel " + value.split(" ")[1]);
                }else {
                    taskManager.putTask(new TaskCancel(player.getName(), ConvertUtils.parseInt(value.split(" ")[1])));
                }
                player.playSound(player.getLocation(), cancelSound, 1F, 1F);
            }
        } else { // 执行指令
            event.setRefresh(false);

            if (Config.DEBUG) GlobalMarket.getPlugin().getLogger().info("command -> " + "market "+ value);
            player.performCommand("market "+ value);
            if (clickSound != null) player.playSound(player.getLocation(), clickSound, 1F, 1F);
        }
    }

    @EventHandler
    public void onViewPlace(ViewPlaceEvent event) {
        if(!event.getView().getTitle().contains(Language.PLUGIN_NAME)) return;

        ItemStack placeItem = event.getCursorItem();
        ItemStack clickItem = event.getCurrentItem();
        if(placeItem == null || clickItem == null || clickItem.getType() == Material.AIR) return;

        Player player = (Player)event.getWhoClicked();

        String value = ItemUtils.getItemValue(clickItem);
        if("".equals(value))return;

        if ("sell".equals(value)){
            if(!player.hasPermission("market.sell")){ // 快捷上架
                player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.sell"));
                return;
            }
            taskManager.putTask(new TaskSell(player.getName(), false, false, -1, placeItem.clone()));
            event.setHandBack(false);
        }else if ("search".equals(value)){ // 搜索同材料物品
            if(!player.hasPermission("market.search")){
                player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.search"));
                return;
            }
            String displayName = TranslateUtils.translate(placeItem.getType());
            player.performCommand(String.format("market search %s", displayName));
        }else if ("classify".equals(value)){ // 分查看物品类型
            String classify = MarketManager.getClassify(placeItem.getType()).toString();
            player.sendMessage(Language.CLASSIFY_ITEM.replace("%classify%", classify));
        }else if (value.contains("send")){ // 发送邮件
            if(!player.hasPermission("market.send")){
                player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.send"));
                return;
            }
            String target = value.split(" ")[1];
            taskManager.putTask(new TaskSend(player.getName(), target, placeItem.clone(), 0, false));
            event.setHandBack(false);
        }else if ("delete".equals(value)){ // 邮箱界面的垃圾桶
            event.setHandBack(false);
        }else if (value.contains("button")){ // 自定义按钮
            String target = value.split(" ")[1];
            String material = event.getCursorItem().getType().name();
            Button button = data.queryOne(Button.class, "target", target);
            if (button == null) {
                button = new Button(target, event.getCursorItem().getType().name(), "");
                data.insert(button);
            }else {
                button.setMaterial(material);
                data.update(button);
            }
            player.sendMessage(Language.BUTTON_ITEM);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();

        // 右键牌子
        if(event.hasBlock()){
            Block block = event.getClickedBlock();
            if(block == null)return;
            if(!block.getType().name().contains("SIGN"))return;
            Sign sign = (Sign)block.getState();

            if(sign.getLine(0).contains("GlobeMarket")){

                if("mail".equals(sign.getLine(1))){
                    event.setCancelled(true);
                    player.performCommand("market mail");
                }else if (player.isSneaking()){
                    event.setCancelled(true);
                    player.performCommand("market quick");
                }else {
                    event.setCancelled(true);
                    player.performCommand("market");
                }
            }
        }

    }

    @EventHandler
    public void onSignChange(SignChangeEvent event){
        if(!event.getPlayer().hasPermission("market.create"))return;
        if("market".equalsIgnoreCase(event.getLine(0))){
            event.setLine(0, Language.PLUGIN_NAME);
        }
    }

}
