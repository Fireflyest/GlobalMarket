package com.fireflyest.market.data;

import java.sql.SQLException;
import java.util.List;

public interface Storage {

    /**
     * 查询一个对象
     * select * from TABLE where CONDITION
     * @param sql 查询语句
     * @param type 查询对象的类
     * @return 查询结果
     */
    <T> T inquiry(String sql, Class<T> type);

    /**
     * 查询所有对象
     * select * from TABLE where CONDITION limit N,M
     * @param sql 查询语句
     * @param type 查询对象的类
     * @return 查询结果
     */
    <T> List<T> inquiryList(String sql, Class<T> type);

    /**
     * 更新数据
     * update TABLE set KEY=VALUE,KEY=VALUE,... where CONDITION
     * @param sql 更新语句
     */
    void update(String sql);

    /**
     * 删除数据
     * delete from TABLE where CONDITION
     * @param sql 删除语句
     */
    void delete(String sql);

    /**
     * 插入数据
     * insert into TABLE (KEY,KEY,...) values (VALUE,VALUE,...)
     * @param sql 插入语句
     * @return 返回id
     */
    int insert(String sql);

    void createTable(String sql);

    void close() throws SQLException;

}
