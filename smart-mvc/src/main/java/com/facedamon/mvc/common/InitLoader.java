package com.facedamon.mvc.common;

import com.facedamon.util.ClassUtil;

public class InitLoader {

    private static Class<?>[] classList = {
            ClassHolder.class,
            BeanHolder.class,
            IocHolder.class,
            ControllerHolder.class
    };

    public static void init(){
        for (Class<?> clazz : classList){
            ClassUtil.loadClassLazyQuietly(clazz.getName());
        }
    }
}
