package com.facedamon.mvc.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
* @Description:    对应request的handler,封装controller和其中的映射方法
* @Author:         facedamon
* @CreateDate:     2018/8/1 17:37
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 17:37
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Handler {
    private Class<?> controllerClass;
    private Method actionMethod;
}
