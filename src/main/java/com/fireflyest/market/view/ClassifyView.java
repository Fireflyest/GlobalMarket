package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class ClassifyView implements View<ClassifyPage> {

    public static final String EDIBLE = "edible";
    public static final String ITEM = "item";
    public static final String BLOCK = "block";
    public static final String BURNABLE = "burnable";
    public static final String EQUIP = "equip";
    public static final String KNOWLEDGE = "knowledge";


    public final String title;
    private final Map<String, ClassifyPage> pageMap = new HashMap<>();

    public ClassifyView(String title) {
        this.title = title;
    }

    @Override
    public ClassifyPage getFirstPage(String target){
        if (! pageMap.containsKey(target)){
            pageMap.put(target, new ClassifyPage(title, target, 1, 54));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
