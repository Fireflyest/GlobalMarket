package com.fireflyest.market.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 转成json在商品的desc中
 */
public class Addend {

    private String type;

    private String desc;

    private long last;

    private int num;
    
    private Set<String> strings;

    public Addend() {
    }

    /**
     * 构造信息
     * 
     * @param type 类型
     */
    public Addend(String type) {
        this.type = type;
        this.desc = "";
        this.last = new Date().getTime();
        this.num = 0;
        this.strings = new HashSet<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Set<String> getStrings() {
        return strings;
    }

    public void setStrings(Set<String> strings) {
        this.strings = strings;
    }
}
