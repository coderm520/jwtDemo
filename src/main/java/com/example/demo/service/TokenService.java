package com.example.demo.service;


public interface TokenService {
    void getToken(String userName,String pwd);

    String getJwtToken(String userName,String pwd);
}
