package com.xsy.sys.controller;

import com.xsy.sys.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO 数据字典
 *
 * @author Q1sj
 * @date 2023.4.23 16:30
 */
@RestController("/sys/dictionary")
public class SysDictionaryController {
    @Autowired
    private SysDictionaryService sysDictionaryService;
}
