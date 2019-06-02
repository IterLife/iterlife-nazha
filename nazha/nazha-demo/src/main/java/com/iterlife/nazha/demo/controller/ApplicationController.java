package com.iterlife.nazha.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc：TODO
 * @date：2019-06-02 09:36
 * @author：lujie
 */
@RestController
@RequestMapping(value = "/home")
public class ApplicationController {

    @RequestMapping(value = "/hello/{args}", method = RequestMethod.GET)
    public String hello(@PathVariable(name = "args") String args) {
        return new StringBuilder("Hello NaZha,").append(args).toString();
    }
}
