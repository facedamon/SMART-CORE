package com.facedamon.orm.common;

import com.facedamon.util.PropertiesUtil;

import java.util.Properties;

/**
* @Description:    配置文件加载器
* @Author:         facedamon
* @CreateDate:     2018/8/1 11:36
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 11:36
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public final class ConfigHolder {

    private static final Properties CONFIG_PROPS =
            PropertiesUtil.loadProperties("jdbc.properties");

    public static String getJdbcDriver(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,ORMConfigConstant.DRIVER.getValue());
    }

    public static String getJdbcUrl(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,ORMConfigConstant.URL.getValue());
    }

    public static String getUsername(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,ORMConfigConstant.USERNAME.getValue());
    }

    public static String getPwd(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,ORMConfigConstant.PWD.getValue());
    }
}
