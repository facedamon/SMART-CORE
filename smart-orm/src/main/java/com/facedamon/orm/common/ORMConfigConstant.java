package com.facedamon.orm.common;

/**
* @Description:    ORM 配置描述类
* @Author:         facedamon
* @CreateDate:     2018/8/1 11:06
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 11:06
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public enum ORMConfigConstant {

    DRIVER("jdbc.driver"),
    URL("jdbc.url"),
    USERNAME("jdbc.username"),
    PWD("jdbc.pwd"),;

    private String value;

    public String getValue() {
        return this.value;
    }

    ORMConfigConstant(String value) {
        this.value = value;
    }
}
