package com.fireflyest.market.view;

import com.fireflyest.market.core.MarketItem;
import com.fireflyest.market.data.Language;
import io.fireflyest.emberlib.inventory.Page;

/**
 * 管理页面
 * 
 * @author Fireflyest
 * @since 3.3
 */
public class ManagePage extends Page {

    protected ManagePage() {
        super("", 0, 27);

        this.setup(Language.TITLE_MANAGE.get());
    }

    @Override
    public void refreshPage() {
        if (!init) {
            this.initPage();
        }

        this.slot(9, MarketItem.getPair("statistic"));
    }

    @Override
    public void initPage() {
        super.initPage();
        this.slot(0, MarketItem.getPair("reload"));
        this.slot(1, MarketItem.getPair("check"));
        this.slot(26, MarketItem.getPair("back"));
    }
    
}
