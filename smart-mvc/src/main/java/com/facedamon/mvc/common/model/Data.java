package com.facedamon.mvc.common.model;

/**
* @Description:    数据模型
* @Author:         facedamon
* @CreateDate:     2018/8/6 13:49
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/6 13:49
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class Data {
    private Object data;

    public Data(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }
}
