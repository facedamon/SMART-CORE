package com.facedamon.orm.generator.core;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
* @Description:    table info
* @Author:         facedamon
* @CreateDate:     2018/7/29 17:35
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/29 17:35
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
@Builder
public class TableInfo {
    /**
     * 表名
     */
    private String name;

    /**
     * 字段
     */
    private Map<String,ColumnInfo> columnInfoMap;

    /**
     * 主键
     */
    private ColumnInfo primaryKey;

    /**
     * 联合主键
     */
    private List<ColumnInfo> primaryKeys;
}
