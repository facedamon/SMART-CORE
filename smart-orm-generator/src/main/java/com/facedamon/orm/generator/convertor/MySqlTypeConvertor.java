package com.facedamon.orm.generator.convertor;


/**
* @Description:    MySql 类型转换器
* @Author:         facedamon
* @CreateDate:     2018/7/29 17:47
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/29 17:47
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MySqlTypeConvertor implements TypeConvertor{

    public String convertDB2Object(String dbType) {
        if("varchar".equalsIgnoreCase(dbType)||"char".equalsIgnoreCase(dbType)){
            return "String";
        }else if("int".equalsIgnoreCase(dbType)
                ||"tinyint".equalsIgnoreCase(dbType)
                ||"smallint".equalsIgnoreCase(dbType)
                ||"integer".equalsIgnoreCase(dbType)
                ){
            return "Integer";
        }else if("bigint".equalsIgnoreCase(dbType)){
            return "Long";
        }else if("double".equalsIgnoreCase(dbType)||"float".equalsIgnoreCase(dbType)){
            return "Double";
        }else if("clob".equalsIgnoreCase(dbType)){
            return "java.sql.CLob";
        }else if("blob".equalsIgnoreCase(dbType)){
            return "java.sql.BLob";
        }else if("date".equalsIgnoreCase(dbType)){
            return "java.sql.Date";
        }else if("time".equalsIgnoreCase(dbType)){
            return "java.sql.Time";
        }else if("timestamp".equalsIgnoreCase(dbType)){
            return "java.sql.Timestamp";
        }

        return null;
    }
}
