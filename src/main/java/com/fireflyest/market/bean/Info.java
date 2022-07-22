package com.fireflyest.market.bean;

import java.util.*;

public class Info {

    private String type;

    private String desc;

    private long last;

    private Set<String> strings;

    public Info(){
    }

    public Info(String type) {
        this.type = type;
        this.desc = "";
        this.last = new Date().getTime();
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

    public Set<String> getStrings() {
        return strings;
    }

    public void setStrings(Set<String> strings) {
        this.strings = strings;
    }
}
