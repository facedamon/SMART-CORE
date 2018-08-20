package com.facedamon.orm.util;

import com.facedamon.orm.common.ConfigHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.sql.*;

/**
* @Description:    数据源相关操作
* @Author:         facedamon
* @CreateDate:     2018/7/30 12:37
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/30 12:37
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public class DataSourceHolder {
    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PWD;

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();

    static{
        DRIVER = ConfigHolder.getJdbcDriver();
        URL = ConfigHolder.getJdbcUrl();
        USERNAME = ConfigHolder.getUsername();
        PWD = ConfigHolder.getPwd();

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("could not load jdbc driver",e);
        }
    }

    public static void close(Connection conn) throws SQLException {
        if (null != conn){
            conn.close();
        }
    }

    /**
     * 静默关闭
     * @param conn
     */
    @Deprecated
    public static void closeQuietly(Connection conn){
        try {
            close(conn);
        } catch (SQLException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    public static void closeConnQuietly(){
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (null != conn){
            try {
                close(conn);
            } catch (SQLException e) {
                log.error(ExceptionUtils.getFullStackTrace(e));
            }finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    public static void close(ResultSet rs) throws SQLException {
        if (null != rs){
            rs.close();
        }
    }

    /**
     * 静默关闭
     * @param rs
     */
    public static void closeQuietly(ResultSet rs){
        try {
            close(rs);
        } catch (SQLException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    public static void close(Statement stmt) throws SQLException{
        if (null != stmt){
            stmt.close();
        }
    }

    /**
     * 静默关闭
     * @param stmt
     */
    public static void closeQuietly(Statement stmt){
        try {
            close(stmt);
        } catch (SQLException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 全部静默关闭
     * @param conn
     * @param stmt
     * @param rs
     */
    public static void closeQuiety(Connection conn,Statement stmt,ResultSet rs){
        try{
            closeQuietly(rs);
        }finally {
            try{
                closeQuietly(stmt);
            }finally {
                closeQuietly(conn);
            }
        }

    }

    /**
     * 提交并关闭链接
     * @param conn
     * @throws SQLException
     */
    @Deprecated
    public static void commitAndClose(Connection conn) throws SQLException{
        if (null != conn){
            try {
                conn.commit();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * 提交并关闭链接
     * @throws SQLException
     */
    public static void commitAndClose() throws SQLException{
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (null != conn){
            try{
                conn.commit();
            }finally {
                conn.close();
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * 提交并静默关闭链接
     * @param conn
     */
    public static void commitAndCloseQuietly(Connection conn){
        try {
            commitAndClose(conn);
        } catch (SQLException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    public static Connection getConnection() throws Exception{
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (null == conn){
            conn = DriverManager.getConnection(URL,USERNAME,PWD);
            CONNECTION_THREAD_LOCAL.set(conn);
        }
        return conn;
    }

    /**
     * 静默获取链接
     * @return
     */
    public static Connection getConnectionQuietly(){
        try {
            return getConnection();
        } catch (Exception e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
        return null;
    }
}
