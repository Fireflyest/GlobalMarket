package com.fireflyest.market.data;

import com.fireflyest.market.bean.Sale;
import com.fireflyest.market.bean.User;
import com.fireflyest.market.util.MysqlExecuteUtils;
import com.fireflyest.market.util.SqliteExecuteUtils;
import com.fireflyest.market.util.TimeUtils;
import org.junit.Test;

/**
 * @author Fireflyest
 * @since 2022/8/9
 */
public class DataTest {

    public DataTest() {
    }

    @Test
    public void test(){
        User user = new User("aa'a", "player.getUniqueId().toString()", 100, 0.0, 0, false, TimeUtils.getDate(), 0);

        System.out.println("Sqlite = " + SqliteExecuteUtils.createTable(User.class));
        System.out.println("Sqlite = " + SqliteExecuteUtils.insert(user));
        System.out.println("Sqlite = " + SqliteExecuteUtils.delete(user));
        System.out.println("Sqlite = " + SqliteExecuteUtils.update(user));
        System.out.println("Sqlite = " + SqliteExecuteUtils.query(User.class, "name", "aa'a"));

        System.out.println("------------------- ");

        System.out.println("Sql = " + MysqlExecuteUtils.createTable(User.class));
        System.out.println("Sql = " + MysqlExecuteUtils.insert(user));
        System.out.println("Sql = " + MysqlExecuteUtils.delete(user));
        System.out.println("Sql = " + MysqlExecuteUtils.update(user));
        System.out.println("Sql = " + MysqlExecuteUtils.query(User.class, "name", "aa'a"));

        System.out.println("------------------- ");

        System.out.println("Sale = " + MysqlExecuteUtils.createTable(Sale.class));
    }

}
