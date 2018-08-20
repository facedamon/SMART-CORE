package com.facedamon.mvc.common;

import com.facedamon.util.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
* @Description:
* @Author:         facedamon
* @CreateDate:     2018/8/10 16:42
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/10 16:42
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public final class HttpUtil {

    public static Map<String,Object> requestParams2Map(HttpServletRequest request){
        Map<String,Object> params = new HashMap<>();
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            String value = request.getParameter(key);
            params.put(key,value);
        }
        try {
            String  body = URLDecoder.decode(StreamUtil.getStringQuietly(request.getInputStream()),"UTF-8");
            if (StringUtils.isNotBlank(body)){
                String[] paramList = StringUtils.split(body,"&");
                if (null != paramList && paramList.length > 0){
                    for (String param : paramList){
                        String[] array = StringUtils.split(param,"=");
                        if (null != array && array.length == 2){
                            String key = array[0];
                            String value = array[1];
                            params.put(key,value);
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("get request inputStream failed",e);
            throw new RuntimeException(e);
        }
        return params;
    }
}
