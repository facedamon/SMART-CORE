package com.facedamon.orm.handler;

import com.facedamon.orm.core.ResultSetHandler;
import com.facedamon.orm.process.RowsProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
* @Description:    convert ResultSet to Map
* @Author:         facedamon
* @CreateDate:     2018/7/25 13:49
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 13:49
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MapHandler implements ResultSetHandler<Map<String,Object>> {

    private final RowsProcessor convert;

    public MapHandler(RowsProcessor convert){
        super();
        this.convert = convert;
    }

    public MapHandler(){
        this(ArrayHandler.ROWS_PROCESSOR);
    }

    /**
     * convert the first row to Map
     * @param rs 结果集
     * @return
     * @throws SQLException
     */
    @Override
    public Map<String, Object> handler(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }
}
