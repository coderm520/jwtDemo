package com.example.demo.service;


import com.example.demo.model.JwtModel;
import com.example.demo.model.JwtReturnInfo;
import com.example.demo.model.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface TokenService {
    void getToken(String userName,String pwd);

    String getJwtToken(String userName,String pwd);

    JwtReturnInfo checkJwt(String jwtStr);

    JwtReturnInfo checkJwt(JwtModel jwtModel) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, JsonProcessingException;

    boolean login(String userName,String pwd);

}
