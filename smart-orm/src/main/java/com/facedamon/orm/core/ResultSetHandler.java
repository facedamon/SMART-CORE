package com.facedamon.orm.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @Description:    结果集包装接口
* @Author:         facedamon
* @CreateDate:     2018/7/10 13:48
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/10 13:48
* @UpdateRemark:
* @Version:        1.0
*/
public interface ResultSetHandler<T> {

    /**
     * 包装器接口
     * @param rs 结果集
     * @return 期望包装映射类型(List,Bean,Map,Array)
     * @throws SQLException
     */
    T handler(ResultSet rs) throws SQLException;
}
