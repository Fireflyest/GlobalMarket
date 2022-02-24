package com.fireflyest.market.sqll;

import com.fireflyest.market.data.Data;
import com.fireflyest.market.data.Storage;
import com.fireflyest.market.util.SqliteExecuteUtils;

import java.util.List;

/**
 * @author Fireflyest
 * 2022/1/6 17:12
 */

public class SqLiteData implements Data {

    private final Storage storage;

    public SqLiteData(Storage storage) {
        this.storage = storage;
    }

    @Override
    public void createTable(Class<?> aClass) {
        storage.createTable(SqliteExecuteUtils.createTable(aClass));
    }

    @Override
    public <T> List<T> query(Class<T> aClass) {
        return storage.inquiryList(SqliteExecuteUtils.query(aClass), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s) {
        return storage.inquiryList(SqliteExecuteUtils.query(aClass, s), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s, Object o) {
        return storage.inquiryList(SqliteExecuteUtils.query(aClass, s, o), aClass);
    }

    @Override
    public  <T> T queryOne(Class<T> aClass, String s, Object o) {
        List<T> list = query(aClass, s, o);
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, int i, int i1) {
        return storage.inquiryList(SqliteExecuteUtils.query(aClass, i, i1), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s, int i, int i1) {
        return storage.inquiryList(SqliteExecuteUtils.query(aClass, s, i, i1), aClass);
    }

    @Override
    public <T> List<T> query(Class<T> aClass, String s, Object o, int i, int i1) {
        return storage.inquiryList(SqliteExecuteUtils.query(aClass, s, o, i, i1), aClass);
    }

    @Override
    public void update(Object o) {
        storage.update(SqliteExecuteUtils.update(o));
    }

    @Override
    public void delete(Class<?> aClass, String s, Object o) {
        storage.delete(SqliteExecuteUtils.delete(aClass, s, o));
    }

    @Override
    public void delete(Class<?> aClass, String s) {
        storage.delete(SqliteExecuteUtils.delete(aClass, s));
    }

    @Override
    public void delete(Object o) {
        storage.delete(SqliteExecuteUtils.delete(o));
    }

    @Override
    public int insert(Object o) {
        return storage.insert(SqliteExecuteUtils.insert(o));
    }

}
