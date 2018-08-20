package com.facedamon.orm.core;

import com.facedamon.orm.util.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Arrays;

/**
* @Description:    顶层Query接口
* @Author:         facedamon
* @CreateDate:     2018/7/10 10:41
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/10 10:41
* @UpdateRemark:
* @Version:        1.0
*/
@Slf4j
public abstract class AbstractQuery {

    /**
     * 是否支持ParameterMetaData#getParameterType(int)方法?
     * Oracle数据库目前不支持该方法
     */
    private volatile boolean isSupportMetaData = false;

    /**
     * 数据源
     */
    private DataSource dataSource;

    public AbstractQuery(){this.dataSource = null;}

    public AbstractQuery(boolean isSupportMetaData){this.isSupportMetaData = isSupportMetaData;this.dataSource = null;}

    public AbstractQuery(DataSource dataSource){this.dataSource = dataSource;}

    public AbstractQuery(DataSource dataSource,boolean isSupportMetaData){this.dataSource = dataSource;this.isSupportMetaData = isSupportMetaData;}

    public boolean isSupportMetaData() {
        return isSupportMetaData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 保证只有子类可以访问
     * @param conn 链接对象
     * @param sql sql语句
     * @return
     * @throws SQLException
     */
    protected PreparedStatement preparedStatement(Connection conn,String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    /**
     *
     * @param conn 链接对象
     * @param sql  sql语句
     * @param autoGeneratorKeys 返回主键
     * @return
     * @throws SQLException
     */
    protected PreparedStatement preparedStatement(Connection conn,String sql,int autoGeneratorKeys) throws SQLException {
        return conn.prepareStatement(sql,autoGeneratorKeys);
    }

    /**
     * 获取connection
     * @return
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException {
        if (this.dataSource == null){
            throw new SQLException("The Datasource for Query Interface is NULL");
        }
        return this.dataSource.getConnection();
    }

    /**
     * 填充参数
     * @param stmt 预执行sql
     * @param params 参数数组
     * @throws SQLException
     */
    public void fillPreparedStatement(PreparedStatement stmt,Object... params) throws SQLException {
        ParameterMetaData parameterMetaData = null;

        if (!isSupportMetaData){
            parameterMetaData = stmt.getParameterMetaData();
            int stmtCount = parameterMetaData.getParameterCount();
            int paramCount = params == null ? 0 : params.length;

            if (stmtCount != paramCount){
                throw new SQLException("the preparedstatement param count " + stmtCount + " is not equals to param count "+ paramCount);
            }
        }

        if (params == null){
            return;
        }

        for (int i = 0; i < params.length; i++){
            if (params[i] != null) {
                stmt.setObject(i + 1,params[i]);
            }else{
                int sqlType = Types.VARCHAR;
                if (!isSupportMetaData){
                    try{
                        sqlType = parameterMetaData.getParameterType(i + 1);
                    }catch(SQLException e){
                        isSupportMetaData = true;
                    }
                }
                stmt.setNull(i + 1,sqlType);
            }
        }
    }

    /**
     *
     * @param cause 异常对象
     * @param sql sql语句
     * @param params 参数数组
     */
    public void retMoreErrorThrow(SQLException cause,String sql,Object... params) throws SQLException {
        String causeMessage = cause.getMessage();
        causeMessage = StringUtils.trimToEmpty(causeMessage);

        StringBuffer sb = new StringBuffer(causeMessage)
                .append("Query :")
                .append(sql)
                .append("Params : [");
        if (params == null){
            sb.append("]");
        }else{
            sb.append(Arrays.deepToString(params));
        }

        SQLException e = new SQLException(sb.toString(),cause.getSQLState(),cause.getErrorCode());

        throw  e;
    }

    /**
     * 静默异常打印
     * @param cause
     * @param sql
     * @param params
     */
    public void retMoreErrorThrowQuietly(SQLException cause,String sql,Object... params){
        try {
            retMoreErrorThrow(cause,sql,params);
        } catch (SQLException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
        }
    }

    /**
     * 采用装饰模式对结果集进行对象映射
     * @param res
     * @return
     */
    protected ResultSet wrap(ResultSet res){
        return res;
    }

    protected  void close(Connection conn) throws SQLException {
        DataSourceHolder.close(conn);
    }

    protected  void close(Statement stmt) throws SQLException {
        DataSourceHolder.close(stmt);
    }

    protected void close(ResultSet rs) throws SQLException {
        DataSourceHolder.close(rs);
    }
}
