package com.facedamon.orm.process;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
* @Description:    核心结果集处理器
* @Author:         facedamon
* @CreateDate:     2018/7/12 15:58
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/12 15:58
* @UpdateRemark:
* @Version:        1.0
*/
public interface RowsProcessor {

    /**
     * 数组转换
     * @param rs
     * @return
     * @throws SQLException
     */
    Object[] toArray(ResultSet rs) throws SQLException;

    /**
     * 填充实体 one2one
     * @param rs
     * @param type
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> T toBean(ResultSet rs, Class<T> type) throws SQLException;

    /**
     * 填充实体 one2many
     * @param rs
     * @param type
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException;

    /**
     * map转换
     * @param rs
     * @return
     * @throws SQLException
     */
    Map<String,Object> toMap(ResultSet rs) throws SQLException;
}
