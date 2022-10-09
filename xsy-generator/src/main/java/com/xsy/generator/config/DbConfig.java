/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.xsy.generator.config;


import com.xsy.generator.dao.*;
import com.xsy.generator.utils.RenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 数据库配置
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class DbConfig {
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Autowired
    private MySQLGeneratorDao mySQLGeneratorDao;
    @Autowired
    private OracleGeneratorDao oracleGeneratorDao;
    @Autowired
    private SQLServerGeneratorDao sqlServerGeneratorDao;
    @Autowired
    private PostgreSQLGeneratorDao postgreSQLGeneratorDao;

    @Bean
    @Primary
    public GeneratorDao getGeneratorDao() {
        switch (driverClassName) {
            case "com.mysql.cj.jdbc.Driver":
                return mySQLGeneratorDao;
            case "oracle.jdbc.OracleDriver":
                return oracleGeneratorDao;
            case "com.microsoft.sqlserver.jdbc.SQLServerDriver":
                return sqlServerGeneratorDao;
            case "org.postgresql.Driver":
                return postgreSQLGeneratorDao;
            default:
                throw new RenException("不支持当前数据库：" + driverClassName);
        }
    }
}
