package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Circulation;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.util.TimeUtils;
import com.fireflyest.market.view.*;
import com.google.common.base.Joiner;

import java.util.*;

/**
 * @author Fireflyest
 * 2022/2/21 10:51
 */

public class MarketManager {

    private static ViewGuide guide;
    private static Data data;

    private MarketManager(){
    }

    public static void initMarketManager(){
        guide = GlobalMarket.getGuide();
        data = GlobalMarket.getData();
    }
    
    public static Sale getSale(int id){
        return data.queryOne(Sale.class, "id", id);
    }
    
    public static Mail getMail(int id){
        return data.queryOne(Mail.class, "id", id);
    }

    public static User getUser(String name){
        return data.queryOne(User.class, "name", name);
    }

    public static void addUser(User user){
        data.insert(user);
    }

    public static OfflinePlayer getOfflinePlayer(String name){
        User user = data.queryOne(User.class, "name", name);
        if (user == null) {
            return null;
        }
        return Bukkit.getOfflinePlayer(UUID.fromString(user.getUuid()));
    }

    public static Circulation getCirculation(String day){
        Circulation circulation = data.queryOne(Circulation.class, "day", day);
        if (circulation == null || !circulation.getDay().equals(day)){
            circulation = new Circulation(day);
            data.insert(circulation);
        }
        return circulation;
    }

    public static List<Sale> getPlayerSales(String playerName){
        return data.query(Sale.class, "owner", playerName);
    }

    public static List<Sale> getSales(){
        return data.query(Sale.class);
    }

    public static void addSale(Sale sale, Material material){
        List<String> classify = getClassify(material);
        sale.setClassify(Joiner.on(",").join(classify));
        int id = data.insert(sale);
        sale.setId(id);
        refreshMarket(sale, classify);
    }
    
    public static void removeSale(Sale sale){
        data.delete(sale);
        List<String> classify = Arrays.asList(sale.getClassify().split(","));
        refreshMarket(sale, classify);
    }

    public static void updateSale(Sale sale){
        data.update(sale);
        List<String> classify = Arrays.asList(sale.getClassify().split(","));
        refreshMarket(sale, classify);
    }

    public static void refreshMarket(Sale sale, List<String> classify){
        for (String viewer : guide.getViewers()) {
            ViewPage page = guide.getUsingPage(viewer);
            if(page instanceof AffairPage && Objects.equals(page.getTarget(), String.valueOf(sale.getId()))){
                guide.refreshPage(viewer);
            }else if(page instanceof MainPage){
                guide.refreshPage(viewer);
            }else if(page instanceof MinePage && Objects.equals(page.getTarget(), sale.getOwner())){
                guide.refreshPage(viewer);
            }else if(page instanceof ClassifyPage && classify.contains(page.getTarget())){
                guide.refreshPage(viewer);
            }
        }
    }

    public static int getMailAmount(String playerName){
        return data.query(Mail.class, "owner", playerName).size();
    }

    public static void addMail(Mail mail){
        data.insert(mail);
        refreshMail(mail);
    }

    public static void removeMail(Mail mail){
        data.delete(mail);
        // 记录 三天内
        Circulation circulation = MarketManager.getCirculation(TimeUtils.getTimeToday());
        circulation.setAmount(circulation.getAmount() + 1);
        if (mail.isPoint()){
            circulation.setPoint((int) (circulation.getPoint() + mail.getPrice()));
        }else {
            circulation.setCoin(circulation.getCoin() + mail.getPrice());
        }
        if (mail.getPrice() > circulation.getMax()) circulation.setMax(mail.getPrice());
        data.update(circulation);
    }

    public static void refreshMail(Mail mail){
        String playerName = mail.getOwner();
        ViewPage page = guide.getUsingPage(playerName);
        if (page instanceof MailPage) {
            guide.refreshPage(playerName);
        }
    }


    
    public static List<String> getClassify(Material material){
        final Material exp = XMaterial.EXPERIENCE_BOTTLE.parseMaterial();
        List<String> classifyList = new ArrayList<>();
        if (material.isEdible()){
            classifyList.add("edible");
        }
        if(material.isItem() && !material.isBlock()){
            classifyList.add("item");
        }
        if(material.isBlock()){
            classifyList.add("block");
        }
        if(material.isBurnable() || material.isFlammable() || material.isFuel()){
            classifyList.add("burnable");
        }
        if (material.getMaxDurability() > 0){
            classifyList.add("equip");
        }
        if(material.name().contains("BOOK") || material.name().contains("POTION") || material.equals(exp)){
            classifyList.add("knowledge");
        }
        return classifyList;
    }

}
