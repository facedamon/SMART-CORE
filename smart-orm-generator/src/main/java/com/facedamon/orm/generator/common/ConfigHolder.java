package com.facedamon.orm.generator.common;

import com.facedamon.util.PropertiesUtil;

import java.util.Properties;

public class ConfigHolder {
    private static final Properties CONFIG_PROPS =
            PropertiesUtil.loadProperties("jdbc.properties");

    public static String getServer(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,GeneratorConstant.SERVER.getValue());
    }

    public static String getSrcPath(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,GeneratorConstant.SRC_PATH.getValue());
    }

    public static String getBeanPackage(){
        return PropertiesUtil.getProperty(CONFIG_PROPS,GeneratorConstant.BEAN_PACKAGE.getValue());
    }
}
