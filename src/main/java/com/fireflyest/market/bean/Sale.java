package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/3/26 12:08
 */

public class Sale extends Item{

    // 商品主人
    private String owner;

    // 购买者
    private String buyer;

    // 原始价格
    private double price;

    // 现价
    private double cost;

    // 热度
    private int heat;

    // 名称
    private String nickname;

    // 简介
    private String desc;

    // 分类
    private String classify;

    // 拍卖
    private boolean auction;

    // 货币
    private boolean point;

    // 无限
    private boolean admin;

    public Sale() {
    }

    public Sale(int id, String stack, String meta, String nbt, long create, String owner, String buyer, double price, double cost, int heat, String nickname, boolean auction, boolean point) {
        super(id, stack, meta, nbt, create);
        this.buyer = buyer;
        this.owner = owner;
        this.price = price;
        this.cost = cost;
        this.heat = heat;
        this.nickname = nickname;
        this.auction = auction;
        this.point = point;
    }

    public boolean isPoint() {
        return point;
    }

    public void setPoint(boolean point) {
        this.point = point;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public boolean isAuction() {
        return auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "owner='" + owner + '\'' +
                ", buyer='" + buyer + '\'' +
                ", price=" + price +
                ", cost=" + cost +
                ", heat=" + heat +
                ", nickname='" + nickname + '\'' +
                ", desc='" + desc + '\'' +
                ", classify='" + classify + '\'' +
                ", auction=" + auction +
                ", point=" + point +
                ", admin=" + admin +
                '}';
    }
}
