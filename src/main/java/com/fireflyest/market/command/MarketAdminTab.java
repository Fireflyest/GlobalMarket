package com.fireflyest.market.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MarketAdminTab implements TabCompleter {

    private final List<String> op = new ArrayList<>();

    public MarketAdminTab(){
        op.add("cancel");
        op.add("black");
        op.add("info");
        op.add("version");
        op.add("update");
        op.add("reload");
        op.add("test");
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args){
        if(command.getName().equalsIgnoreCase("marketadmin") || command.getName().equalsIgnoreCase("mka")){
            List<String> tab = new ArrayList<>();
            if(args.length == 1){
                for(String sub : op){
                    if(sub.startsWith(args[0]))tab.add(sub);
                }
            }else if(args.length == 2){
                if("cancel".equalsIgnoreCase(args[0])){
                    tab.add("<id>");
                }else if ("black".equalsIgnoreCase(args[0])){
                    return null;
                }
            }else if(args.length == 3){
               return tab;
            }
            return tab;
        }
        return null;
    }

}
