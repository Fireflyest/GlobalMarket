package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/5/5 12:51
 */

public class Note {

    private String day;

    private double money;

    private int amount;

    private double max;

    public Note() {
    }

    public Note(String day, double money, int amount, double max) {
        this.day = day;
        this.money = money;
        this.amount = amount;
        this.max = max;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
}
