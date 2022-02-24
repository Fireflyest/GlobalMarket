package com.fireflyest.market.sql;

import com.fireflyest.market.data.Data;
import com.fireflyest.market.data.Storage;
import com.fireflyest.market.util.MysqlExecuteUtils;

import java.util.List;

/**
 * @author Fireflyest
 * 2022/1/6 17:11
 */

public class SqlData implements Data {

    private final Storage storage;
    
    public SqlData(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void createTable(Class<?> aClass) {
        storage.createTable(MysqlExecuteUtils.createTable(aClass));
    }

    @Override
    public <T> List<T> query(Class<T> aClass) {
        return storage.inquiryList(MysqlExecuteUtils.query(aClass), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s) {
        return storage.inquiryList(MysqlExecuteUtils.query(aClass, s), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s, Object o) {
        return storage.inquiryList(MysqlExecuteUtils.query(aClass, s, o), aClass);
    }

    @Override
    public <T> T queryOne(Class<T> aClass, String s, Object o) {
        List<T> list = query(aClass, s, o);
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, int i, int i1) {
        return storage.inquiryList(MysqlExecuteUtils.query(aClass, i, i1), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s, int i, int i1) {
        return storage.inquiryList(MysqlExecuteUtils.query(aClass, s, i, i1), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s, Object o, int i, int i1) {
        return storage.inquiryList(MysqlExecuteUtils.query(aClass, s, o, i, i1), aClass);
    }

    @Override
    public void update(Object o) {
        storage.update(MysqlExecuteUtils.update(o));
    }

    @Override
    public void delete(Class<?> aClass, String s, Object o) {
        storage.delete(MysqlExecuteUtils.delete(aClass, s, o));
    }

    @Override
    public void delete(Class<?> aClass, String s) {
        storage.delete(MysqlExecuteUtils.delete(aClass, s));
    }

    @Override
    public void delete(Object o) {
        storage.delete(MysqlExecuteUtils.delete(o));
    }

    @Override
    public int insert(Object o) {
        return storage.insert(MysqlExecuteUtils.insert(o));
    }
    
}
