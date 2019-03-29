package com.example.demo.components;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@Inherited
public @interface Login {
    String name() default "验证登录";
}
