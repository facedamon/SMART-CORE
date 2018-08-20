package com.facedamon.orm.process;

import org.apache.commons.lang.StringUtils;

import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;

/**
* @Description:    下划线转驼峰核心处理器
* @Author:         facedamon
* @CreateDate:     2018/7/19 14:19
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/19 14:19
* @UpdateRemark:
* @Version:        1.0
*/
public class Under2CamelProcessor extends CoreProcessor {

    /**
     * 下划线转驼峰
     * @param value
     * @return
     */
    public static String under2Camel(String value){
        if (StringUtils.isBlank(value)){
            return StringUtils.EMPTY;
        }
        StringBuffer sb = new StringBuffer();
        value = value.toLowerCase(Locale.ENGLISH);
        String[] str = value.split("_");
        for (String s : str){
            sb.append(s.substring(0,1).toUpperCase(Locale.ENGLISH));
            sb.append(s.substring(1));
        }
        return sb.toString();
    }

    public static String camel2Under(String value){
        if (StringUtils.isBlank(value)){
            return StringUtils.EMPTY;
        }
        StringBuffer sb = new StringBuffer(value);
        int temp = 0;
        for (int i = 0; i < value.length(); i++){
            if (Character.isUpperCase(value.charAt(i))){
                sb.insert(i + temp,"_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    protected int[] columnToPropertySeq(ResultSetMetaData metaData, PropertyDescriptor[] props) throws SQLException {
        if (null == metaData || null == props){
            throw new SQLException("the metaData or PropertyDescriptor is null");
        }
        int cols = metaData.getColumnCount();
        int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty,PROPERTY_NOT_FOUND);

        /**
         * 因为数据库的索引是从1开始的
         */
        for (int col = 1; col <= cols; col ++){
            /**
             * 别名
             */
            String columnName = metaData.getColumnLabel(col);
            if (StringUtils.isBlank(columnName)){
                /**
                 * 原始名称
                 */
                columnName = metaData.getColumnName(col);
            }
            //TODO
            String propertyName = under2Camel(columnName);
            if (StringUtils.isBlank(propertyName)){
                propertyName = columnName;
            }
            for (int pop = 0; pop < props.length; pop++){
                if (StringUtils.equalsIgnoreCase(propertyName,props[pop].getName())
                        || StringUtils.equalsIgnoreCase(columnName,props[pop].getName())){
                    columnToProperty[col]  = pop;
                    break;
                }
            }
        }
        return columnToProperty;
    }
}
