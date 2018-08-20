package com.facedamon.orm.generator.core;

import lombok.Builder;
import lombok.Data;

/**
* @Description:    column info
* @Author:         facedamon
* @CreateDate:     2018/7/29 17:40
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/29 17:40
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
@Builder
public class ColumnInfo {
    /**
     * 字段名
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 主键类型
     */
    private int keyType;
}
