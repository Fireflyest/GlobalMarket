package com.fireflyest.market.data;

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

    public static final Box<Boolean> DEBUG = new Box<>(false);

    public static final Box<String> LANG = new Box<>("");
    
    public static final Box<Boolean> SQL_ENABLE = new Box<>(false);
    public static final Box<String> SQL_URL = new Box<>("");
    public static final Box<String> SQL_USER = new Box<>("");
    public static final Box<String> SQL_PASSWORD = new Box<>("");
    
    public static final Box<Boolean> UPDATE_CHECK = new Box<>(false);
    
    public static final Box<Boolean> PLAYER_POINT_MARKET = new Box<>(false);
    public static final Box<Boolean> ORDER_MARKET = new Box<>(false);
    public static final Box<Boolean> TRADE_MARKET = new Box<>(false);
    
    public static final Box<Integer> TERM_OF_VALIDITY = new Box<>(0);
    
    public static final Box<Boolean> CONTRABAND_LORE = new Box<>(false);
    public static final Box<String> CONTRABAND_LORE_LIST = new Box<>("");
    
    public static final Box<Boolean> MAXIMUM_SALE = new Box<>(false);
    public static final Box<Integer> MAXIMUM_SALE_NUM = new Box<>(0);
    public static final Box<Boolean> MAXIMUM_MAIL = new Box<>(false);
    public static final Box<Integer> MAXIMUM_MAIL_NUM = new Box<>(0);
    
    public static final Box<Boolean> ACTION_BROADCAST = new Box<>(false);
    
    public static final Box<Integer> MAX_PRICE = new Box<>(0);
    
    public static final Box<Boolean> COMMISSION = new Box<>(false);
    public static final Box<Long> COMMISSION_THRESHOLD = new Box<>(0L);
    public static final Box<Double> COMMISSION_RATE = new Box<>(0.0);
}
