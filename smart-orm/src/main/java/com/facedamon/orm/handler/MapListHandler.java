package com.facedamon.orm.handler;

import com.facedamon.orm.process.RowsProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
* @Description:    convert ResultSet to MapList
* @Author:         facedamon
* @CreateDate:     2018/7/25 13:52
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 13:52
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class MapListHandler extends AbstractListHandler<Map<String,Object>> {

    private final RowsProcessor convert;

    public MapListHandler(RowsProcessor convert){
        super();
        this.convert = convert;
    }

    public MapListHandler(){
        this(ArrayHandler.ROWS_PROCESSOR);
    }

    @Override
    protected Map<String, Object> handlerRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }
}
