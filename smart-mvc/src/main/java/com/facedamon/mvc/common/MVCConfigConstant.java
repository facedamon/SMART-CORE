package com.facedamon.mvc.common;

/**
 * @Description:    MVC配置文件描述类
 * @Author:         facedamon
 * @CreateDate:     2018/8/1 11:01
 * @UpdateUser:     facedamon
 * @UpdateDate:     2018/8/1 11:01
 * @UpdateRemark:   修改内容
 * @Version:        1.0
 */
public enum  MVCConfigConstant {

    BASE_PACKAGE("base_package"),
    JSP_PATH("jsp_path"),
    ASSET_PATH("asset_path");

    private String value;

    public String getValue() {
        return this.value;
    }

    MVCConfigConstant(String value) {
        this.value = value;
    }
}
