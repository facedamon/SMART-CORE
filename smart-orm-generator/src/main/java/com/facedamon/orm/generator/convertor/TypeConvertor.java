package com.facedamon.orm.generator.convertor;

import java.sql.SQLException;

/**
* @Description:    类型转化器
* @Author:         facedamon
* @CreateDate:     2018/7/29 17:47
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/29 17:47
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public interface TypeConvertor {
    String convertDB2Object(String dbType);
}
