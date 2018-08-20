package com.facedamon.mvc.common;

import com.facedamon.mvc.common.annotation.Autowired;
import com.facedamon.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
* @Description:    MVC IOC注入
 *                 所有的对象都是单例的
* @Author:         facedamon
* @CreateDate:     2018/8/1 17:03
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 17:03
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public final class IocHolder {
    static {
        Map<Class<?>,Object> beanMap = BeanHolder.getBeanMap();
        if (null != beanMap && !beanMap.isEmpty()){
            for (Map.Entry<Class<?>,Object> beanEntity : beanMap.entrySet()){
                Class<?> beanClass = beanEntity.getKey();
                Object beanInstance = beanEntity.getValue();
                if (null != beanClass){
                    Field[] fields = beanClass.getDeclaredFields();
                    if (null != fields && fields.length > 0){
                        for (Field field : fields){
                            if (null != field.getAnnotation(Autowired.class)){
                                Class<?> beanFieldClass = field.getType();
                                Object beanFieldInstance = beanMap.get(beanFieldClass);
                                if (null != beanFieldInstance){
                                    ReflectUtil.setField(beanInstance,field,beanFieldInstance);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
