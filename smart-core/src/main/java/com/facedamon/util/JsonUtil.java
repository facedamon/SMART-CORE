package com.facedamon.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
* @Description:    java类作用描述
* @Author:         facedamon
* @CreateDate:     2018/9/14 9:59
* @UpdateUser:     facedamon
* @UpdateDate:     2018/9/14 9:59
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String toJson(T obj){
        String json;
        try {
            json = OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("convert bean to json failure",e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T fromJson(String json,Class<T> type){
        T pojo;
        try {
            pojo = OBJECT_MAPPER.readValue(json,type);
        } catch (IOException e) {
            log.error("convert json to bean failure",e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}
