package com.facedamon.orm.process;

import com.facedamon.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
* @Description:    将列名与bean属性名匹配,将ResultSet列转换为这些bean的对象属性.
* @Author:         facedamon
* @CreateDate:     2018/7/12 16:11
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/12 16:11
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Slf4j
public class CoreProcessor {

    /**
     * there is no bean property that matches a column from a ResultSet
     */
    protected static final int PROPERTY_NOT_FOUND = -1;

    /**
     * 当SQL为空时，将bean的基本属性设置为这些默认值
     * 它们与ResultSet get*的默认值相同
     */
    private static final Map<Class<?>,Object> primitiveDefaults = new HashMap<Class<?>, Object>();

    static{
        primitiveDefaults.put(Byte.TYPE,Byte.valueOf((byte)0));
        primitiveDefaults.put(Short.TYPE,Short.valueOf((short)0));
        primitiveDefaults.put(Integer.TYPE,Integer.valueOf(0));
        primitiveDefaults.put(Long.TYPE,Long.valueOf(0L));

        primitiveDefaults.put(Float.TYPE,Float.valueOf(0f));
        primitiveDefaults.put(Double.TYPE,Double.valueOf(0d));

        primitiveDefaults.put(Boolean.TYPE,Boolean.FALSE);
        primitiveDefaults.put(Character.TYPE,Character.valueOf((char) 0));
    }

    /**
     * column-property 映射关系
     */
    private final Map<String,String> columnPropertyMap;

    public CoreProcessor() {
        this(new HashMap<String, String>());
    }

    public CoreProcessor(Map<String,String> columnPropertyMap){
        super();
        if (null == columnPropertyMap){
            throw new IllegalArgumentException("the columnPropertyMap relation should not be null");
        }
        this.columnPropertyMap = columnPropertyMap;
    }

    /**
     * 内省获取clazz属性
     * @param clazz
     * @return  clazz属性数组
     * @throws SQLException
     */
    private PropertyDescriptor[] getPropertyDescriptor(Class<?> clazz) throws SQLException {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
           throw  new SQLException("get "+clazz.getName()+" BeanInfo Failed,"+e.getMessage());
        }
        return beanInfo.getPropertyDescriptors();
    }

    /**
     * 将结果集列与clazz属性一一关联起来
     * @param metaData  结果集元数据
     * @param props     clazz属性
     * @return          column与property的关系序列
     * @throws SQLException
     */
    protected int[] columnToPropertySeq(ResultSetMetaData metaData,PropertyDescriptor[] props) throws SQLException{
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
            String propertyName = columnPropertyMap.get(columnName);
            if (StringUtils.isBlank(propertyName)){
                propertyName = columnName;
            }
            for (int pop = 0; pop < props.length; pop++){
                if (StringUtils.equalsIgnoreCase(propertyName,props[pop].getName())){
                    columnToProperty[col]  = pop;
                    break;
                }
            }
        }
        return columnToProperty;
    }

    /**
     * 结果集转Bean
     * @param rst 结果集
     * @param clazz bean模板
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T toBean(ResultSet rst,Class<T> clazz) throws SQLException{
        ResultSetMetaData metaData = rst.getMetaData();
        PropertyDescriptor[] props = getPropertyDescriptor(clazz);

        int[] seq = columnToPropertySeq(metaData,props);

        return this.createBean(rst,clazz,props,seq);
    }

    /**
     * 结果集转列表
     * @param rst   结果集
     * @param clazz bean模板
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> List<T> toBeanList(ResultSet rst,Class<T> clazz) throws SQLException{
        List<T> beanList = new ArrayList<T>();
        if (!rst.next()){
            return beanList;
        }
        ResultSetMetaData metaData = rst.getMetaData();
        PropertyDescriptor[] props = this.getPropertyDescriptor(clazz);
        int[] seq = this.columnToPropertySeq(metaData,props);

        do {
            beanList.add(this.createBean(rst,clazz,props,seq));
        }while (rst.next());

        return beanList;
    }

    /**
     *内部转bean
     * @param rst               结果集
     * @param clazz             bean模板
     * @param props             bean属性数组
     * @param columnToProperty  结果集列属性映射bean属性关系
     * @param <T>
     * @return
     * @throws SQLException
     */
    private <T> T createBean(ResultSet rst,Class<T> clazz,PropertyDescriptor[] props,int[] columnToProperty) throws SQLException{
        //T bean = this.newInstance(clazz);
        T bean = ReflectUtil.newInstance(clazz);

        for (int i = 1; i < columnToProperty.length; i++){
            if (columnToProperty[i] == PROPERTY_NOT_FOUND){
                continue;
            }
            PropertyDescriptor prop = props[columnToProperty[i]];
            Class<?> propType = prop.getPropertyType();
            Object value = null;
            if (null != propType){
                value = this.convertColumn(rst,propType,i);
                if (null == value && propType.isPrimitive()){
                    value = primitiveDefaults.get(propType);
                }
            }
            this.callSetter(bean,prop,value);
        }
        return bean;
    }

    /**
     *反射调用bean Set方法实例化
     * @param target    目标对象
     * @param prop      目标对象属性
     * @param value
     * @throws SQLException
     */
    private void callSetter(Object target,PropertyDescriptor prop,Object value) throws SQLException{
        Method setter = prop.getWriteMethod();

        if (null == setter){
            return;
        }

        Class<?>[] params = setter.getParameterTypes();

        if (value instanceof Date){
            final String targetType = params[0].getName();
            if ("java.sql.Date".equals(targetType)){
                value = new java.sql.Date(((Date) value).getTime());
            }else if ("java.sql.Time".equals(targetType)){
                value = new java.sql.Time(((Date)value).getTime());
            }else if ("java.sql.Timestamp".equals(targetType)){
                Timestamp temp = (Timestamp)value;
                int nano = temp.getNanos();
                value = new Timestamp(temp.getTime());
                ((Timestamp)value).setNanos(nano);
            }
        }
        /**
         * 枚举字符类型
         */
        else if (value instanceof String && params[0].isEnum()){
            value = Enum.valueOf(params[0].asSubclass(Enum.class),(String) value);
        }
        try {
            if (this.isMatch(value,params[0])){
                setter.invoke(target, value);
            }else{
                throw new SQLException("Can not set "+prop.getName()+"match type,can not convert "+value.getClass().getName()+"to "+params[0].getName());
            }

        } catch (IllegalAccessException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new SQLException("Can not set:"+prop.getName()+":"+e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(ExceptionUtils.getFullStackTrace(e));
            throw new SQLException("Can not set:"+prop.getName()+":"+e.getMessage());
        }
    }

    /**
     *检测value与bean模板类型是否一致
     * @param value
     * @param type
     * @return
     */
    private boolean isMatch(Object value,Class<?> type){
        /**
         * 首先检查Object类型
         */
        if (null == value || type.isInstance(value)){
            return true;
        }else if (Byte.TYPE.equals(type) && value instanceof Byte){
            return true;
        }else if (Short.TYPE.equals(type) && value instanceof Short){
            return true;
        }else if (Integer.TYPE.equals(type) && value instanceof Integer){
            return true;
        }else if (Long.TYPE.equals(type) && value instanceof Long){
            return true;
        }else if (Float.TYPE.equals(type) && value instanceof Float){
            return true;
        }else if (Double.TYPE.equals(type) && value instanceof Double){
            return true;
        }else if (Boolean.TYPE.equals(type) && value instanceof Boolean){
            return true;
        }else return Character.TYPE.equals(type) && value instanceof Character;
    }

    /**
     * 数据库结果集列转属性值
     * @param rst
     * @param propType
     * @param i
     * @return
     * @throws SQLException
     */
    protected Object convertColumn(ResultSet rst,Class<?> propType,int i) throws SQLException{
        if (!propType.isPrimitive() && null == rst.getObject(i)){
            return null;
        }
        if (String.class.equals(propType)){
            return rst.getString(i);
        }else if (Byte.TYPE.equals(propType) || Byte.class.equals(propType)){
            return Byte.valueOf(rst.getByte(i));
        }else if (Short.TYPE.equals(propType) || Short.class.equals(propType)){
            return Short.valueOf(rst.getShort(i));
        }else if (Integer.TYPE.equals(propType) || Integer.class.equals(propType)){
            return Integer.valueOf(rst.getInt(i));
        }else if (Long.TYPE.equals(propType) || Long.class.equals(propType)){
            return Long.valueOf(rst.getLong(i));
        }else if (Float.TYPE.equals(propType) || Float.class.equals(propType)){
            return Float.valueOf(rst.getFloat(i));
        }else if (Double.TYPE.equals(propType) || Double.class.equals(propType)){
            return Double.valueOf(rst.getDouble(i));
        }else if (Timestamp.class.equals(propType)){
            return rst.getTimestamp(i);
        }else{
            return rst.getObject(i);
        }
    }

}
