package com.fireflyest.market.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.ID;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("market_delivery")
public class Delivery {

    // 物品id
    @ID
    @Column
    private int id;

    // 物品
    @Column(dataType = "text")
    private String stack;

    // 创建时间
    @Column
    private long appear;

    // 邮件主人
    @Column
    private String owner;

    // 邮件发送者
    @Column
    private String sender;

    // 简介
    @Column(defaultValue = "")
    private String info;

    // 钱
    @Column(defaultValue = "0")
    private double price;

    // 货币类型
    @Column
    private String currency;
    // 货币数据
    @Column(dataType = "text")
    private String extras;
    
    public Delivery() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public long getAppear() {
        return appear;
    }

    public void setAppear(long appear) {
        this.appear = appear;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExtras() {
        return extras;
    }

    public void setExtras(String extras) {
        this.extras = extras;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

      
}
