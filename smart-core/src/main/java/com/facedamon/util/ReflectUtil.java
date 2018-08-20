package com.facedamon.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
* @Description:    反射工具
* @Author:         facedamon
* @CreateDate:     2018/8/1 14:34
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 14:34
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public class ReflectUtil {
    /**
     * 通过类模板创建实例
     * @param clazz
     * @param <T>
     * @return
     * @throws SQLException
     */
    public static <T> T newInstance(Class<T> clazz){
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not create "+clazz.getName()+" : "+e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not create "+clazz.getName()+" : "+e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not create "+clazz.getName()+" : "+e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not create "+clazz.getName()+" : "+e.getMessage());
        }
    }

    /**
     * 加载obj 的method方法
     * @param obj
     * @param method
     * @param args
     * @return
     */
    public static Object invokeMethod(Object obj, Method method,Object... args){
        Object result = null;
        try {
            method.setAccessible(true);
            result = method.invoke(obj, args);
        } catch (IllegalAccessException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not invoke "+ method.getName()+" : "+e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not invoke "+ method.getName()+" : "+e.getMessage());
        }
        return result;
    }

    /**
     * 设置obj的field字段值
     * @param obj
     * @param field
     * @param arg
     */
    public static void setField(Object obj, Field field,Object arg){
        try {
            field.setAccessible(true);
            field.set(obj,arg);
        } catch (IllegalAccessException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new RuntimeException("Can not set field "+ field.getName()+" : "+e.getMessage());
        }
    }
}
