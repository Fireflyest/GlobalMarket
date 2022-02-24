package com.fireflyest.market.data;

import java.util.List;

public interface Data {

    void createTable(Class<?> clazz);
    
    <T> List<T> query(Class<T> clazz);

    <T> List<T> query(Class<T> clazz, String condition);

    <T> List<T> query(Class<T> clazz, String key, Object value);

    <T> T queryOne(Class<T> clazz, String key, Object value);

    <T> List<T> query(Class<T> clazz, int start, int amount);

    <T> List<T> query(Class<T> clazz, String condition, int start, int amount);

    <T> List<T> query(Class<T> clazz, String key, Object value, int start, int amount);

    void update(Object o);

    void delete(Class<?> clazz, String key, Object value);

    void delete(Class<?> clazz, String condition);

    void delete(Object obj);

    int insert(Object obj);
}
