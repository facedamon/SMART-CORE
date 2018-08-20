package com.facedamon.mvc.common;

import com.facedamon.mvc.common.annotation.Controller;
import com.facedamon.mvc.common.annotation.Service;
import com.facedamon.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
* @Description:    获取BasePackage下具有特定标识的类模板
* @Author:         facedamon
* @CreateDate:     2018/8/1 14:25
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 14:25
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class ClassHolder {

    /**
     * 存放加载的类集合
     */
    private static final Set<Class<?>> CLASS_SET;

    static {
        CLASS_SET = ClassUtil.getClassSetQuietly(ConfigHolder.getBasePackage());
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取所有@Service
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET){
            if (clazz.isAnnotationPresent(Service.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * 获取所有@Controller
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet = new HashSet<>();
        for (Class<?> clazz : CLASS_SET){
            if (clazz.isAnnotationPresent(Controller.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }

    /**
     * 获取所有的Bean
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classSet = new HashSet<>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }
}
