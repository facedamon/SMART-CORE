package com.facedamon.orm.handler;

import com.facedamon.orm.core.ResultSetHandler;
import com.facedamon.orm.process.RowsProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
* @Description:    convert ResultSet to BeanList
* @Author:         facedamon
* @CreateDate:     2018/7/25 13:40
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 13:40
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class BeanListHandler<T> implements ResultSetHandler<List<T>> {

    private final RowsProcessor convert;

    private final Class<T> type;

    public BeanListHandler(RowsProcessor convert,Class<T> type){
        this.convert = convert;
        this.type = type;
    }

    public BeanListHandler(Class<T> type){
        this(ArrayHandler.ROWS_PROCESSOR,type);
    }

    /**
     *
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    @Override
    public List<T> handler(ResultSet rs) throws SQLException {
        return this.convert.toBeanList(rs,this.type);
    }
}
