package com.facedamon.orm.generator.core;

import lombok.Data;

/**
* @Description:    数据库链接配置类
* @Author:         facedamon
* @CreateDate:     2018/7/29 17:56
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/29 17:56
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
@Data
public class Configuration {
    private String driver;
    private String url;
    private String user;
    private String pwd;
    private String server;
    private String srcPath;
    private String beanPackage;
}
