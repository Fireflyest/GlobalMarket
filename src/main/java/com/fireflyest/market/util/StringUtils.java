package com.fireflyest.market.util;

import java.util.regex.Pattern;

/**
 * @author Fireflyest
 * 2022/1/3 13:13
 */

public class StringUtils {

    private StringUtils(){}


    /**
     * TEST_TEST转换为TestTest
     * @param str 文本
     * @return 拼接
     */
    public static String toLowerCase(String str){
        StringBuilder sb = new StringBuilder();
        for(String word: str.split("_")){
            sb.append(word.charAt(0))
            .append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * test转换为Test
     * @param str 文本
     * @return 首字母大写
     */
    public static String toFirstUpCase(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

}
