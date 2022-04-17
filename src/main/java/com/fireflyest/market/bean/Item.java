package com.fireflyest.market.bean;

/**
 * @author Fireflyest
 * 2021/3/26 12:08
 */

public class Item  implements Comparable<Item>{

    // 物品id
    private int id;

    // 物品
    private String stack;
    private String meta;
    // nbt数据
    private String nbt;

    // 创建时间
    private long appear;

    public Item(int id, String stack, String meta, String nbt, long appear) {
        this.id = id;
        this.stack = stack;
        this.meta = meta;
        this.appear = appear;
    }

    public Item() {
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

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public long getAppear() {
        return appear;
    }

    public void setAppear(long appear) {
        this.appear = appear;
    }

    public String getNbt() {
        return nbt;
    }

    public void setNbt(String nbt) {
        this.nbt = nbt;
    }

    @Override
    public int compareTo(Item o) {
        if (appear > o.appear) return 1;
        if (appear < o.appear) return -1;
        if (id > o.id) return 1;
        if (id < o.id) return -1;
        return 0;
    }
}
