package com.fireflyest.market.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MarketTab implements TabCompleter {

    private final List<String> user = new ArrayList<>();
    private final List<String> op = new ArrayList<>();

    private final String[] classify = new String[]{
            "edible", "item", "block", "burnable", "interactable", "equip", "knowledge"
    };

    public MarketTab(){
        op.add("reload");
        op.add("default");

        op.add("admin");
        op.add("statistic");

        user.add("help");
        user.add("mine");
        user.add("mail");

        user.add("data");
        user.add("buy");
        user.add("add");
        user.add("affair");
        user.add("edit");
        user.add("sign");
        user.add("finish");
        user.add("other");
        user.add("classify");
        user.add("quick");

        user.add("sell");
        user.add("point");
        user.add("retail");
        user.add("auction");
        user.add("discount");
        user.add("send");
        user.add("reprice");
        user.add("desc");
        user.add("search");

        op.addAll(user);
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args){
        if(command.getName().equalsIgnoreCase("market") || command.getName().equalsIgnoreCase("gm")){
            List<String> tab = new ArrayList<>();
            if(args.length == 1){
                for(String sub : sender.isOp()?op:user){
                    if(sub.contains(args[0]))tab.add(sub);
                }
            }else if(args.length == 2){
                if("sell".equalsIgnoreCase(args[0]) || "auction".equalsIgnoreCase(args[0])){
                    tab.add("[price]");
                }else if("data".equalsIgnoreCase(args[0])){
                    tab.add("<id>");
                }else if("admin".equalsIgnoreCase(args[0])
                        || "buy".equalsIgnoreCase(args[0])
                        || "add".equalsIgnoreCase(args[0])
                        || "affair".equalsIgnoreCase(args[0])
                        || "finish".equalsIgnoreCase(args[0])
                        || "edit".equalsIgnoreCase(args[0])
                        || "discount".equalsIgnoreCase(args[0])
                        || "desc".equalsIgnoreCase(args[0])){
                    tab.add("[id]");
                }else if("sign".equalsIgnoreCase(args[0])){
                    tab.add("<id>");
                }else if("search".equalsIgnoreCase(args[0])){
                    tab.add("[something]");
                }else if("classify".equalsIgnoreCase(args[0])){
                    for(String type : classify){
                        if(type.contains(args[1]))tab.add(type);
                    }
                }else if("send".equalsIgnoreCase(args[0]) || "statistic".equalsIgnoreCase(args[0]) || "other".equalsIgnoreCase(args[0])){
                    return null;
                }
            }else if(args.length == 3){
                if("sell".equalsIgnoreCase(args[0]) || "auction".equalsIgnoreCase(args[0]) || "send".equalsIgnoreCase(args[0])){
                    tab.add("<number>");
                }else if("discount".equalsIgnoreCase(args[0])){
                    tab.add("1");
                    tab.add("2");
                    tab.add("3");
                    tab.add("4");
                    tab.add("5");
                    tab.add("6");
                    tab.add("7");
                    tab.add("8");
                    tab.add("9");
                }else if("desc".equalsIgnoreCase(args[0])){
                    tab.add("[desc]");
                }
            }
            return tab;
        }
        return null;
    }

}
