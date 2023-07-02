package com.fireflyest.market.dao;

import org.fireflyest.craftdatabase.annotation.Dao;
import org.fireflyest.craftdatabase.annotation.Delete;
import org.fireflyest.craftdatabase.annotation.Insert;
import org.fireflyest.craftdatabase.annotation.Select;
import org.fireflyest.craftdatabase.annotation.Update;

import com.fireflyest.market.bean.Transaction;

@Dao("com.fireflyest.market.bean.Transaction")
public interface TransactionDao {

    @Insert("INSERT INTO `market_transaction` (`stack`,`owner`,`ownerName`,`nickname`,`price`,`cost`,`appear`) VALUES ('${stack}','${owner}','${ownerName}','${nickname}',${price},${price},${appear});")
    long insertTransaction(String stack, String owner, String ownerName, String nickname, double price, long appear);
    
    @Select("SELECT * FROM `market_transaction` WHERE `id`=${id};")
    Transaction selectTransactionById(long id);

    @Select("SELECT * FROM `market_transaction` ORDER BY `id` DESC LIMIT ${start},${end};")
    Transaction[] selectTransactions(int start, int end);

    @Select("SELECT * FROM `market_transaction`WHERE `type` LIKE '%${type}%' ORDER BY `id` DESC LIMIT ${start},${end};")
    Transaction[] selectTransactionByType(String type, int start, int end);

    @Select("SELECT * FROM `market_transaction`WHERE `currency`='${currency}' ORDER BY `id` DESC LIMIT ${start},${end};")
    Transaction[] selectTransactionByCurrency(String currency, int start, int end);

    @Select("SELECT * FROM `market_transaction`WHERE `category` & '${category}' > 0 ORDER BY `id` DESC LIMIT ${start},${end};")
    Transaction[] selectTransactionByCategory(int category, int start, int end);

    @Select("SELECT * FROM `market_transaction`WHERE `nickname` LIKE '%${search}%' OR `stack` LIKE '%${search}%' ORDER BY `id` DESC  LIMIT ${start},${end};")
    Transaction[] selectTransactionBySearch(String search, int start, int end);

    @Select("SELECT * FROM `market_transaction`WHERE `ownerName`='${ownerName}' ORDER BY `id` DESC LIMIT ${start},${end};")
    Transaction[] selectTransactionByOwnerName(String ownerName, int start, int end);

    @Select("SELECT `id` FROM `market_transaction` WHERE `appear` < ${deadline} AND `type`<>'auction' AND `type` NOT LIKE 'admin%';")
    long[] selectTransactionCancel(long deadline);
    
    @Select("SELECT `id` FROM `market_transaction` WHERE `type` LIKE '%${type}%';")
    long[] selectTransactionIdByType(String type);

    @Select("SELECT `id` FROM `market_transaction` WHERE `heat`>${min} AND `type`<>'auction' ORDER BY `heat` LIMIT 10;")
    long[] selectTransactionIdByHeat(int min);

    @Select("SELECT `type` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionType(long id);

    @Select("SELECT `stack` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionStack(long id);

    @Select("SELECT `ownerName` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionOwnerName(long id);

    @Select("SELECT `owner` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionOwner(long id);

    @Select("SELECT `currency` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionCurrency(long id);

    @Select("SELECT `target` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionTarget(long id);

    @Select("SELECT `desc` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionDesc(long id);

    @Select("SELECT `nickname` FROM `market_transaction` WHERE `id`=${id};")
    String selectTransactionNickname(long id);

    @Select("SELECT `cost` FROM `market_transaction` WHERE `id`=${id};")
    double selectTransactionCost(long id);

    @Select("SELECT `price` FROM `market_transaction` WHERE `id`=${id};")
    double selectTransactionPrice(long id);

    @Select("SELECT `heat` FROM `market_transaction` WHERE `id`=${id};")
    int selectTransactionHeat(long id);

    @Update("UPDATE `market_transaction` SET `type`='${type}' WHERE `id`=${id};")
    long updateTransactionType(String type, long id);

    @Update("UPDATE `market_transaction` SET `cost`=${cost} WHERE `id`=${id};")
    long updateTransactionCost(double cost, long id);

    @Update("UPDATE `market_transaction` SET `price`=${price} WHERE `id`=${id};")
    long updateTransactionPrice(double price, long id);

    @Update("UPDATE `market_transaction` SET `heat`=${heat} WHERE `id`=${id};")
    long updateTransactionHeat(int heat, long id);

    @Update("UPDATE `market_transaction` SET `category`=${category} WHERE `id`=${id};")
    long updateTransactionCategory(long category, long id);

    @Update("UPDATE `market_transaction` SET `target`='${target}' WHERE `id`=${id};")
    long updateTransactionTarget(String target, long id);

    @Update("UPDATE `market_transaction` SET `desc`='${desc}' WHERE `id`=${id};")
    long updateTransactionDesc(String desc, long id);

    @Update("UPDATE `market_transaction` SET `stack`='${stack}' WHERE `id`=${id};")
    long updateTransactionStack(String stack, long id);

    @Update("UPDATE `market_transaction` SET `currency`='${currency}' WHERE `id`=${id};")
    long updateTransactionCurrency(String currency, long id);

    @Update("UPDATE `market_transaction` SET `extras`='${extras}' WHERE `id`=${id};")
    long updateTransactionExtras(String extras, long id);

    @Delete("DELETE FROM `market_transaction` WHERE `id`=${id};")
    long deleteTransaction(long id);

}
