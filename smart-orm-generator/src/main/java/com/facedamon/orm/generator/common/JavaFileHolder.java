package com.facedamon.orm.generator.common;

import com.facedamon.orm.generator.convertor.TypeConvertor;
import com.facedamon.orm.generator.core.ColumnInfo;
import com.facedamon.orm.generator.core.FieldGetAndSet;
import com.facedamon.orm.generator.core.TableInfo;
import com.facedamon.orm.process.Under2CamelProcessor;
import com.facedamon.orm.util.DataSourceHolder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JavaFileHolder {

    /**
     * genuine字段信息生成java属性信息 varchar username --> private String username
     * @param columnInfo 字段信息
     * @param convertor  类型转换器
     * @return
     */
    private static FieldGetAndSet createFieldGetAndSet(ColumnInfo columnInfo, TypeConvertor convertor){
        FieldGetAndSet getAndSet = new FieldGetAndSet();
        String beanFieldType = convertor.convertDB2Object(columnInfo.getType());
        getAndSet.setFiled("\tprivate "+beanFieldType+" "+under2Camel2Lower(columnInfo.getName()) +";\n");

        /**
         * public String getUsername(){return username;}
         */
        StringBuffer getField = new StringBuffer();
        getField.append("\tpublic ")
                .append(beanFieldType)
                .append(" get")
                .append(under2Camel2UpperCase(columnInfo.getName()))
                .append("(){")
                .append("return ")
                .append(columnInfo.getName())
                .append(";}\n");
        getAndSet.setFiledGet(getField.toString());

        /**
         * public void setUsername(String username){this.username = username;}
         */
        StringBuffer setField = new StringBuffer();
        setField.append("\tpublic void set")
                .append(under2Camel2UpperCase(columnInfo.getName()))
                .append("(")
                .append(beanFieldType)
                .append(" ")
                .append(columnInfo.getName())
                .append("){this.")
                .append(columnInfo.getName())
                .append(" = ")
                .append(columnInfo.getName())
                .append(";}\n");
        getAndSet.setFiledSet(setField.toString());

        return getAndSet;
    }

    /**
     * 根据表信息生成java类源代码
     * @param tableInfo 表信息
     * @param convertor 转换器
     * @return
     */
    private static String createJavaBean(TableInfo tableInfo,TypeConvertor convertor){
        Map<String,ColumnInfo> columnInfoMap = tableInfo.getColumnInfoMap();
        List<FieldGetAndSet> javaFields = new ArrayList<FieldGetAndSet>();

        for (ColumnInfo columnInfo : columnInfoMap.values()){
            javaFields.add(createFieldGetAndSet(columnInfo,convertor));
        }

        StringBuffer src = new StringBuffer();
        /**
         * package info
         */
        src.append("package ")
                .append(ConfigHolder.getBeanPackage())
                .append(";\n\n");
        /**
         * import info
         */
        src.append("import java.sql.*;\n")
                .append("import java.util.*;\n\n");
        /**
         * class head
         */
        src.append("public class ")
                .append(under2Camel2UpperCase(tableInfo.getName()))
                .append("{\n\n");
        /**
         * field info
         */
        for (FieldGetAndSet fieldGetAndSet : javaFields){
            src.append(fieldGetAndSet.getFiled())
                    .append(fieldGetAndSet.getFiledGet())
                    .append(fieldGetAndSet.getFiledSet());
        }

        src.append("}\n");
       return src.toString();
    }

    private static String firstChar2UpperCase(String str){
        return StringUtils.isNotBlank(str) ?
                str.toUpperCase().substring(0,1)+str.substring(1)
                : StringUtils.EMPTY;
    }

    private static String firstChar2Lower(String str){
        return StringUtils.isNotBlank(str) ?
                str.toLowerCase().substring(0,1)+str.substring(1)
                : StringUtils.EMPTY;
    }

    private static String under2Camel2Lower(String str){
        if (StringUtils.isNotBlank(str)){
            String camel = Under2CamelProcessor.under2Camel(str);
            return firstChar2Lower(camel);
        }else{
            return StringUtils.EMPTY;
        }
    }

    private static String under2Camel2UpperCase(String str){
        if (StringUtils.isNotBlank(str)){
            String caml = Under2CamelProcessor.under2Camel(str);
            return firstChar2UpperCase(caml);
        }else{
            return StringUtils.EMPTY;
        }
    }

    public static void createBeanFile(TableInfo tableInfo,TypeConvertor convertor) throws IOException {
        String src = createJavaBean(tableInfo,convertor);
        String srcPath = ConfigHolder.getSrcPath() + "\\";
        String packagePath = ConfigHolder.getBeanPackage().replaceAll("\\.","/");

        File file = new File(srcPath + packagePath);
        if (!file.exists()){
            file.mkdirs();
        }

       FileUtils.writeByteArrayToFile(
                new File(file.getAbsoluteFile()+"/"+under2Camel2UpperCase(tableInfo.getName())+".java"),src.getBytes()
        );
    }
}
