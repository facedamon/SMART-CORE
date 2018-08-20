package com.facedamon.service;

import com.facedamon.orm.core.Query;
import com.facedamon.orm.handler.BeanHandler;
import com.facedamon.orm.handler.BeanListHandler;
import com.facedamon.orm.handler.MapListHandler;
import com.facedamon.orm.process.Under2CamelProcessor;
import com.facedamon.orm.util.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @Description:    封装query接口，统一CRUD操作
* @Author:         facedamon
* @CreateDate:     2018/7/30 13:21
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/30 13:21
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public class SimpleQuery {
    private static final Query QUERY = new Query();

    /**
     * 查询Bean列表
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> List<T> queryForList(Class<T> entityClass,String sql,Object... params){
        List<T> entityList = null;
        Connection conn = DataSourceHolder.getConnectionQuietly();
        try {
            QUERY.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        } catch (SQLException e) {
            log.error("query entity list failure",e);
            QUERY.retMoreErrorThrowQuietly(e,sql,params);
        }finally {
            DataSourceHolder.closeConnQuietly();
        }
        return entityList;
    }

    /**
     * 查询Bean
     * @param entityClass
     * @param sql
     * @param params
     * @param <T>
     * @return
     */
    public static <T> T queryForBean(Class<T> entityClass,String sql,Object... params){
        T entity = null;
        Connection conn = DataSourceHolder.getConnectionQuietly();
        try {
            entity = QUERY.query(conn,sql,new BeanHandler<T>(entityClass),params);
        } catch (SQLException e) {
            log.error("query entity failure",e);
            QUERY.retMoreErrorThrowQuietly(e,sql,params);
        }finally {
            DataSourceHolder.closeConnQuietly();
        }
        return entity;
    }

    /**
     * 查询List<Map>
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> queryForMapList(String sql,Object params){
        List<Map<String,Object>> result = null;
        Connection conn = DataSourceHolder.getConnectionQuietly();
        try {
            result = QUERY.query(conn,sql,new MapListHandler(),params);
        } catch (SQLException e) {
            log.error("query for mapList failure",e);
            QUERY.retMoreErrorThrowQuietly(e,sql,params);
        }finally {
            DataSourceHolder.closeConnQuietly();
        }
        return result;
    }

    /**
     * 插入Bean
     * @param entityClass
     * @param filedMap
     * @param <T>
     * @return
     */
    public static <T> int insert(Class<T> entityClass,Map<String,Object> filedMap){
        if (null == filedMap || filedMap.isEmpty()){
            log.error("can not insert entity,filedMap is empty");
            return -1;
        }
        String sql = "insert into " + getTableName(entityClass);
        StringBuffer columns = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");

        for (String item : filedMap.keySet()){
            columns.append(item).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(", "),columns.length(),")");
        values.replace(values.lastIndexOf(", "),values.length(),")");

        sql += columns.append(" values ").append(values);

        try {
            return QUERY.update(sql,filedMap.values().toArray());
        } catch (SQLException e) {
            log.error("insert entity failure",e);
            QUERY.retMoreErrorThrowQuietly(e,sql,filedMap.values().toArray());
        }finally {
            DataSourceHolder.closeConnQuietly();
        }
        return -1;
    }

    /**
     * 更新Bean
     * @param entityClass
     * @param id
     * @param filedMap
     * @param <T>
     * @return
     */
    public static <T> int update(Class<T> entityClass,Integer id,Map<String,Object> filedMap){
        if (null == filedMap || filedMap.isEmpty()){
            log.error("can not update entity,filedMap is empty");
            return -1;
        }

        String sql = "update " + getTableName(entityClass) + "set ";
        StringBuffer columns = new StringBuffer();
        for (String item : filedMap.keySet()){
            columns.append(item).append("=?, ");
        }

        sql += columns.substring(0,columns.lastIndexOf(", ")) + "where id=?";
        List<Object> params = new ArrayList<Object>();
        params.add(filedMap.values().toArray());
        params.add(id);
        try {
            return QUERY.update(sql,params.toArray());
        } catch (SQLException e) {
            log.error("update entity failure",e);
            QUERY.retMoreErrorThrowQuietly(e,sql,params.toArray());
        }finally {
            DataSourceHolder.closeConnQuietly();
        }
        return -1;
    }

    public static <T> int delete(Class<T> entityClass,Integer id){
        if (id == null){
            log.error("can not delete entity,id is null");
            return -1;
        }

        String sql = "delete from " + getTableName(entityClass) + "where id=?";
        try {
            return QUERY.update(sql,id);
        } catch (SQLException e) {
            log.error("delete entity failure",e);
            QUERY.retMoreErrorThrowQuietly(e,sql,id);
        }finally {
            DataSourceHolder.closeConnQuietly();
        }
        return -1;
    }

    private static String getTableName(Class<?> entityClass){
        return Under2CamelProcessor.camel2Under(entityClass.getSimpleName());
    }
}
