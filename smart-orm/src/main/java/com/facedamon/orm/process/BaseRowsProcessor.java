package com.facedamon.orm.process;

import org.apache.commons.lang.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
* @Description:    基础结果集核心处理器
* @Author:         facedamon
* @CreateDate:     2018/7/19 10:06
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/19 10:06
* @UpdateRemark:
* @Version:        1.0
*/
public class BaseRowsProcessor implements RowsProcessor{

    /**
     * Singleton Object
     */
    private static BaseRowsProcessor ourInstance = new BaseRowsProcessor();

    public static BaseRowsProcessor getInstance() {
        return ourInstance;
    }

    /**
     * 默认的核心处理器
     * 如果构造器中没有处理器，则默认采用该处理器
     */
    private static final CoreProcessor defaultConvert = new CoreProcessor();

    private final CoreProcessor convert;

    /**
     * default noArgs Constructor
     */
    public BaseRowsProcessor(){
        this(defaultConvert);
    }

    /**
     * default coreprocessor Constructor
     * @param convert
     */
    public BaseRowsProcessor(CoreProcessor convert){
        super();
        this.convert = convert;
    }

    public Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        Object[] objects = new Object[count];

        for (int i = 0; i < objects.length; i++){
            /**
             * rs 游标从1开始
             */
            objects[i] = rs.getObject(i+1);
        }

        return objects;
    }

    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
       return this.convert.toBean(rs,type);
    }

    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        return this.convert.toBeanList(rs,type);
    }

    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        NavigationHashMap navigationHashMap = new NavigationHashMap();
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();

        for (int col = 1; col <= count; col++){
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
            navigationHashMap.put(columnName,rs.getObject(col));
        }
        return navigationHashMap;
    }

    /**将所有key转化为小写，因为数据库不处理大小写
     * 以此来实现不区分大小写的查找
     * LinkedHashMap的插入，删除操作效率较高
    * @Description:
    * @Author:         facedamon
    * @CreateDate:     2018/7/19 10:30
    * @UpdateUser:     facedamon
    * @UpdateDate:     2018/7/19 10:30
    * @UpdateRemark:
    * @Version:        1.0
    */
    private static class NavigationHashMap extends LinkedHashMap<String,Object>{
        /**
         * key:lowercase
         * value:not lowercase
         */
        private final Map<String,String> lowerCaseHashMap = new HashMap();

        @Override
        public boolean containsKey(Object key) {
            String newKey = lowerCaseHashMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.containsKey(newKey);
        }

        @Override
        public Object get(Object key) {
            String newKey = lowerCaseHashMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.get(newKey);
        }

        @Override
        public Object put(String key, Object value) {
            String oldKey = lowerCaseHashMap.put(key.toLowerCase(Locale.ENGLISH),key);
            /**
             * 如果put成功则是null,如果重复put则返回上一个元素的值
             * 为了保持lowercasehashMap和super的同步
             * 则需要将super对应的oldkey值删除,再put
             */
            Object oldValue = super.remove(oldKey);
            super.put(key,value);
            return oldValue;
        }

        @Override
        public void putAll(Map<? extends String, ?> m) {
            for (Map.Entry<? extends String,?> entity : m.entrySet()){
                String key = entity.getKey();
                Object value = entity.getValue();
                this.put(key,value);
            }
        }

        @Override
        public Object remove(Object key) {
            Object newKey = lowerCaseHashMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
            return super.remove(newKey);
        }
    }
}
