package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/5/5 12:51
 */

public class Circulation {

    private String day;

    private double coin;
    private int point;

    private int amount;

    private double max;

    public Circulation() {
    }

    public Circulation(String day) {
        this.day = day;
        this.coin = 0;
        this.point = 0;
        this.amount = 0;
        this.max = 0;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getCoin() {
        return coin;
    }

    public void setCoin(double coin) {
        this.coin = coin;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
