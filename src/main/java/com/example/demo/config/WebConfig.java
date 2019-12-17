package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author m110266
 * @Date 2019/4/11 17:51
 **/
@Configuration
@ComponentScan(basePackages = { "com.example.demo.controller"})
public class WebConfig {
}
