package com.fireflyest.market.bean;

import org.fireflyest.craftdatabase.annotation.Column;
import org.fireflyest.craftdatabase.annotation.Primary;
import org.fireflyest.craftdatabase.annotation.Table;

@Table("market_merchant")
public class Merchant {

    //唯一id
    @Primary
    @Column
    private String uid;

    //玩家名
    @Column
    private String name;

    //信誉度
    @Column(defaultValue = "100")
    private int credit;

    //交易量
    @Column(defaultValue = "0")
    private int amount;

    //黑名单
    @Column(defaultValue = "0")
    private boolean black;

    //注册时间
    @Column
    private long register;

    //目前出售数量
    @Column(defaultValue = "0")
    private int selling;

    // 额外上架数量
    @Column(defaultValue = "0")
    private int size;

    // 店铺图标
    @Column(defaultValue = "CHEST")
    private String logo;

    // 店铺名称
    @Column(defaultValue = "")
    private String store;

    // 点赞数量
    @Column(defaultValue = "0")
    private int star;

    // 点赞数量
    @Column(defaultValue = "0")
    private int visit;

    public Merchant() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isBlack() {
        return black;
    }

    public void setBlack(boolean black) {
        this.black = black;
    }

    public long getRegister() {
        return register;
    }

    public void setRegister(long register) {
        this.register = register;
    }

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getVisit() {
        return visit;
    }

    public void setVisit(int visit) {
        this.visit = visit;
    }

}
