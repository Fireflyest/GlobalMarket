package com.fireflyest.market.view;

import org.fireflyest.craftgui.api.View;
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.util.ConvertUtils;

import java.util.HashMap;
import java.util.Map;

public class AffairView implements View<AffairPage> {

    private final Map<String, AffairPage> pageMap = new HashMap<>();

    private final String title;

    public AffairView(String title) {
        this.title = title;
    }

    @Override
    public AffairPage getFirstPage(String target){
        if (! pageMap.containsKey(target)){
            Sale sale = MarketManager.getSale(ConvertUtils.parseInt(target));
            if (sale != null) {
                pageMap.put(target, new AffairPage(title, target));
            }
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }

}
