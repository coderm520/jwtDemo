package com.example.demo.service;

import com.example.demo.model.JwtModel;
import com.example.demo.model.JwtReturnInfo;
import com.example.demo.model.LoginReturnInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenServiceTest {

    @Autowired
    TokenService tokenService;


    @Test
    public void checkToken()
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        String userName="mmm";
        String pwd="123456";
        boolean logIn = tokenService.login(userName,pwd);
        String token="";
        if (logIn){
            token = tokenService.getJwtToken(userName);
        }
        System.out.println("token is:"+token);

        JwtReturnInfo returnInfo = tokenService.checkJwt(token);
        String result =returnInfo.toString();
        System.out.println(result);
    }

}