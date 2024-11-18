package com.fireflyest.market.bean;

import io.fireflyest.emberlib.database.annotation.Column;
import io.fireflyest.emberlib.database.annotation.Primary;
import io.fireflyest.emberlib.database.annotation.Table;

/**
 * 商人
 * 
 * @author Fireflyest
 * @since 1.0
 */
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

    /**
     * 构造方法
     */
    public Merchant() {
        // 默认构造方法
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isBlack() {
        return black;
    }

    public long getRegister() {
        return register;
    }

    public int getSelling() {
        return selling;
    }

    public int getSize() {
        return size;
    }

    public String getLogo() {
        return logo;
    }

    public String getStore() {
        return store;
    }

    public int getStar() {
        return star;
    }

    public int getVisit() {
        return visit;
    }

}
