package com.facedamon.mvc.common;

import com.facedamon.util.PropertiesUtil;

import java.util.Properties;

/**
* @Description:    配置文件加载器
* @Author:         facedamon
* @CreateDate:     2018/8/1 11:38
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 11:38
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public final class ConfigHolder {

    private static final Properties CONFIG_PROPS =
            PropertiesUtil.loadProperties("smart.properties");

    public static String getBasePackage(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,MVCConfigConstant.BASE_PACKAGE.getValue());
    }

    public static String getJspPath(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,MVCConfigConstant.JSP_PATH.getValue(),"/WEB-INF/view/");
    }

    public static String getAssetPath(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,MVCConfigConstant.ASSET_PATH.getValue(),"/asset/");
    }
}
