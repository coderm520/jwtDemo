package com.example.demo.aspect;

import com.example.demo.components.Login;
import com.example.demo.model.JwtReturnInfo;
import com.example.demo.service.TokenService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@Component
@Aspect
public class LogInAspect {

    static final String LOGIN_URL="/login/loginA";

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

    //@Before(value = "access()")
    @Before("execution(*  com.example.demo.controller.TestController.Test(..))")
    public void beforeAccess() {
        System.out.println("开始验证登录状态");
//        JwtTestReturnInfo jwtTestReturnInfo=new JwtTestReturnInfo();
//        jwtTestReturnInfo.setName("asdf");
//        jwtTestReturnInfo.setClassValue("dsf");
//        String ss = JwtHelper.getTestValue();
    }

    @Around(value = "@annotation(login)")
    public Object aroundAccess(ProceedingJoinPoint joinPoint, Login login) throws IOException {
        System.out.println("login name is " + login.name());
        JwtReturnInfo returnInfo = new JwtReturnInfo();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = "";

            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();

            if (requestMethods != null && requestMethods.length > 0 && requestMethods[0].name().equals("POST")) {
                token = request.getHeader("Authorization");
                if (!StringUtils.isEmpty(token)) {
                    if (token.contains("Bearer") && token.contains(" ")) {
                        token = token.split(" ")[1];
                    }
                }
            } else {
                Object[] paramValues = joinPoint.getArgs();
                String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
                if (paramNames.length == paramValues.length) {
                    for (int i = 0; i < paramNames.length; i++) {
                        if (paramNames[i].equals("token") && paramValues[i] != null) {
                            token = paramValues[i].toString();
                            break;
                        }
                    }
                }
            }
            System.out.println("tokenStr is :" + token);
            //String returnType = ((MethodSignature) (joinPoint.getSignature())).getReturnType().getSimpleName();

            if (!StringUtils.isEmpty(token)) {
                //校验
                returnInfo = tokenService.checkJwt(token);
                if (returnInfo.getHttpStatus() == HttpStatus.OK) {
                    System.out.println("token is correct");
                    return joinPoint.proceed();
                } else {
                    response.sendRedirect(LOGIN_URL);
                }
                return null;
            } else {
                returnInfo.setHttpStatus(HttpStatus.FORBIDDEN);
                System.out.println("token is null");
                response.sendRedirect(LOGIN_URL);
                return null;
            }
        } catch (Throwable throwable) {
            System.out.println("error in aroundAccess");
            returnInfo.setHttpStatus(HttpStatus.UNAUTHORIZED);
            System.out.println("token is null or error");
            response.sendRedirect(LOGIN_URL);
        }
        return null;
    }
}
