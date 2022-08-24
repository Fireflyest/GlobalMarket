package com.fireflyest.market.util;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.market.data.Language;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fireflyest.craftgui.util.TranslateUtils;

/**
 *
 * @author Fireflyest
 * OPEN_URL  在用户的浏览器打开指定地址
 * OPEN_FILE  在用户的电脑打开指定文件
 * RUN_COMMAND  让用户运行指令
 * SUGGEST_COMMAND  在用户的输入框设置文字
 * CHANGE_PAGE  改变书本的页码
 *
 * SHOW_TEXT  显示一个文本
 * SHOW_ACHIEVEMENT  显示一个成就及其介绍
 * SHOW_ITEM  显示一个物品的名字和其他信息
 * SHOW_ENTITY  显示一个实体的名字，ID和其他信息
 *
 */
public class ChatUtils {

    private static final TextComponent LEFT = new TextComponent("[");
    private static final TextComponent RIGHT = new TextComponent("]");

    private ChatUtils(){

    }

    /**
     * 发送一个可执行指令的按钮文本
     * @param player 玩家
     * @param display 文本
     * @param hover 指令提示
     * @param command 所执行指令
     */
    public static void sendCommandButton(Player player, String display, String hover, String command) {
        sendCommandButton(player, display, hover, command, "");
    }

    /**
     * 发送一个可执行指令的按钮文本
     * @param player 玩家
     * @param display 文本
     * @param hover 指令提示
     * @param command 所执行指令
     */
    @SuppressWarnings("deprecation")
    public static void sendCommandButton(Player player, String display, String hover, String command, String info) {
        player.spigot().sendMessage(new ComponentBuilder(LEFT)
                .append(display)
                .color(ChatColor.YELLOW)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                .append(RIGHT)
                .reset()
                .color(ChatColor.WHITE)
                .append(info)
                .create()
        );
    }

    @SuppressWarnings("deprecation")
    public static void sendItemButton(ItemStack item, String command, String name) {
        ItemMeta meta = item.getItemMeta();
        String info, display = null;
        String type = item.getType().isItem() ? "item.minecraft." : "block.minecraft.";

        // 提示文本
        info = Language.SELL_ITEM_BROADCAST.replace("%player%", name);

        // 物品展示
        ComponentBuilder componentBuilder = new ComponentBuilder();
        // 展示名
        if (meta == null) return;
        if(!"".equals(meta.getDisplayName())) display = meta.getDisplayName();
        if (display == null){
            componentBuilder.append(new TranslatableComponent(type + item.getType().name().toLowerCase()));
        } else {
            componentBuilder.append(display);
        }
        // 附魔
        item.getEnchantments().forEach((enchantment, integer) ->{
            String key = "enchantment.minecraft."+enchantment.getKey().toString().replace("minecraft:", "");
            componentBuilder.append("\n").color(ChatColor.GRAY).append(new TranslatableComponent(key)).append(" ").append(n2l(integer));
        });
        if(meta.hasLore() && meta.getLore() != null){
            meta.getLore().forEach(s -> componentBuilder.append("\n").append(s));
        }
        // 发送文本
        ComponentBuilder textBuilder = new ComponentBuilder(info).append(LEFT);
        if (display == null){
            textBuilder.append(new TranslatableComponent(type + item.getType().name().toLowerCase()));
        } else {
            textBuilder.append(display);
        }
        textBuilder.color(ChatColor.YELLOW)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create()))
                .append(RIGHT)
                .reset()
                .color(ChatColor.WHITE)
                .append("§7×")
                .append(String.format("§3%s§f", item.getAmount()));
        // 发送
        Bukkit.spigot().broadcast(textBuilder.create());
    }

    private static String n2l(int num){
        String []roman = {"M","CM","D","CD","C","XC","L","XL","X","IX","V","IV","I"};
        int[] nums = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        StringBuilder res = new StringBuilder();
        for(int i=0; i<nums.length&&num>=0; i++){
            while(nums[i]<=num){
                num-=nums[i];
                res.append(roman[i]);
            }
        }
        return res.toString();
    }

}
