package com.facedamon.orm.generator.core;

import lombok.Data;

/**
* @Description:    Object of filed for get and set method
* @Author:         facedamon
* @CreateDate:     2018/7/29 17:43
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/29 17:43
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
public class FieldGetAndSet {
    private String filed;
    private String filedGet;
    private String filedSet;
}
