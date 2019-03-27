package com.example.demo.model;

import lombok.Data;

@Data
public class JwtSignature {
   /* HMACSHA256(base64UrlEncode(header) + "." +base64UrlEncode(payload), secret)*/
    private String signature;

    public JwtSignature(String signature) {
        this.signature = signature;
    }
}
