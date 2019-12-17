package com.example.demo.controller;

import com.example.demo.model.LoginReturnInfo;
import com.example.demo.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    TokenService tokenService;

    @ResponseBody
    @PostMapping(value = "/do")
    public LoginReturnInfo doLogIn(@RequestParam String name, @RequestParam String pwd) {
        LoginReturnInfo returnInfo = new LoginReturnInfo();
        String token = "";
        boolean logIn = tokenService.login(name, pwd);
        if (logIn) {
            token = tokenService.getJwtToken(name);
            returnInfo.setReturnCode(HttpStatus.OK.value());
        } else {
            returnInfo.setReturnCode(HttpStatus.UNAUTHORIZED.value());
        }
        returnInfo.setToken(token);
        return returnInfo;
    }
}
