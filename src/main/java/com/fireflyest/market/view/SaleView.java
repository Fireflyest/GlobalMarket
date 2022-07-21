package com.fireflyest.market.view;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.util.ConvertUtils;
import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;


public class SaleView implements View<SalePage> {

    private final Map<String, SalePage> pageMap = new HashMap<>();
    private final String title;

    public SaleView(String title) {
        this.title = title;
    }

    @Override
    public SalePage getFirstPage(String target){
        if (! pageMap.containsKey(target)){
            Sale sale = MarketManager.getSale(ConvertUtils.parseInt(target));
            if (sale != null) {
                pageMap.put(target, new SalePage(title, target));
            }
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }

}
