package com.facedamon.mvc.common.model;

import java.util.Map;

/**
* @Description:    请求参数对象
* @Author:         facedamon
* @CreateDate:     2018/8/6 13:48
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/6 13:48
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public long getLong(String name){
        return Long.valueOf((long)paramMap.get(name));
    }
}
