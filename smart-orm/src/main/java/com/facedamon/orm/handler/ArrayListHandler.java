package com.facedamon.orm.handler;

import com.facedamon.orm.process.RowsProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @Description:    convert ResultSet into a list of Object[]
* @Author:         facedamon
* @CreateDate:     2018/7/25 13:23
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 13:23
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class ArrayListHandler extends AbstractListHandler<Object[]>{

    private final RowsProcessor convert;

    public ArrayListHandler(RowsProcessor convert){
        super();
        this.convert = convert;
    }

    public ArrayListHandler(){
        this(ArrayHandler.ROWS_PROCESSOR);
    }

    /**
     * convert ResultSet into Object[]
     * and handler add Object[] into list
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    protected Object[] handlerRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }
}
