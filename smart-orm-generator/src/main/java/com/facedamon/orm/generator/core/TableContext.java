package com.facedamon.orm.generator.core;

import com.facedamon.orm.generator.common.DBHandler;
import com.facedamon.orm.generator.common.DBType;
import com.facedamon.orm.generator.common.JavaFileHolder;
import com.facedamon.orm.util.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: facedamon
 * @Description: 表-类上下文
 * @Date: Credted in 下午11:35 2018/7/9
 * @Modified by:
 */
@Slf4j
public class TableContext {

    /**
     * 表名为key，表信息对象为value
     */
    private static final Map<String,TableInfo> TABLES = new HashMap<String,TableInfo>();

    static {
        Connection conn = DataSourceHolder.getConnectionQuietly();
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            /**
             * 查询schema下的所有表
             */
            ResultSet tableRes = dbmd.getTables(null,"%","%",new String[] {"TABLE"});
            while(tableRes.next()){
                String tableName = (String)tableRes.getObject("TABLE_NAME");
                TableInfo tableInfo = TableInfo.builder().name(tableName)
                        .columnInfoMap(new HashMap<String,ColumnInfo>())
                        .primaryKeys(new ArrayList<ColumnInfo>())
                        .build();
                TABLES.put(tableName,tableInfo);
                /**
                 * 查询表中的所有字段
                 */
                ResultSet columnRes = dbmd.getColumns(null,"%",tableName,"%");
                while(columnRes.next()){
                    ColumnInfo columnInfo = ColumnInfo.builder().name(columnRes.getString("COLUMN_NAME"))
                            .type(columnRes.getString("TYPE_NAME"))
                            .keyType(0)
                            .build();
                    tableInfo.getColumnInfoMap().put(columnRes.getString("COLUMN_NAME"),columnInfo);
                }
                /**
                 * 查询主键
                 */
                ResultSet keyRes = dbmd.getPrimaryKeys(null, "%", tableName);
                while (keyRes.next()){
                    ColumnInfo columnInfo = tableInfo.getColumnInfoMap().get(keyRes.getObject("COLUMN_NAME"));
                    /**
                     * 设置为主键类型
                     */
                    columnInfo.setKeyType(1);
                    tableInfo.getPrimaryKeys().add(columnInfo);
                }
                /**
                 * 取唯一主键,方便使用。如果是联合主键。则为空！
                 */
                if (tableInfo.getPrimaryKeys().size() > 0){
                    tableInfo.setPrimaryKey(tableInfo.getPrimaryKeys().get(0));
                }
            }
        } catch (SQLException e) {

        }
    }

    /**
     * 外部加载入口
     */
    public static void init(DBType dbType){
        try {
            for (TableInfo tableInfo : TABLES.values()) {
                JavaFileHolder.createBeanFile(tableInfo, DBHandler.getTypeConvertor(dbType));
            }
        }catch(IOException e){

        }
    }
}
