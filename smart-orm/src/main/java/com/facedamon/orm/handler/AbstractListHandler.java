package com.facedamon.orm.handler;

import com.facedamon.orm.core.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* @Description:    抽象List转化器,不包括自动转化Bean,需要传入T的Java对象类型
* @Author:         facedamon
* @CreateDate:     2018/7/25 13:07
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/25 13:07
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {

    @Override
    public List<T> handler(ResultSet rs) throws SQLException {
        List<T> list = new ArrayList<T>();
        while (rs.next()){
            list.add(this.handlerRow(rs));
        }
        return list;
    }

    /**
     * 向下转型
     * @param rs
     * @return
     * @throws SQLException
     */
    protected abstract T handlerRow(ResultSet rs) throws SQLException;
}
