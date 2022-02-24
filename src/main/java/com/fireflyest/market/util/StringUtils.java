package com.fireflyest.market.util;

/**
 * @author Fireflyest
 * 2022/1/3 13:13
 */

public class StringUtils {

    private StringUtils(){}

    public static String toLowerCase(String str){
        StringBuilder sb = new StringBuilder();
        for(String word: str.split("_")){
            sb.append(word.charAt(0))
            .append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public static String toFirstUpCase(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

}
