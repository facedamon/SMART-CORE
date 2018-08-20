package com.facedamon.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
* @Description:
* @Author:         facedamon
* @CreateDate:     2018/8/10 17:09
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/10 17:09
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public final class StreamUtil {

    public static String getString(InputStream in) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";

        while ((line = reader.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }

    public static String getStringQuietly(InputStream in){
        try {
            return getString(in);
        } catch (IOException e) {
            log.error("get request inputstream failed",e);
            throw new RuntimeException(e);
        }
    }
}
