package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class ClassifyView implements View<ClassifyPage> {

    public static final String EDIBLE = "edible";
    public static final String ITEM = "item";
    public static final String BLOCK = "block";
    public static final String BURNABLE = "burnable";
    public static final String INTERACTABLE = "interactable";
    public static final String EQUIP = "equip";
    public static final String KNOWLEDGE = "knowledge";


    private final Map<String, ClassifyPage> pageMap = new HashMap<>();

    public ClassifyView(String title) {
        pageMap.put(EDIBLE, new ClassifyPage(title, EDIBLE, 1, 54));
        pageMap.put(ITEM, new ClassifyPage(title, ITEM, 1, 54));
        pageMap.put(BLOCK, new ClassifyPage(title, BLOCK, 1, 54));
        pageMap.put(BURNABLE, new ClassifyPage(title, BURNABLE, 1, 54));
        pageMap.put(INTERACTABLE, new ClassifyPage(title, INTERACTABLE, 1, 54));
        pageMap.put(EQUIP, new ClassifyPage(title, EQUIP, 1, 54));
        pageMap.put(KNOWLEDGE, new ClassifyPage(title, KNOWLEDGE, 1, 54));
    }

    @Override
    public ClassifyPage getFirstPage(String target){
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
