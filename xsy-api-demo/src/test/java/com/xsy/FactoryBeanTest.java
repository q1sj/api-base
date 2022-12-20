package com.xsy;

import com.xsy.base.util.PageData;
import com.xsy.base.util.SpringContextUtils;
import com.xsy.sys.dao.SysUserDao;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.dto.UserListQuery;
import com.xsy.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Q1sj
 * @date 2022.12.8 14:03
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FactoryBeanTest {
    @Autowired
//    private MyInterface myInterface;
    private I i;
    @Autowired
    private SysUserDao sysUserDao;
    @Test
    public void test() {
        i.foo();
        I2 i2 = SpringContextUtils.getBean(I2.class);
        i2.foo();
    }


}




