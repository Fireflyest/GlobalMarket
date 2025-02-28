package com.fireflyest.market.data;

import io.fireflyest.emberlib.config.annotation.Comments;
import io.fireflyest.emberlib.config.annotation.Entry;
import io.fireflyest.emberlib.config.annotation.Yaml;
import io.fireflyest.emberlib.data.Box;

/**
 * 配置文件
 * 
 * @author Fireflyest
 * @since 1.0
 */
@Yaml("config.yml")
public final class Config {

    private Config() {
        // EMPTY
    }

    @Entry
    @Comments("是否开启调试模式")
    public static final Box<Boolean> DEBUG = new Box<>(false);

    @Entry
    @Comments("语言文件")
    public static final Box<String> LANG = new Box<>("zh-CN");
    
    @Entry
    @Comments("数据库启用，如果不启用，则使用SQLite")
    public static final Box<Boolean> SQL_ENABLE = new Box<>(false);
    @Entry
    @Comments({"数据库地址", "示例 jdbc:mysql://localhost:3306/#####?useSSL=false&serverTimezone=UTC"})
    public static final Box<String> SQL_URL = new Box<>("jdbc:mysql://localhost:3306/mc_market");
    @Entry
    @Comments("数据库用户")
    public static final Box<String> SQL_USER = new Box<>("root");
    @Entry
    @Comments("数据库密码")
    public static final Box<String> SQL_PASSWORD = new Box<>("123456");
    
    @Entry
    @Comments("更新检测")
    public static final Box<Boolean> UPDATE_CHECK = new Box<>(true);
    
    @Entry
    @Comments("货币点券")
    public static final Box<Boolean> CURRENCY_POINT = new Box<>(false);

    @Entry
    @Comments("市场物品收购功能")
    public static final Box<Boolean> MARKET_ORDER = new Box<>(false);
    @Entry
    @Comments("市场物品交易功能")
    public static final Box<Boolean> MARKET_TRADE = new Box<>(false);
    @Entry
    @Comments("首页显示市场导航按钮")
    public static final Box<Boolean> MARKET_NAVIGATION = new Box<>(true);
    @Entry
    @Comments("市场物品自动分类")
    public static final Box<Boolean> MARKET_CATEGORY = new Box<>(true);
    
    @Entry
    @Comments("违禁品检测")
    public static final Box<Boolean> CONTRABAND_ENABLE = new Box<>(false);
    @Entry
    @Comments("违禁品词条，从物品的lore中检测")
    public static final Box<String> CONTRABAND_LORE = new Box<>("vip,binding,专属,禁止交易");
    
    @Entry
    @Comments("交易数量限制")
    public static final Box<Boolean> TRANSACTION_LIMIT = new Box<>(true);
    @Entry
    @Comments("交易数量限制数量")
    public static final Box<Integer> TRANSACTION_MAXIMUM = new Box<>(35);
    @Entry
    @Comments("交易公告")
    public static final Box<Boolean> TRANSACTION_BROADCAST = new Box<>(true);
    @Entry
    @Comments("交易拆分，如果开启可以不用一次性购买所有商品")
    public static final Box<Boolean> TRANSACTION_PARTIAL = new Box<>(true);
    @Entry
    @Comments("商品有效期，默认7天，-1为永久")
    public static final Box<Integer> TRANSACTION_EXPIRATION = new Box<>(7);

    @Entry
    @Comments("邮件数量限制")
    public static final Box<Boolean> MAIL_LIMIT = new Box<>(true);
    @Entry
    @Comments("邮件数量限制数量")
    public static final Box<Integer> MAIL_MAXIMUM = new Box<>(50);
    
    @Entry
    @Comments("定价限制")
    public static final Box<Integer> PRICE_MAX = new Box<>(99999999);
    @Entry
    @Comments("定价手续费是否启用")
    public static final Box<Boolean> PRICE_COMMISSION_ENABLE = new Box<>(true);
    @Entry
    @Comments("定价手续费阈值，上架的交易价格超过这个阈值收取手续费")
    public static final Box<Long> PRICE_COMMISSION_THRESHOLD = new Box<>(100000L);
    @Entry
    @Comments("定价手续费比例，手续费占交易价格的比例")
    public static final Box<Double> PRICE_COMMISSION_RATE = new Box<>(0.002);


}
