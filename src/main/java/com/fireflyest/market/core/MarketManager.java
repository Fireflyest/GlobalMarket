package com.fireflyest.market.core;

import com.cryptomorin.xseries.XMaterial;
import com.fireflyest.gui.api.ViewGuide;
import com.fireflyest.gui.api.ViewPage;
import com.fireflyest.market.GlobalMarket;
import com.fireflyest.market.bean.Mail;
import com.fireflyest.market.bean.Note;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.data.Data;
import com.fireflyest.market.util.TimeUtils;
import com.fireflyest.market.view.*;
import com.google.common.base.Joiner;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static Note getTodayNote(){
        String day = TimeUtils.getTimeToday();
        Note todayNote = data.queryOne(Note.class, "day", day);
        if (todayNote == null || !todayNote.getDay().equals(day)){
            todayNote = new Note(day, 0, 0, 0);
            data.insert(todayNote);
        }
        return todayNote;
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
            if(page instanceof AffairPage && page.getTarget().equals(String.valueOf(sale.getId()))){
                guide.refreshPage(viewer);
            }else if(page instanceof MainPage){
                guide.refreshPage(viewer);
            }else if(page instanceof MinePage && page.getTarget().equals(sale.getOwner())){
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
        refreshMail(mail);
        // 记录
        Note note = MarketManager.getTodayNote();
        note.setAmount(note.getAmount() + 1);
        note.setMoney(note.getMoney() + mail.getPrice());
        if (mail.getPrice() > note.getMax()) note.setMax(mail.getPrice());
        data.update(note);
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
        if(material.isInteractable()){
            classifyList.add("interactable");
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
