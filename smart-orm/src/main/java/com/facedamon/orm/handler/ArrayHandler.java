package com.facedamon.orm.handler;

import com.facedamon.orm.core.ResultSetHandler;
import com.facedamon.orm.process.BaseRowsProcessor;
import com.facedamon.orm.process.RowsProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @Description:    convert a resultset of first place into object[]
* @Author:         facedamon
* @CreateDate:     2018/7/20 9:55
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/20 9:55
* @UpdateRemark:
* @Version:        1.0
*/
public class ArrayHandler implements ResultSetHandler<Object> {

    /**
     * default singleton rowsprocessor
     */
    protected static final RowsProcessor ROWS_PROCESSOR = new BaseRowsProcessor();

    private static final Object[] EMPTY_ARRAY = new Object[0];

    private final RowsProcessor convert;

    public ArrayHandler(RowsProcessor convert) {
        super();
        this.convert = convert;
    }

    public ArrayHandler(){
        this(ROWS_PROCESSOR);
    }

    /**
     * 将第一行中的列值放在数组对象中
     * 如果ResultSet中没有行将返回一个空数组
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    public Object handler(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }
}
