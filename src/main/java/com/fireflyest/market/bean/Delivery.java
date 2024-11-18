package com.fireflyest.market.bean;

import io.fireflyest.emberlib.database.annotation.Column;
import io.fireflyest.emberlib.database.annotation.Primary;
import io.fireflyest.emberlib.database.annotation.Table;

/**
 * 邮件
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Table("market_delivery")
public class Delivery {

    // 物品id
    @Primary(autoIncrement = true)
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
    
    /**
     * 构造方法
     */
    public Delivery() {
        // 默认构造方法
    }

    public int getId() {
        return id;
    }

    public String getStack() {
        return stack;
    }

    public long getAppear() {
        return appear;
    }

    public String getOwner() {
        return owner;
    }

    public String getSender() {
        return sender;
    }

    public String getInfo() {
        return info;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getExtras() {
        return extras;
    }

}
