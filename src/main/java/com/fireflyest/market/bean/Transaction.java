package com.fireflyest.market.bean;

import io.fireflyest.emberlib.database.annotation.Column;
import io.fireflyest.emberlib.database.annotation.Primary;
import io.fireflyest.emberlib.database.annotation.Table;

/**
 * 交易
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Table("market_transaction")
public class Transaction {
    
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

    // 商品主人
    @Column
    private String owner;
    @Column
    private String ownerName;

    // 购买者
    @Column(defaultValue = "")
    private String target;

    // 原始价格
    @Column
    private double price;

    // 现价
    @Column
    private double cost;

    // 热度
    @Column(defaultValue = "3")
    private int heat;

    // 名称
    @Column
    private String nickname;

    /**
     * 构造方法
     */
    public Transaction() {
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

    public String getOwnerName() {
        return ownerName;
    }

    public String getTarget() {
        return target;
    }

    public double getPrice() {
        return price;
    }

    public double getCost() {
        return cost;
    }

    public int getHeat() {
        return heat;
    }

    public String getNickname() {
        return nickname;
    }

}
