package com.fireflyest.market.service;

import java.util.UUID;

import org.fireflyest.craftdatabase.annotation.Auto;
import org.fireflyest.craftdatabase.annotation.Service;
import org.fireflyest.craftdatabase.sql.SQLService;

import com.fireflyest.market.bean.Delivery;
import com.fireflyest.market.bean.Merchant;
import com.fireflyest.market.bean.Transaction;
import com.fireflyest.market.dao.DeliveryDao;
import com.fireflyest.market.dao.MerchantDao;
import com.fireflyest.market.dao.TransactionDao;

@Service
public class MarketService extends SQLService {

    @Auto
    public DeliveryDao deliveryDao;

    @Auto
    public MerchantDao merchantDao;

    @Auto
    public TransactionDao transactionDao; 

    /**
     * 数据服务
     * @param url sql地址
     */
    public MarketService(String url) {
        super(url);
    }
    

    public Merchant selectMerchantByUid(UUID uid) {
        return merchantDao.selectMerchantByUid(uid.toString());
    }

    public Merchant selectMerchantByName(String name) {
        return merchantDao.selectMerchantByName(name);
    }

    public Merchant[] selectMerchants(int start, int end) {
        return merchantDao.selectMerchants(start, end);
    }

    public long insertMerchant(String name, UUID uid, long register) {
        return merchantDao.insertMerchant(name, uid.toString(), register);
    }

    public String selectMerchantName(UUID uid) {
        return merchantDao.selectMerchantName(uid.toString());
    }

    public int selectMerchantSize(String name) {
        return merchantDao.selectMerchantSize(name);
    }

    public int selectMerchantCredit(UUID uid) {
        return merchantDao.selectMerchantCredit(uid.toString());
    }

    public int selectMerchantAmount(UUID uid) {
        return merchantDao.selectMerchantAmount(uid.toString());
    }

    public int selectMerchantSelling(UUID uid) {
        return merchantDao.selectMerchantSelling(uid.toString());
    }

    public boolean selectMerchantBlack(UUID uid) {
        return merchantDao.selectMerchantBlack(uid.toString());
    }

    public String selectMerchantUid(String name) {
        return merchantDao.selectMerchantUid(name);
    }

    public String selectMerchantLogo(String name) {
        return merchantDao.selectMerchantLogo(name);
    }

    public long updateMerchantSelling(String update, String uid) {
        return merchantDao.updateMerchantSelling(update, uid);
    }

    public long updateMerchantCredit(String update, String uid) {
        return merchantDao.updateMerchantCredit(update, uid);
    }

    public long updateMerchantAmount(String uid) {
        return merchantDao.updateMerchantAmount(uid);
    }

    public long updateMerchantVisit(String uid) {
        return merchantDao.updateMerchantVisit(uid);
    }

    public long updateMerchantStar(String uid) {
        return merchantDao.updateMerchantStar(uid);
    }

    public long updateMerchantSize(String uid, int size) {
        return merchantDao.updateMerchantSize(uid, size);
    }

    public long updateMerchantBlack(String uid, int black) {
        return merchantDao.updateMerchantBlack(uid, black);
    }

    public long updateMerchantStore(UUID uid, String store) {
        return merchantDao.updateMerchantStore(uid.toString(), store);
    }

    public long updateMerchantLogo(String name, String logo) {
        return merchantDao.updateMerchantLogo(name, logo);
    }




    public long[] selectDeliveryIdByOwner(UUID owner) {
        return deliveryDao.selectDeliveryIdByOwner(owner.toString());
    }

    public Delivery selectDeliveryById(long id) {
        return deliveryDao.selectDeliveryById(id);
    }

    public Delivery[] selectDeliveryByOwner(String owner) {
        return deliveryDao.selectDeliveryByOwner(owner);
    }

    public long insertDelivery(String stack, String owner, String sender, long appear, double price, String currency, String extras) {
        return deliveryDao.insertDelivery(stack, owner, sender, appear, price, currency, extras);
    }

    public long updateDeliveryInfo(String info, long id) {
        return deliveryDao.updateDeliveryInfo(info, id);
    }

    public long deleteDelivery(long id) {
        return deliveryDao.deleteDelivery(id);
    }




    public long insertTransaction(String stack, UUID owner, String ownerName, String nickname, double price, long appear) {
        return transactionDao.insertTransaction(stack, owner.toString(), ownerName, nickname, price, appear);
    }

    public Transaction selectTransactionById(long id) {
        return transactionDao.selectTransactionById(id);
    }

    public Transaction[] selectTransactions(int start, int end) {
        return transactionDao.selectTransactions(start, end);
    }

    public Transaction[] selectTransactionByType(String type, int start, int end) {
        return transactionDao.selectTransactionByType(type, start, end);
    }

    public Transaction[] selectTransactionByCurrency(String currency, int start, int end) {
        return transactionDao.selectTransactionByCurrency(currency, start, end);
    }

    public Transaction[] selectTransactionByCategory(int category, int start, int end) {
        return transactionDao.selectTransactionByCategory(category, start, end);
    }

    public Transaction[] selectTransactionBySearch(String search, int start, int end) {
        return transactionDao.selectTransactionBySearch(search, start, end);
    }

    public Transaction[] selectTransactionByOwnerName(String ownerName, int start, int end) {
        return transactionDao.selectTransactionByOwnerName(ownerName, start, end);
    }

    public long[] selectTransactionCancel(long deadline) {
        return transactionDao.selectTransactionCancel(deadline);
    }

    public long[] selectTransactionPrepareCancel(long deadline) {
        return transactionDao.selectTransactionPrepareCancel(deadline);
    }

    public long[] selectTransactionIdByType(String type) {
        return transactionDao.selectTransactionIdByType(type);
    }

    public long[] selectTransactionIdByHeat(int min) {
        return transactionDao.selectTransactionIdByHeat(min);
    }

    public String selectTransactionType(long id) {
        return transactionDao.selectTransactionType(id);
    }

    public String selectTransactionStack(long id) {
        return transactionDao.selectTransactionStack(id);
    }

    public String selectTransactionOwnerName(long id) {
        return transactionDao.selectTransactionOwnerName(id);
    }

    public String selectTransactionOwner(long id) {
        return transactionDao.selectTransactionOwner(id);
    }

    public String selectTransactionCurrency(long id) {
        return transactionDao.selectTransactionCurrency(id);
    }

    public String selectTransactionTarget(long id) {
        return transactionDao.selectTransactionTarget(id);
    }

    public String selectTransactionDesc(long id) {
        return transactionDao.selectTransactionDesc(id);
    }

    public String selectTransactionNickname(long id) {
        return transactionDao.selectTransactionNickname(id);   
    }

    public double selectTransactionCost(long id) {
        return transactionDao.selectTransactionCost(id);
    }

    public double selectTransactionPrice(long id) {
        return transactionDao.selectTransactionPrice(id);
    }

    public int selectTransactionHeat(long id) {
        return transactionDao.selectTransactionHeat(id);
    }

    public long updateTransactionType(String type, long id) {
        return transactionDao.updateTransactionType(type, id);
    }

    public long updateTransactionCost(double cost, long id) {
        return transactionDao.updateTransactionCost(cost, id);
    }

    public long updateTransactionPrice(double price, long id) {
        return transactionDao.updateTransactionPrice(price, id);
    }

    public long updateTransactionHeat(int heat, long id) {
        return transactionDao.updateTransactionHeat(heat, id);
    }

    public long updateTransactionCategory(long category, long id) {
        return transactionDao.updateTransactionCategory(category, id);
    }

    public long updateTransactionTarget(String buyer, long id) {
        return transactionDao.updateTransactionTarget(buyer, id);
    }

    public long updateTransactionDesc(String desc, long id) {
        return transactionDao.updateTransactionDesc(desc, id);
    }

    public long updateTransactionStack(String stack, long id) {
        return transactionDao.updateTransactionStack(stack, id);
    }

    public long updateTransactionCurrency(String currency, long id) {
        return transactionDao.updateTransactionCurrency(currency, id);
    }

    public long updateTransactionExtras(String extras, long id) {
        return transactionDao.updateTransactionExtras(extras, id);
    }

    public long deleteTransaction(long id) {
        return transactionDao.deleteTransaction(id);
    }

}
