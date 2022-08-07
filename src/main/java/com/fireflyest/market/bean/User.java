package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/3/26 12:09
 */

public class User {

    //玩家名
    private String name;

    //唯一id
    private String uuid;

    //信誉度
    private int credit;

    //总交易金额（准备弃用，无法记录是否点券）
    private double money;

    //交易量
    private int amount;

    //黑名单
    private boolean black;

    //注册时间
    private long register;

    //目前出售数量
    private int selling;

    public User() {
    }

    public User(String name, String uuid, int credit, double money, int amount, boolean black, long register, int selling) {
        this.name = name;
        this.uuid = uuid;
        this.credit = credit;
        this.money = money;
        this.amount = amount;
        this.black = black;
        this.register = register;
        this.selling = selling;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", credit=" + credit +
                ", money=" + money +
                ", amount=" + amount +
                ", black=" + black +
                ", register=" + register +
                ", selling=" + selling +
                '}';
    }
}
