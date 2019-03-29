package com.example.demo.model;

import lombok.Data;

@Data
public class JwtModel {
    private JwtHeader header;

    private JwtPayload payload;

    private String signature;

    public JwtModel(){}
    public JwtModel(JwtHeader header, JwtPayload payload) {
        this.header = header;
        this.payload = payload;
    }



}
