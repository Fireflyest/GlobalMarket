package com.fireflyest.market.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.ID;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("market_transaction")
public class Transaction {
    
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

    // 简介
    @Column(defaultValue = "")
    private String desc;

    // 分类
    @Column
    private long category;

    // 交易类型  prepare retail auction trade order adminretail adminorder
    @Column(defaultValue = "prepare")
    private String type;

    // 货币类型 coin point item
    @Column(defaultValue = "coin")
    private String currency;
    // 货币数据
    @Column(dataType = "text", defaultValue = "")
    private String extras;
    
    public Transaction() {
    }

    public Transaction duplicate() {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setStack(stack);
        transaction.setAppear(appear);
        transaction.setCategory(category);
        transaction.setCost(cost);
        transaction.setCurrency(currency);
        transaction.setDesc(desc);
        transaction.setExtras(extras);
        transaction.setHeat(heat);
        transaction.setNickname(nickname);
        transaction.setOwner(owner);
        transaction.setOwnerName(ownerName);
        transaction.setPrice(price);
        transaction.setTarget(target);
        transaction.setType(type);
        return transaction;
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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    


}
