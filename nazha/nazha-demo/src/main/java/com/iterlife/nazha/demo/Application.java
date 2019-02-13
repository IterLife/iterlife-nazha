package com.iterlife.nazha.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan.Filter;

import com.iterlife.nazha.core.annotation.Job;

/**
*
* @desc Hello World
* @author Lu Jie
* @date 2019 2019年2月13日 上午11:43:51
* @tags 
*/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Configuration
@ComponentScan(basePackages = { "com.iterlife.nazha" }, useDefaultFilters = false, includeFilters = @Filter(type = FilterType.ANNOTATION, classes = { Job.class }))
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
