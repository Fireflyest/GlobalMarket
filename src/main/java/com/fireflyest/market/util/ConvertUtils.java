package com.fireflyest.market.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * @author Fireflyest
 * ❤✦✧@❀❁❂☻☠☽⚒⚓⚔⚜⚝✈✂✄✘♯¶♩♪♫♬
 */
public class ConvertUtils {

    private final static String BAR = "▎";

    private ConvertUtils(){

    }

    /**
     * 获取一定数量的符号条
     * @param amount 数量
     * @return String
     */
    public static String convertBar(double amount) {
        String bars = "";
        for(int i = (int)amount ; i > 0 ; i--) { bars = bars.concat(BAR); }
        return bars;
    }

    /**
     * 将long类型转化为时间
     * @param time 时间数据
     * @return String
     */
    public static String convertTime(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = time / dd;
        long hour = (time - day * dd)/ hh;
        long minute = (time - day * dd - hour * hh )/ mi;
        long second = (time - day * dd - hour * hh - minute * mi )/ ss;
        return day+","+hour+","+minute+","+second;
    }

    /**
     * 转化颜色符号
     * @param text 文本
     * @return 带颜色符号文本
     */
    public static String parseColor(String text){
        if(null == text)return "";
        if("".equals(text))return text;
        return text.replace("&", "§");
    }

    /**
     * 将java数据类型转化为sql数据类型
     * @param type java数据类型
     * @return sql数据类型
     */
    public static String javaType2SQLType(String type){
        switch (type){
            case "int":
                return "int";
            case "long":
            case "java.lang.Long":
                return "bigint";
            case "boolean":
            case "java.lang.Boolean":
                return "bit";
            case "short":
                return "tinyint";
            case "java.lang.String":
                return "varchar(255)";
            case "java.sql.Date":
                return "datetime";
            case "java.lang.Double":
                return "double";
            case "java.lang.Float":
                return "float";
            case "java.lang.Integer":
                return "integer";
            default:
        }
        return "varchar(31)";
    }

    public static int parseInt(String var){
        int i = 0;
        try {
            i = Integer.parseInt(var);
        } catch (NumberFormatException ignored) {
        }
        return i;
    }

    public static double parseDouble(String var){
        double i = 0;
        try {
            i = Double.parseDouble(var);
        } catch (NumberFormatException ignored) {
        }
        return i;
    }

    public static String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        return nf.format(d);
    }

}
