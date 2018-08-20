package com.facedamon.mvc.common;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @Description:    请求体封装
* @Author:         facedamon
* @CreateDate:     2018/8/1 17:35
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/1 17:35
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    private String requestMethod;
    private String requestPath;
}
