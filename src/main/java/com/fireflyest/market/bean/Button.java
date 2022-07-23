package com.fireflyest.market.bean;

public class Button {

    private String target;

    private String material;

    private String display;

    public Button() {
    }

    public Button(String target, String material, String display) {
        this.target = target;
        this.material = material;
        this.display = display;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
