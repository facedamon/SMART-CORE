package com.facedamon.mvc.common;

import com.facedamon.util.ReflectUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
* @Description:    Bean模板与实例的映射关系
* @Author:         facedamon
* @CreateDate:     2018/8/1 16:40
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 16:40
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public final class BeanHolder {
    private static final Map<Class<?>,Object> BEAN_MAP = new HashMap<>();

    static {
        Set<Class<?>> classSet = ClassHolder.getBeanClassSet();
        for (Class<?> clazz : classSet){
            Object instance = ReflectUtil.newInstance(clazz);
            BEAN_MAP.put(clazz,instance);
        }
    }

    /**
     * 获取bean映射关系
     * @return
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        if (!BEAN_MAP.containsKey(clazz)){
            throw new RuntimeException("can not get bean of "+ clazz.getName());
        }
        return (T)BEAN_MAP.get(clazz);
    }
}
