package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/3/26 12:08
 */

public class Mail {

    // 邮件主人
    private String owner;

    // 简介
    private String info;

    // 是否签收
    private boolean signed;

    // 是否交易记录
    private boolean record;

    // 钱
    private double price;

    // 是否点券
    private boolean point;

    public Mail() {
    }

    public Mail(int id, String stack, String meta, String nbt, long create, String owner, String info, boolean signed, boolean record, double price, boolean point) {
        // super(id, stack, meta, nbt, create);
        this.owner = owner;
        this.info = info;
        this.signed = signed;
        this.record = record;
        this.price = price;
        this.point = point;
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

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isPoint() {
        return point;
    }

    public void setPoint(boolean point) {
        this.point = point;
    }
}
