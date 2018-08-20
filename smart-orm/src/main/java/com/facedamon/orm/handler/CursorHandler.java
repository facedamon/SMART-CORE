package com.facedamon.orm.handler;

import com.facedamon.orm.core.ResultSetHandler;
import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @Description:    指定名称或者游标 convert ResultSet to Object
* @Author:         facedamon
* @CreateDate:     2018/7/25 16:19
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 16:19
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class CursorHandler<T> implements ResultSetHandler<T> {

    private final int columnIndex;

    private final String columnName;

    public CursorHandler(int columnIndex,String columnName){
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }

    public CursorHandler(int columnIndex){
        this(columnIndex,null);
    }

    public CursorHandler(String columnName){
        this(1,columnName);
    }

    public CursorHandler(){
        this(1,null);
    }

    /**
     *
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    @Override
    public T handler(ResultSet rs) throws SQLException {
       while(rs.next()){
           if (StringUtils.isNotBlank(this.columnName)){
               return (T) rs.getObject(this.columnName);
           }
           return (T) rs.getObject(this.columnIndex);
       }
       return null;
    }
}
