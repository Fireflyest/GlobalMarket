package com.fireflyest.market.dao;

import io.fireflyest.emberlib.database.annotation.Dao;
import io.fireflyest.emberlib.database.annotation.Delete;
import io.fireflyest.emberlib.database.annotation.Insert;
import io.fireflyest.emberlib.database.annotation.Select;
import io.fireflyest.emberlib.database.annotation.Update;

import com.fireflyest.market.bean.Delivery;

@Dao("com.fireflyest.market.bean.Delivery")
public interface DeliveryDao {
    
    @Select("SELECT `id` FROM `market_delivery` WHERE `owner`='${owner}';")
    long[] selectDeliveryIdByOwner(String owner);

    @Select("SELECT * FROM `market_delivery` WHERE `id`=${id};")
    Delivery selectDeliveryById(long id);

    @Select("SELECT * FROM `market_delivery` WHERE `owner`='${owner}';")
    Delivery[] selectDeliveryByOwner(String owner);

    @Insert("INSERT INTO `market_delivery` (`stack`,`owner`,`sender`,`appear`,`price`,`currency`,`extras`) VALUES ('${stack}','${owner}','${sender}',${appear},${price},'${currency}','${extras}');")
    long insertDelivery(String stack, String owner, String sender, long appear, double price, String currency, String extras);

    @Update("UPDATE `market_delivery` SET `info`='${info}' WHERE `id`=${id};")
    long updateDeliveryInfo(String info, long id);

    @Delete("DELETE FROM `market_delivery` WHERE `id`=${id};")
    long deleteDelivery(long id);

}
