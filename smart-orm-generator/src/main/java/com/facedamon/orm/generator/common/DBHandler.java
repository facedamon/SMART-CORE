package com.facedamon.orm.generator.common;

import com.facedamon.orm.generator.convertor.MySqlTypeConvertor;
import com.facedamon.orm.generator.convertor.OracleTypeConvertor;
import com.facedamon.orm.generator.convertor.TypeConvertor;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

/**
* @Description:
* @Author:         facedamon
* @CreateDate:     2018/8/14 10:44
* @UpdateUser:     facedamon
* @UpdateDate:     2018/8/14 10:44
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class DBHandler {
    public static TypeConvertor getTypeConvertor(DBType dbType){
        if (dbType.compareTo(DBType.MYSQL) == 0){
            return new MySqlTypeConvertor();
        }else if (dbType.compareTo(DBType.ORACLE) == 0){
            return new OracleTypeConvertor();
        }
        return null;
    }
}
