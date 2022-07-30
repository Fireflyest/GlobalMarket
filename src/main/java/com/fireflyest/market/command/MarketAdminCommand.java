package com.fireflyest.market.command;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Latest;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.core.MarketTasks;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.task.TaskCancel;
import com.fireflyest.market.util.ChatUtils;
import com.fireflyest.market.util.ConvertUtils;
import com.fireflyest.market.view.AdminView;
import com.google.gson.Gson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.FileUtil;
import org.fireflyest.craftgui.api.ViewGuide;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MarketAdminCommand  implements CommandExecutor {

    private static final String PROJECT_LATEST_URL = "https://api.github.com/repos/Fireflyest/GlobalMarket/releases/latest";
    private final ViewGuide guide;
    private final MarketTasks.TaskManager taskManager;
    private final GlobalMarket globalMarket;
    private Latest.AssetsDTO assetsDTO;

    public MarketAdminCommand() {
        this.guide = GlobalMarket.getGuide();
        this.taskManager = MarketTasks.getTaskManager();
        this.globalMarket = GlobalMarket.getPlugin();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!label.equalsIgnoreCase("marketadmin")
                && !label.equalsIgnoreCase("mka")) return true;

        switch (args.length) {
            case 1:
                this.executeCommand(sender, args[0]);
                break;
            case 2:
                this.executeCommand(sender, args[0], args[1]);
                break;
//            case 3:
//                this.executeCommand(sender, args[0], args[1], args[2]);
//                break;
            default:
                this.executeCommand(sender);
        }

        return true;
    }

    private void executeCommand(CommandSender sender){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }

        if(!player.hasPermission("market.admin")){
            player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
            return;
        }

        guide.openView(player, GlobalMarket.ADMIN_VIEW, AdminView.NORMAL);
    }

    private void executeCommand(CommandSender sender, String var1){
        Player player = (sender instanceof Player)? (Player)sender : null;

        if(!sender.hasPermission("market.admin")){
            sender.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
            return;
        }

        switch (var1){
            case "reload":
                sender.sendMessage(Language.PLUGIN_NAME + Language.RELOADING);
                globalMarket.setupData();
                globalMarket.setupGuide();
                sender.sendMessage(Language.PLUGIN_NAME + Language.RELOADED);
                break;
            case "test":
                if(player == null) {
                    sender.sendMessage(Language.PLAYER_COMMAND);
                    return;
                }
                for (int i = 0; i < 60; i++) {
                    player.performCommand(String.format( "market sell %d 1", i));
                }
                break;
            case "version":
                sender.sendMessage(Language.PLUGIN_NAME);
                sender.sendMessage(Language.TITLE + String.format("§3§lVersion§7: §fv%s",  Language.VERSION));
                break;
            case "check":
                sender.sendMessage(Language.PLUGIN_NAME);
                sender.sendMessage(Language.TITLE + "checking...");
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        try {
                            checkLatest(sender);
                        } catch (IOException ignored) {
                            sender.sendMessage(Language.TITLE + "§3§lLatest§7: §ferror");
                        }
                    }
                }.runTaskAsynchronously(globalMarket);
                break;
            default:
        }
        
    }

    private void executeCommand(CommandSender sender, String var1, String var2){
        Player player = (sender instanceof Player)? (Player)sender : null;
        if(player == null) {
            sender.sendMessage(Language.PLAYER_COMMAND);
            return;
        }

        if(!player.hasPermission("market.admin")){
            player.sendMessage(Language.NOT_PERMISSION.replace("%permission%", "market.admin"));
            return;
        }

        switch (var1){
            case "cancel":
                int saleId = ConvertUtils.parseInt(var2);
                Sale sale = MarketManager.getSale(saleId);
                taskManager.putTask(new TaskCancel(sale.getOwner(), saleId));
                player.sendMessage(Language.CANCEL_ADMIN);
                break;
            case "black":
                break;
            default:
        }

    }

    private void update(CommandSender sender) throws IOException {
        if (assetsDTO == null) this.checkLatest(sender);
        if (assetsDTO != null) {
            sender.sendMessage(Language.TITLE + String.format("Downloading%s", assetsDTO.getName()));
            FileUtil.copy(new File(assetsDTO.getBrowserDownloadUrl()), globalMarket.getDataFolder().getParentFile());
            sender.sendMessage(Language.TITLE + "Download succeed");
        }
    }

    private void checkLatest(CommandSender sender) throws IOException {
        String result = doGet();
        if (result != null) {
            Latest latest = new Gson().fromJson(result, Latest.class);
            sender.sendMessage(Language.TITLE + String.format("§3§lLatest§7: §f%s", latest.getName()));
            sender.sendMessage(Language.TITLE + String.format("§3§lAuthor§7: §f%s", latest.getAuthor().getLogin()));
            sender.sendMessage(Language.TITLE + String.format("§3§lInfo§f7: §%s", latest.getBody()));
            sender.sendMessage(Language.TITLE + String.format("§3§lPublish§7: §f%s", latest.getPublishedAt()));
            if (latest.getAssets() != null && latest.getAssets().size() > 0){
                this.assetsDTO = latest.getAssets().get(0);
                long size = assetsDTO.getSize() / 1024;
                if (sender instanceof Player){
                    Player player = ((Player) sender);
                    ChatUtils.sendCommandButton(player,
                            assetsDTO.getName(),
                            "download",
                            "marketadmin update",
                            String.format(" §3%s§fKB    Download§7: §3%s", size, assetsDTO.getDownloadCount()));
                }else {
                    sender.sendMessage(String.format("§f[§a§l%s§f] §3%s§fKB    Download§7: §3%s", assetsDTO.getName(), size, assetsDTO.getDownloadCount()));
                }
            }
            sender.sendMessage("§7§m·                                                           ·");
        }
    }

    private String doGet() throws IOException {
        String result;
        URL url = new URL(PROJECT_LATEST_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // 设置连接方式：get
        connection.setConnectTimeout(15000); // 设置连接主机服务器的超时时间：15000毫秒
        connection.setReadTimeout(60000); // 设置读取远程返回的数据时间：60000毫秒
        connection.connect(); // 发送请求
        if (connection.getResponseCode() != 200) {
            connection.disconnect();
            return null;
        }
        // 读取
        try (InputStream inputStream = connection.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))){
            StringBuilder stringBuilder = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
                stringBuilder.append("\r\n");
            }
            result = stringBuilder.toString();
        }
        connection.disconnect();
        return result;
    }

}
