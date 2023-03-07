package com.xsy.api.demo.controller;

import com.xsy.sys.service.DynamicTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Q1sj
 * @date 2023.3.1 15:17
 */
@Slf4j
@Component
public class TestTask implements DynamicTask {

    @Override
    public void run() {
        log.info("run....");
    }
}
