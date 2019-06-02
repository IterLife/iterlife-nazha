package com.iterlife.nazha.demo.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc：主接口
 * @date：2019-06-02 09:36
 * @author：lujie
 */
@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/home")
public class AppController {

    @RequestMapping(value = {"/hello/{args}", "/hello"}, method = RequestMethod.GET)
    public String hello(@PathVariable(name = "args", required = false) String args) {
        return new StringBuilder("Hello World,NaZha! ").append(args).toString();
    }
}
