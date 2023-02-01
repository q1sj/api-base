package com.xsy.api.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Q1sj
 * @date 2023.1.4 15:10
 */
@RequestMapping("/test")
public interface TestApi {
    @GetMapping("/a")
    String a();
}
