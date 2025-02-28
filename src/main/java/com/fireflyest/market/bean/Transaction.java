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

    // 物品翻译名称
    @Column
    private String nickname;

    // 创建时间
    @Column
    private long appear;

    // 商品主人
    @Column
    private String owner;

    // 购买者
    @Column
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

    // 交易简介，收购交易中用来记录收购数量
    @Column
    private String desc;

    // 列表位运算
    @Column(defaultValue = "0")
    private long category;

    // 交易货币
    @Column(defaultValue = "coin")
    private String currency;

    // 交易类型
    @Column(defaultValue = "prepare")
    private String type;

    // 额外信息
    @Column(dataType = "text")
    private String extra;

    /**
     * 构造方法
     */
    public Transaction() {
        // 默认构造方法
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    /**
     * 复制交易
     * 
     * @return Transaction
     */
    public Transaction duplicate() {
        final Transaction transaction = new Transaction();
        transaction.setId(this.id);
        transaction.setStack(this.stack);
        transaction.setAppear(this.appear);
        transaction.setOwner(this.owner);
        transaction.setTarget(this.target);
        transaction.setPrice(this.price);
        transaction.setCost(this.cost);
        transaction.setHeat(this.heat);
        transaction.setDesc(this.desc);
        transaction.setCategory(this.category);
        transaction.setCurrency(this.currency);
        transaction.setType(this.type);
        transaction.setExtra(this.extra);
        return transaction;
    }

}
