package com.iterlife.nazha.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc：TODO
 * @date：2019-05-31 21:54
 * @author：lujie
 */
@RestController
@RequestMapping("/deom")
public class DemoController {

    @GetMapping(value = "/hello")
    public String testDemo() {
        return "Hello NaZha!";
    }
}
