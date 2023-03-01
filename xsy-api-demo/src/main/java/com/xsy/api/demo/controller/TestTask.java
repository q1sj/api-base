package com.xsy.api.demo.controller;

import com.xsy.sys.service.AbstractDynamicSchedule;
import org.springframework.stereotype.Component;

/**
 * @author Q1sj
 * @date 2023.3.1 15:17
 */
@Component
public class TestTask extends AbstractDynamicSchedule {

    @Override
    public void run() {
        log.info("run....");
    }
}
