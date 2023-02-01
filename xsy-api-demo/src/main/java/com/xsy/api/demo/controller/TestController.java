package com.xsy.api.demo.controller;

import com.xsy.base.enums.ResultCodeEnum;
import com.xsy.base.util.EnumUtils;
import com.xsy.security.annotation.NoAuth;
import com.xsy.sys.dto.SysUserDTO;
import com.xsy.sys.entity.StringKey;
import com.xsy.sys.service.SysConfigService;
import com.xsy.sys.service.SysUserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Q1sj
 * @date 2023.1.4 15:11
 */
@Slf4j
@RestController
public class TestController implements TestApi {
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    @NoAuth
    public String a() {
        return "ok";
    }

    @NoAuth
    @GetMapping("/b")
    public String b() {
        log.info("bbbb");
        String a1 = a();
//        log.info("a:{}", a1);
        SysUserDTO sysUserDTO = sysUserService.get(1067246875800000001L);
//        log.info("user:{}", sysUserDTO);
        String a = sysConfigService.get(new StringKey("1", "a"));
//        log.info("val:{}", a);
        log.info("bbbb end");
        return "ok";
    }
}
