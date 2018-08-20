package com.facedamon.orm.core;

import org.apache.commons.lang.StringUtils;

import javax.sql.DataSource;
import java.sql.*;

/**
* @Description:    Query
* @Author:         facedamon
* @CreateDate:     2018/7/10 13:54
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/10 13:54
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class Query extends AbstractQuery {

    public Query(){super();}

    public Query(boolean isSupportMetaData){super(isSupportMetaData);}

    public Query(DataSource ds){super(ds);}

    public Query(boolean isSupportMetaData,DataSource ds){super(ds,isSupportMetaData);}

    /**
     * 内部查询
     * @param conn      链接对象
     * @param closeConn 是否关闭链接
     * @param sql       sql语句
     * @param rst       结果集包装器
     * @param params    sql参数
     * @param <T>       返回数据类型
     * @return
     */
    private <T> T query(Connection conn,boolean closeConn,String sql,ResultSetHandler<T> rst,Object...params) throws SQLException {
        if (null == conn){
            throw new SQLException("Connection is NULL");
        }
        if (StringUtils.isBlank(sql)){
            if (closeConn){
                close(conn);
            }
            throw  new SQLException("Sql Statement is NULL");
        }
        if (null == rst){
            if (closeConn){
                close(conn);
            }
            throw new SQLException("NONE ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T result = null;

        try {
            stmt = preparedStatement(conn, sql);
            fillPreparedStatement(stmt, params);

            rs = wrap(stmt.executeQuery());
            result = rst.handler(rs);
        }catch (SQLException e){
            retMoreErrorThrow(e,sql,params);
        }finally {
            try{
                close(rs);
            }finally {
                close(stmt);
                if (closeConn){
                    close(conn);
                }
            }

        }

        return result;
    }

    /**
     * 内部批量更新
     * @param conn      链接对象
     * @param closeConn 是否关闭链接
     * @param sql       sql语句
     * @param params    sql参数,每一行代表更新的一条参数列表,所以采用二维数组模拟批量操作
     * @return int[]    返回受影响的每一条语句的行数
     * @throws SQLException
     */
    private int[] batch(Connection conn,boolean closeConn,String sql,Object[][] params) throws SQLException{
        if (null == conn){
            throw new SQLException("Connection is NULL");
        }
        if (StringUtils.isBlank(sql)){
            if (closeConn){
                close(conn);
            }
            throw  new SQLException("Sql Statement is NULL");
        }
        if (null == params){
            if (closeConn){
                close(conn);
            }
            throw new SQLException("Params is null, if the params are`t need,please full a empty params");
        }

        PreparedStatement stmt = null;
        int[] rows = null;

        try {
            stmt = preparedStatement(conn, sql);
            for (int i = 0; i < params.length; i++){
                fillPreparedStatement(stmt,params[i]);
                stmt.addBatch();
            }
            rows = stmt.executeBatch();
        }catch (SQLException e){
            retMoreErrorThrow(e,sql,(Object[]) params);
        }finally {
            try{
                close(stmt);
            }finally {
                if (closeConn){
                    close(conn);
                }
            }
        }
        return rows;
    }

    /**
     * 内部更新
     * @param conn      链接对象
     * @param closeConn 是否关闭链接
     * @param sql       sql语句
     * @param params    sql参数
     * @return int      返回受影响的行
     * @throws SQLException
     */
    private int update(Connection conn,boolean closeConn,String sql,Object... params) throws SQLException{
        if (null == conn){
            throw new SQLException("Connection is NULL");
        }
        if (StringUtils.isBlank(sql)){
            if (closeConn){
                close(conn);
            }
            throw  new SQLException("Sql Statement is NULL");
        }

        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = preparedStatement(conn,sql);
            fillPreparedStatement(stmt,params);
            rows = stmt.executeUpdate();
        }catch (SQLException e){
            retMoreErrorThrow(e,sql,params);
        }finally {
            try{
                close(stmt);
            }finally {
                if (closeConn){
                    close(conn);
                }
            }
        }
        return rows;
    }

    /**
     * 内部新增
     * @param conn         链接对象
     * @param closeConn    是否关闭链接
     * @param sql          sql语句
     * @param rst          结果集包装器
     * @param params       sql参数
     * @param <T>          返回数据类型
     * @return
     * @throws SQLException
     */
    private <T> T insert(Connection conn,boolean closeConn,String sql,ResultSetHandler<T> rst,Object...params) throws SQLException{
        if (null == conn){
            throw new SQLException("Connection is NULL");
        }
        if (StringUtils.isBlank(sql)){
            if (closeConn){
                close(conn);
            }
            throw  new SQLException("Sql Statement is NULL");
        }
        if (null == rst){
            if (closeConn){
                close(conn);
            }
            throw new SQLException("NONE ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        T autoGeneratorKeys = null;

        try {
            stmt = preparedStatement(conn,sql,Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(stmt,params);
            stmt.executeUpdate();
            resultSet =  wrap(stmt.getGeneratedKeys());
            autoGeneratorKeys = rst.handler(resultSet);
        }catch(SQLException e){
            retMoreErrorThrow(e,sql,params);
        }finally {
            try{
                close(stmt);
            }finally {
                if (closeConn){
                    close(conn);
                }
            }
        }
        return autoGeneratorKeys;
    }

    /**
     * 内部批量新增
     * @param conn          链接对象
     * @param closeConn     是否关闭链接
     * @param sql           sql语句
     * @param rst           结果集包装器
     * @param params        sql参数
     * @param <T>           返回数据类型
     * @return
     * @throws SQLException
     */
    private <T> T batchInsert(Connection conn,boolean closeConn,String sql,ResultSetHandler<T> rst,Object[][] params) throws SQLException{
        if (null == conn){
            throw new SQLException("Connection is NULL");
        }
        if (StringUtils.isBlank(sql)){
            if (closeConn){
                close(conn);
            }
            throw  new SQLException("Sql Statement is NULL");
        }
        if (null == rst){
            if (closeConn){
                close(conn);
            }
            throw new SQLException("NONE ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        T autoGeneratorKeys = null;

        try {
            stmt = preparedStatement(conn,sql,Statement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < params.length; i++){
                fillPreparedStatement(stmt,params[i]);
                stmt.addBatch();
            }
            stmt.executeBatch();
            resultSet = wrap(stmt.getGeneratedKeys());
            autoGeneratorKeys = rst.handler(resultSet);
        }catch (SQLException e){
            retMoreErrorThrow(e,sql,(Object[]) params);
        }finally {
            try {
                close(stmt);
            }finally {
                if (closeConn){
                    close(conn);
                }
            }
        }
        return autoGeneratorKeys;
    }
    /**
     * A Simple batch for update,insert,delete by Connection
     * not close the connection Quietly
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int[] batch(Connection conn,String sql,Object[][] params) throws SQLException{
        return this.batch(conn,false,sql,params);
    }

    /**
     * A simple batch for update,insert,delete by the default Datasource
     * close the connection Quietly
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int[] batch(String sql,Object[][] params) throws  SQLException{
        return this.batch(this.getConnection(),true,sql,params);
    }

    /**
     * A simple query interface by the Connection
     * not close the connection Quietly
     * the params is a Object[]
     * @param conn
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(Connection conn,String sql,Object[] params,ResultSetHandler<T> rst) throws SQLException{
        return this.query(conn,false,sql,rst,params);
    }

    /**
     * A simple query interface by the Connection
     * not close the connection Quietly
     * the params is a Object
     * @param conn
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(Connection conn,String sql,ResultSetHandler<T> rst,Object params) throws SQLException{
        return this.query(conn,false,sql,rst, params);
    }

    /**
     * A simple query interface by the Connection
     * not close the connection Quietly
     * the params is a Object...
     * @param conn
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(Connection conn,String sql,ResultSetHandler<T> rst,Object... params) throws SQLException{
        return this.query(conn,false,sql,rst,params);
    }

    /**
     * A simple query interface by the Connection
     * not close the connection Quietly
     * there is no params
     * @param conn
     * @param sql
     * @param rst
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(Connection conn,String sql,ResultSetHandler<T> rst) throws SQLException{
        return this.query(conn,false,sql,rst,(Object[]) null);
    }

    /**
     * A simple query interface by the default Datasource
     * close the connection quietly
     * the params is a Object
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(String sql,ResultSetHandler<T> rst,Object params) throws SQLException{
        return this.query(this.getConnection(),true,sql,rst, params);
    }

    /**
     * A simple query interface by the default Datasource
     * close the connection quietly
     * the params is a Object[]
     * @param sql
     * @param params
     * @param rst
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(String sql,Object[] params,ResultSetHandler<T> rst) throws SQLException{
        return this.query(this.getConnection(),true,sql,rst,params);
    }

    /**
     * A simple query interface by the default Datasource
     * close the connection quietly
     * the params is a Object...
     * @param sql
     * @param params
     * @param rst
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(String sql,ResultSetHandler<T> rst,Object... params) throws SQLException{
        return this.query(this.getConnection(),true,sql,rst,params);
    }

    /**
     * A simple query interface by the default Datasource
     * close the connection quietly
     * there is no params
     * @param sql
     * @param rst
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T query(String sql,ResultSetHandler<T> rst) throws SQLException{
        return this.query(this.getConnection(),true,sql,rst,(Object[]) null);
    }

    /**
     * A simple update interface by the Connection
     * not close the connection quietly
     * the params is a Object
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int update(Connection conn,String sql,Object params) throws SQLException{
        return this.update(conn,false,sql, params);
    }

    /**
     * A simple update interface by the Connection
     * not close the connection quietly
     * the params is a Object[]
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int update(Connection conn,Object[] params,String sql) throws SQLException{
        return this.update(conn,false,sql,params);
    }

    /**
     * A simple update interface by the Connection
     * not close the connection quietly
     * the params is a Object...
     * @param conn
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int update(Connection conn,String sql,Object... params) throws SQLException{
        return this.update(conn,false,sql,params);
    }

    /**
     * A simple update interface by the default DataSource
     * close the connection qiuetly
     * the params is a Object
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int update(String sql,Object params) throws SQLException{
        return this.update(this.getConnection(),true,sql, params);
    }

    /**
     * A simple update interface by the default DataSource
     * close the connection qiuetly
     * the params is a Object...
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public int update(String sql,Object... params) throws SQLException{
        return this.update(this.getConnection(),true,sql,params);
    }

    /**
     * A simple update interface by the default DataSource
     * close the connection qiuetly
     * there is no params
     * @param sql
     * @return
     * @throws SQLException
     */
    public int update(String sql) throws SQLException{
        return this.update(this.getConnection(),true,sql,(Object[]) null);
    }

    /**
     * A simple insert interface by the Connection
     * not close the connection quietly
     * there is no params
     * @param conn
     * @param sql
     * @param rst
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T insert(Connection conn,String sql,ResultSetHandler<T> rst) throws SQLException{
        return this.insert(conn,false,sql,rst,(Object[])null);
    }

    /**
     * A simple insert interface by the Connection
     * not close the connection quietly
     * the params is a Object...
     * @param conn
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T insert(Connection conn,String sql,ResultSetHandler<T> rst,Object... params) throws SQLException{
        return this.insert(conn,false,sql,rst,params);
    }

    /**
     * A simple insert interface by the default DatsSource
     * close the connection quietly
     * the params is a Object...
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T insert(String sql,ResultSetHandler<T> rst,Object... params) throws SQLException{
        return this.insert(this.getConnection(),true,sql,rst,params);
    }

    /**
     * A simple insert interface by the default DatsSource
     * close the connection quietly
     * there is no params
     * @param sql
     * @param rst
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T insert(String sql,ResultSetHandler<T> rst) throws SQLException{
        return this.insert(this.getConnection(),true,sql,rst,(Object[]) null);
    }

    /**
     * A simple batch insert interface by the Connection
     * not close the connection quietly
     * the params is a Object[][]
     * @param conn
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T batchInsert(Connection conn,String sql,ResultSetHandler<T> rst,Object[][] params) throws SQLException{
        return this.batchInsert(conn,false,sql,rst,params);
    }

    /**
     * A simple batch insert interface by default DataSource
     * close the connection quietly
     * the params is a Object[][]
     * @param sql
     * @param rst
     * @param params
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T batchInsert(String sql,ResultSetHandler<T> rst,Object[][] params) throws SQLException{
        return this.batchInsert(this.getConnection(),true,sql,rst,params);
    }
}
