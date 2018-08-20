package com.facedamon.mvc.common.model;

import java.util.HashMap;
import java.util.Map;

/**
* @Description:    view视图
* @Author:         facedamon
* @CreateDate:     2018/8/6 13:51
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/6 13:51
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class View {
    private String path;
    private Map<String,Object> model;

    public View(String path) {
        this.path = path;
        model = new HashMap<>();
    }

    public View addModel(String key,String value){
        model.put(key,value);
        return this;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
