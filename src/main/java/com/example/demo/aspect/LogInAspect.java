package com.example.demo.aspect;

import com.example.demo.components.Login;
import com.example.demo.model.JwtReturnInfo;
import com.example.demo.service.TokenService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Component
@Aspect
public class LogInAspect {
    @Autowired
    TokenService tokenService;

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
    public void access() {

    }

    @Before(value = "access()")
    public void beforeAccess() {
        System.out.println("开始验证登录状态");
    }

    @Around(value = "@annotation(login)")
    public Object aroundAccess(ProceedingJoinPoint joinPoint, Login login) {
        System.out.println("login name is " + login.name());
        JwtReturnInfo returnInfo = new JwtReturnInfo();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("Authorization");
            System.out.println("tokenStr is :" + token);
            if (!StringUtils.isEmpty(token)) {
                if (token.contains("Bearer") && token.contains(" ")) {
                    token = token.split(" ")[1];
                    System.out.println("tokenStr is :" + token);
                    //校验
                    returnInfo = tokenService.checkJwt(token);
                    if (returnInfo.getHttpStatus() == HttpStatus.OK) {
                        System.out.println("token is correct");
                        return joinPoint.proceed();
                    } else if (returnInfo.getHttpStatus() == HttpStatus.UNAUTHORIZED) {
                        System.out.println("UNAUTHORIZED");
                        return "redirect:/loginA";
                    } else if (returnInfo.getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        System.out.println("token is Expired");
                        return "redirect:/loginA";
                    }
                }
                return null;
            } else {
                returnInfo.setHttpStatus(HttpStatus.FORBIDDEN);
                System.out.println("token is null");
                return "redirect:/loginA";
            }
        } catch (Throwable throwable) {
            System.out.println("error in aroundAccess");
        }
        return null;
    }
}
