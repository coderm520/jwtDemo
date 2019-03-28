package com.example.demo.service;

import com.example.demo.model.JwtModel;
import com.example.demo.model.JwtReturnInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Test
    public void getToken(){
        String userName="miaofb";
        String pwd="123456";
        String token = tokenService.getJwtToken(userName,pwd);
        System.out.println("token is:"+token);

    }

    @Test
    public void checkToken(){
        String userName="miaofb";
        String pwd="123456";
        String token = tokenService.getJwtToken(userName,pwd);
        System.out.println("token is:"+token);

        JwtReturnInfo returnInfo = tokenService.checkJwt(token);
        String result =returnInfo.toString();
        System.out.println(result);
    }

}