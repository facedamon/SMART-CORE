package com.facedamon.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
* @Description:    Properties file util
* @Author:         facedamon
* @CreateDate:     2018/7/26 16:41
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/26 16:41
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class PropertiesUtil {

    public static String getProperty(Properties properties,String key){
        String value = getValue(properties,key);
        if (StringUtils.isBlank(value)){
            throw new NoSuchElementException("there is no property about: "+ key);
        }
        return value.trim();
    }

    public static String getProperty(Properties properties,String key,String defaultValue){
        String value = getValue(properties,key);
        return StringUtils.isBlank(value) ? defaultValue : value.trim();
    }

    public static Integer getInteger(Properties properties,String key){
        String value = getValue(properties,key);
        if (StringUtils.isBlank(value)){
            throw new NoSuchElementException("there is no property about: "+ key);
        }
        return Integer.valueOf(value);
    }

    public static Integer getInteger(Properties properties,String key,Integer defaultValue){
        String value = getValue(properties,key);
        return StringUtils.isBlank(value) ? defaultValue : Integer.valueOf(value);
    }

    public static Double getDouble(Properties properties,String key){
        String value = getValue(properties,key);
        if (StringUtils.isBlank(value)){
            throw new NoSuchElementException("there is no property about: "+ key);
        }
        return Double.valueOf(value);
    }

    public static Double getDouble(Properties properties,String key,Double defaultValue){
        String value = getValue(properties,key);
        return StringUtils.isBlank(value) ? defaultValue : Double.valueOf(value);
    }

    public static Boolean getBoolean(Properties properties,String key){
        String value = getValue(properties,key);
        if (StringUtils.isBlank(value)){
            throw new NoSuchElementException("there is no property about: "+ key);
        }
       return Boolean.valueOf(value);
    }

    public static Boolean getBoolean(Properties properties,String key,Boolean defaultValue){
        String value = getValue(properties,key);
        return StringUtils.isBlank(value) ? defaultValue : Boolean.valueOf(value);
    }

    private static String getValue(Properties properties,String key){
        String systemValue = System.getProperty(key);
        if (StringUtils.isNotBlank(systemValue)){
            return systemValue.trim();
        }
        if (properties.containsKey(key)){
            return properties.getProperty(key);
        }
        return StringUtils.EMPTY;
    }

    public static Properties loadProperties(String... propertyName){
        Properties properties = new Properties();
        for (String item : propertyName){
            InputStreamReader reader = null;
            InputStream inputStream = null;

            try {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(item);
                reader = new InputStreamReader(inputStream);
                properties.load(reader);
            } catch (IOException e) {
                IOUtils.closeQuietly(inputStream);
                IOUtils.closeQuietly(reader);
            }
        }
        return properties;
    }
}
