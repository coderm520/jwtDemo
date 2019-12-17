package com.example.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @Description TODO
 * @Author m110266
 * @Date 2019/4/11 17:48
 **/
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.example.demo.config" }, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = {}))
public class Config {
}
