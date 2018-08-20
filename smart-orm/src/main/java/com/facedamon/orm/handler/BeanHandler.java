package com.facedamon.orm.handler;

import com.facedamon.orm.core.ResultSetHandler;
import com.facedamon.orm.process.RowsProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @Description:    convert ResultSet to Bean
* @Author:         facedamon
* @CreateDate:     2018/7/25 13:36
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 13:36
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class BeanHandler<T> implements ResultSetHandler<T> {

    private final RowsProcessor convert;

    private final Class<T> type;

    public BeanHandler(Class<T> type){
        this(type,ArrayHandler.ROWS_PROCESSOR);
    }

    public BeanHandler(Class<T> type,RowsProcessor convert){
        this.type = type;
        this.convert = convert;
    }

    /**
     * convert the first row to Bean
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    @Override
    public T handler(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toBean(rs,this.type) : null;
    }
}
