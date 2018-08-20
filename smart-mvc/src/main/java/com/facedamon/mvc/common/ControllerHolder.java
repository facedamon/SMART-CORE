package com.facedamon.mvc.common;

import com.facedamon.mvc.common.annotation.Action;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
* @Description:    request映射处理器
* @Author:         facedamon
* @CreateDate:     2018/8/1 17:38
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 17:38
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public final class ControllerHolder {
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<>();

    private static final String REQUEST_REG = "\\w+:/\\w*";

    static {
        Set<Class<?>> controllerSet =  ClassHolder.getControllerClassSet();
        if (null != controllerSet && !controllerSet.isEmpty()){
            for (Class<?> controllerClass : controllerSet){
                Method[] methods = controllerClass.getDeclaredMethods();
                if (null != methods && methods.length > 0){
                    for (Method method : methods){
                        if (method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            if (null != action){
                                String mapping = action.value();
                                if (StringUtils.isNotBlank(mapping)){
                                    if (mapping.matches(REQUEST_REG)){
                                        String[] array = StringUtils.split(":");
                                        if (null != array && array.length == 2) {
                                            String requestMethod = array[0];
                                            String requestPath = array[1];
                                            Request request = Request.builder().requestMethod(requestMethod).requestPath(requestPath).build();
                                            Handler handler = Handler.builder().actionMethod(method).controllerClass(controllerClass).build();
                                            ACTION_MAP.put(request,handler);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Request request = Request.builder().requestMethod(requestMethod).requestPath(requestPath).build();
        return ACTION_MAP.get(request);
    }
}
