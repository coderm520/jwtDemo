package com.example.demo.controller;
import com.example.demo.model.jwt.JwtReturnInfo;
import com.example.demo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/test")
public class TestController {
    @Autowired
    private TokenService tokenService;

    @PostMapping("getUserName")
    public String getUserName( @RequestParam("token") String token) throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        JwtReturnInfo jwtReturnInfo = tokenService.checkJwt(token);

        if (jwtReturnInfo.getHttpStatus()== HttpStatus.INTERNAL_SERVER_ERROR)
            return "token 过期";
        if (jwtReturnInfo.getHttpStatus()!= HttpStatus.OK)
            return "token is error";

        String userName = jwtReturnInfo.getUserName();
        return userName;
    }
}
