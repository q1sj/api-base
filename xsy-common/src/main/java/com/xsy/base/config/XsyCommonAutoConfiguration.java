package com.xsy.base.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Q1sj
 * @date 2023.3.17 15:23
 */
@MapperScan(basePackages = {"com.xsy.*.dao"})
@ComponentScan(basePackages = "com.xsy")
public class XsyCommonAutoConfiguration {
}
