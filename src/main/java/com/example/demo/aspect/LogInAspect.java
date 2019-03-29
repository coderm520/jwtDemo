package com.example.demo.aspect;

import com.example.demo.components.Login;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Aspect
public class LogInAspect {

//    @Pointcut("execution(* com.example.demo.controller.*.*(..))")
//    public void access() {
//    }
//
//    @Before("access()")
//    public void beforeAccess(JoinPoint joinPoint){
//        Object[] args = joinPoint.getArgs();
//        System.out.println("beforeAccess,参数 : " + Arrays.toString(args));
//
//    }

    @Pointcut(value = "@annotation(com.example.demo.components.Login)")
    public void access(){

    }

    @Before(value = "access()")
    public void beforeAccess(){
        System.out.println("开始验证登录状态");
    }

    @Around(value = "@annotation(login)")
    public void aroundAccess(ProceedingJoinPoint joinPoint, Login login){
        System.out.println("login name is "+login.name());
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //request.getHeader("")
        }
        catch (Throwable throwable){
            System.out.println("error in aroundAccess");
        }
    }
}
