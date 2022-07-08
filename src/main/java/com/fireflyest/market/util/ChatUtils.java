package com.fireflyest.market.util;

import com.fireflyest.market.data.Language;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    @SuppressWarnings("deprecation")
    public static void sendCommandButton(Player player, String display, String hover, String command) {
        player.spigot().sendMessage(new ComponentBuilder(LEFT)
                .append(display)
                .color(ChatColor.YELLOW)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                .append(RIGHT)
                .reset()
                .color(ChatColor.WHITE)
                .create()
        );
    }

    @SuppressWarnings("deprecation")
    public static void sendItemButton(Player player, boolean auction, ItemStack item, String command, String name) {
        if(Bukkit.getVersion().contains("1.12"))return;
        ItemMeta meta = item.getItemMeta();
        String hover, info, display = item.getType().name();
        // 悬浮文本
        StringBuilder hoverBuilder = new StringBuilder();
        // 提示文本
        StringBuilder infoBuilder = new StringBuilder();
        infoBuilder.append(Language.TITLE)
                .append("玩家§3")
                .append(name)
                .append("§f在环球市场");
        if(auction){
            infoBuilder.append("§3拍卖 §f");
        }else {
            infoBuilder.append("§3出售 §f");
        }
        info = infoBuilder.toString();

        if(meta != null){
            if(!"".equals(meta.getDisplayName())){
                display = meta.getDisplayName();
            }else {
                display = TranslateUtils.translate(item.getType());
            }
            hoverBuilder.append(display);
            if(meta.hasLore() && meta.getLore() != null){
                meta.getLore().forEach(s -> hoverBuilder.append(s).append("\n"));
            }
        }
        item.getEnchantments().forEach((enchantment, integer) ->
                hoverBuilder.append("§7")
                .append(enchantment.getKey())
                .append(" ")
                .append(n2l(integer))
                .append("\n"));
        hoverBuilder.append("点击查看");
        hover = hoverBuilder.toString();

        player.spigot().sendMessage(new ComponentBuilder(info)
                .append(LEFT)
                .append(display)
                .color(ChatColor.YELLOW)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                .append(RIGHT)
                .reset()
                .color(ChatColor.WHITE)
                .append("§7×")
                .append(String.format("§3%s§f", item.getAmount()))
                .create()
        );
    }

    private static String n2l(int n){
        switch (n){
            case 1:
            default:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
    }

}
