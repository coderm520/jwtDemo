package com.example.demo.service;


import com.example.demo.model.jwt.JwtModel;
import com.example.demo.model.jwt.JwtReturnInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface TokenService {
    void getToken(String userName,String pwd);

    String getJwtToken(String userName);

    JwtReturnInfo checkJwt(String jwtStr) throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

    JwtReturnInfo checkJwt(JwtModel jwtModel) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, JsonProcessingException;

    boolean login(String userName,String pwd);

}
