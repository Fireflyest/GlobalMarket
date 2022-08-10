package com.fireflyest.market.sqll;

import com.fireflyest.market.data.Storage;
import com.fireflyest.market.util.ReflectUtils;
import com.fireflyest.market.util.SqliteUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 * 2022/1/3 11:11
 */

public class SqLiteStorage implements Storage {

    private final Connection connection;

    public SqLiteStorage(String url) throws SQLException {
        SqliteUtils.init();

        // 连接
        connection = SqliteUtils.getConnection(url);
    }

    @Override
    public <T> T inquiry(String s, Class<T> aClass) {
        List<T> list = this.inquiryList(s, aClass);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public <T> List<T> inquiryList(String s, Class<T> aClass) {
        List<T> list = new ArrayList<>();
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(s);
//            System.out.println("s = " + s);
            while (resultSet.next()){
                T t = aClass.getDeclaredConstructor().newInstance();
                for(Field field : ReflectUtils.getClassFields(aClass)){
                    if (float.class.equals(field.getType())) {
                        ReflectUtils.invokeSet(t, field.getName(), resultSet.getFloat(field.getName()));
                    } else if (double.class.equals(field.getType())) {
                        ReflectUtils.invokeSet(t, field.getName(), resultSet.getDouble(field.getName()));
                    } else if (boolean.class.equals(field.getType())) {
                        ReflectUtils.invokeSet(t, field.getName(), resultSet.getBoolean(field.getName()));
                    } else if (long.class.equals(field.getType())) {
                        ReflectUtils.invokeSet(t, field.getName(), resultSet.getLong(field.getName()));
                    } else {
                        ReflectUtils.invokeSet(t, field.getName(), resultSet.getObject(field.getName()));
                    }
                }
                list.add(t);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            SqliteUtils.close(resultSet, statement);
        }
        return list;
    }

    @Override
    public void update(String s) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteUtils.close(statement);
        }
    }

    @Override
    public void delete(String s) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteUtils.close(statement);
        }
    }

    @Override
    public int insert(String s) {
        int id = 0;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()){
                id = resultSet.getInt(1);//返回主键值
            }
            preparedStatement.close();
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SqliteUtils.close(resultSet, statement);
        }
        return id;
    }

    @Override
    public void createTable(String s) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            SqliteUtils.close(statement);
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
