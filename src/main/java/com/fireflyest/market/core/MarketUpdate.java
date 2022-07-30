package com.fireflyest.market.core;

import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Latest;
import com.fireflyest.market.data.Language;
import com.fireflyest.market.util.ChatUtils;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidPluginException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * update the plugin
 *
 * @author Fireflyest
 * @version 1.0.0
 * @since 2022/7/30
 */
public class MarketUpdate {

    private static final BossBar progerssBar = Bukkit.getServer().createBossBar(
            String.format("%s downloading...", Language.PLUGIN_NAME), BarColor.WHITE, BarStyle.SOLID, BarFlag.PLAY_BOSS_MUSIC);
    private static final String PROJECT_LATEST_URL = "https://api.github.com/repos/Fireflyest/GlobalMarket/releases/latest";
    private static Latest.AssetsDTO assetsDTO;
    private static double allSize = 1;
    private static long downloadSize = 1;

    private MarketUpdate(){
    }

    /**
     * 查看最后一个版本的信息
     * @param sender 指令发送者
     * @throws IOException io错误
     */
    public static void checkLatest(CommandSender sender) throws IOException {
        sender.sendMessage(Language.PLUGIN_NAME);
        sender.sendMessage(Language.TITLE + "checking...");
        String result = doGet();
        if (result != null) {
            Latest latest = new Gson().fromJson(result, Latest.class);
            sender.sendMessage(Language.TITLE + String.format("§3§lLatest§7: §f%s", latest.getName()));
            sender.sendMessage(Language.TITLE + String.format("§3§lAuthor§7: §f%s", latest.getAuthor().getLogin()));
            sender.sendMessage(Language.TITLE + String.format("§3§lInfo§7: §f%s", latest.getBody()));
            sender.sendMessage(Language.TITLE + String.format("§3§lPublish§7: §f%s", latest.getPublishedAt()));
            if (latest.getAssets() != null && latest.getAssets().size() > 0){
                assetsDTO = latest.getAssets().get(0);
                long size = assetsDTO.getSize() / 1024;
                if (sender instanceof Player){
                    Player player = ((Player) sender);
                    ChatUtils.sendCommandButton(player,
                            assetsDTO.getName(),
                            "download",
                            "/marketadmin update",
                            String.format(" §3%s§fKB    Download§7: §3%s", size, assetsDTO.getDownloadCount()));
                }else {
                    sender.sendMessage(String.format("§f[§a§l%s§f] §3%s§fKB    Download§7: §3%s", assetsDTO.getName(), size, assetsDTO.getDownloadCount()));
                }
            }
            sender.sendMessage("§7§m·                                                           ·");
        }
    }

    private static String doGet() throws IOException {
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

    public static void update(CommandSender sender) throws IOException, URISyntaxException, InvalidPluginException {
        if (assetsDTO == null) checkLatest(sender);
        if (assetsDTO != null) {
            sender.sendMessage(Language.TITLE + String.format("Downloading %s", assetsDTO.getName()));
            // 文件位置
            File file = new File(GlobalMarket.getPlugin().getDataFolder().getParent(), assetsDTO.getName());
            // 进度条
            allSize = assetsDTO.getSize();
            if (sender instanceof Player) showBar(((Player) sender));
            // 下载
            boolean succeed = download(assetsDTO.getBrowserDownloadUrl(), file);
            // 显示是否成功
            sender.sendMessage(Language.TITLE + String.format("Download %s!", succeed ? "succeed" : "fail"));
        }
    }

    private static boolean download(String urlStr, File file) throws IOException {
        // 已存在，删掉
        if (file.exists() && !file.delete()) return false;
        // 建文件
        if (!file.createNewFile()) return false;
        // 下载
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(15000); // 设置连接主机服务器的超时时间：15000毫秒
        connection.connect(); // 发送请求
        if (connection.getResponseCode() != 200) {
            connection.disconnect();
            return false;
        }
        // 读取
        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream fileOutputStream = new FileOutputStream(file)){

            byte[] bytes = new byte[2048];
            int len;
            while ((len = inputStream.read(bytes))>0){
                fileOutputStream.write(bytes,0, len);
                downloadSize += len;
                updateBar();
            }
            fileOutputStream.flush();
        }
        connection.disconnect();
        return true;
    }

    private static void updateBar(){
        double progress = downloadSize/ allSize;
        if (progress > 1) progress = 1;
        progerssBar.setProgress(progress);
    }

    private static void showBar(Player player){
        progerssBar.setVisible(true);
        progerssBar.addPlayer(player);
    }

    public static void hideBar(){
        progerssBar.removeAll();
        progerssBar.setVisible(false);
    }

}
