package com.fireflyest.market.view;

<<<<<<< HEAD
import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.core.MarketManager;
import com.fireflyest.market.util.ConvertUtils;
import org.fireflyest.craftgui.api.View;

=======
>>>>>>> 9bc3b8e (版本更新)
import java.util.HashMap;

import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.api.View;

import com.fireflyest.market.data.Language;
import com.fireflyest.market.data.MarketYaml;
import com.fireflyest.market.service.MarketService;

public class AffairView implements View<AffairPage>{

    private final HashMap<String, AffairPage> pageMap = new HashMap<>();

    private final MarketService service;
    private final MarketYaml yaml;
    
    public AffairView(MarketService service, MarketYaml yaml) {
        this.service = service;
        this.yaml = yaml;
    }

    @Override
    public AffairPage getFirstPage(String target) {
        int id = NumberConversions.toInt(target);
        String nickname = service.selectTransactionNickname(id);
        pageMap.computeIfAbsent(target, k -> new AffairPage(Language.TITLE_AFFAIR_PAGE.replace("%item%", nickname), target, id, service, yaml));
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        // 
    }
    
}
