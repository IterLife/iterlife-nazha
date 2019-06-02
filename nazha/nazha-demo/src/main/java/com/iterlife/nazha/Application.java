package com.iterlife.nazha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author Lu Jie
 * @desc NaZha Demo 启动类
 * @date 2019 2019年2月13日 上午11:43:51
 * @tags
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Configuration
@ComponentScan(basePackages = {"com.iterlife.nazha"}, useDefaultFilters = false, includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION))
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.run(args);
        log.info("Hello,,NaZha Demo Started!");
    }

}
