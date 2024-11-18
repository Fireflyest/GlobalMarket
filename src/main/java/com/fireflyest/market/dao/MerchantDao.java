package com.fireflyest.market.dao;

import io.fireflyest.emberlib.database.annotation.Dao;
import io.fireflyest.emberlib.database.annotation.Insert;
import io.fireflyest.emberlib.database.annotation.Select;
import io.fireflyest.emberlib.database.annotation.Update;

import com.fireflyest.market.bean.Merchant;

@Dao("com.fireflyest.market.bean.Merchant")
public interface MerchantDao {
    
    /**
     * 唯一id选择
     * @param uid 唯一id
     * @return 玩家数据
     */
    @Select("SELECT * FROM `market_merchant` WHERE `uid`='${uid}';")
    Merchant selectMerchantByUid(String uid);

    /**
     * 名称选择
     * @param name 名称
     * @return 玩家数据
     */
    @Select("SELECT * FROM `market_merchant` WHERE `name`='${name}' LIMIT 1;")
    Merchant selectMerchantByName(String name);

    @Select("SELECT * FROM `market_merchant` WHERE `selling`>0 AND `black`=0 LIMIT ${start},${end};")
    Merchant[] selectMerchants(int start, int end);

    /**
     * 插入玩家数据
     * @return id
     */
    @Insert("INSERT INTO `market_merchant` (`name`,`uid`,`register`) VALUES ('${name}','${uid}',${register});")
    long insertMerchant(String name, String uid, long register);

    /**
     * 获取名称
     * @param uid uid
     * @return 游戏名
     */
    @Select("SELECT `name` FROM `market_merchant` WHERE `uid`='${uid}';")
    String selectMerchantName(String uid);

    @Select("SELECT `size` FROM `market_merchant` WHERE `name`='${name}';")
    int selectMerchantSize(String name);

    @Select("SELECT `credit` FROM `market_merchant` WHERE `uid`='${uid}';")
    int selectMerchantCredit(String uid);

    @Select("SELECT `amount` FROM `market_merchant` WHERE `uid`='${uid}';")
    int selectMerchantAmount(String uid);

    @Select("SELECT `selling` FROM `market_merchant` WHERE `uid`='${uid}';")
    int selectMerchantSelling(String uid);

    @Select("SELECT `black` FROM `market_merchant` WHERE `uid`='${uid}';")
    boolean selectMerchantBlack(String uid);

    @Select("SELECT `uid` FROM `market_merchant` WHERE `name`='${name}';")
    String selectMerchantUid(String name);

    @Select("SELECT `logo` FROM `market_merchant` WHERE `name`='${name}';")
    String selectMerchantLogo(String name);

    @Update("UPDATE `market_merchant` SET `selling`=`selling`${update} WHERE `uid`='${uid}';")
    long updateMerchantSelling(String update, String uid);
    
    @Update("UPDATE `market_merchant` SET `credit`=`credit`${update} WHERE `uid`='${uid}';")
    long updateMerchantCredit(String update, String uid);

    @Update("UPDATE `market_merchant` SET `amount`=`amount`+1 WHERE `uid`='${uid}';")
    long updateMerchantAmount(String uid);

    @Update("UPDATE `market_merchant` SET `visit`=`visit`+1 WHERE `uid`='${uid}';")
    long updateMerchantVisit(String uid);

    @Update("UPDATE `market_merchant` SET `star`=`star`+1 WHERE `uid`='${uid}';")
    long updateMerchantStar(String uid);

    @Update("UPDATE `market_merchant` SET `size`=${size} WHERE `uid`='${uid}';")
    long updateMerchantSize(String uid, int size);

    @Update("UPDATE `market_merchant` SET `black`=${black} WHERE `uid`='${uid}';")
    long updateMerchantBlack(String uid, int black);

    @Update("UPDATE `market_merchant` SET `store`='${store}' WHERE `uid`='${uid}';")
    long updateMerchantStore(String uid, String store);

    @Update("UPDATE `market_merchant` SET `logo`='${logo}' WHERE `name`='${name}';")
    long updateMerchantLogo(String name, String logo);

}
