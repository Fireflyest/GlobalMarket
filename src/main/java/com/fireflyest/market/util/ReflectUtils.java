package com.fireflyest.market.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射
 * @author Fireflyest
 */
public class ReflectUtils {

    private ReflectUtils(){
    }

    /**
     * 反射执行Get
     * @param obj 反射对象
     * @param field Get变量
     * @return 执行得到的值
     */
    public static Object invokeGet(Object obj, String field){
        Class<?> clazz = obj.getClass();
        try {
            String name = field.substring(0,1).toUpperCase() + field.substring(1);
            Method method = getMethod(clazz, "get" + name);
            if (method == null) method = getMethod(clazz, "is" + name);
            if (method == null) return null;
            method.setAccessible(true);
            return method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射执行Set
     * @param field Set变量
     * @param value 设置的值
     */
    public static <T> void invokeSet(T t, String field, Object value){
        Class<?> clazz = t.getClass();
        String name = field.substring(0,1).toUpperCase() + field.substring(1);
        Method method = getMethod(clazz, "set" + name, getBaseClass(value.getClass()));
        try {
            if (method != null) {
                method.setAccessible(true);
                method.invoke(t, value);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static Method getMethod(Class<?> clazz, String name, Class<?>... cs){
        if (clazz.getSuperclass() != null && !clazz.getSuperclass().getTypeName().equals("java.lang.Object")){
            Method method = getMethod(clazz.getSuperclass(), name, cs);
            if (method != null) return method;
        }
        try {
            return clazz.getMethod(name, cs);
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    public static List<Field> getClassFields(Class<?> clazz){
        List<Field> fields = new ArrayList<>();
        if (clazz.getSuperclass() != null){
            fields.addAll(getClassFields(clazz.getSuperclass()));
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    private static Class<?> getBaseClass(Class<?> clazz){
        switch (clazz.getSimpleName()){
            case "Integer" :
                return int.class;
            case "Long":
                return long.class;
            case "Double":
                return double.class;
            case "Boolean":
                return boolean.class;
            case "Short":
                return short.class;
            case "Float":
                return float.class;
            default:
                return clazz;
        }
    }

}
