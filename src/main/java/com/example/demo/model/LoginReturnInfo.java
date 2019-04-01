package com.example.demo.model;

import lombok.Data;

@Data
public class LoginReturnInfo extends ReturnInfo {
    private String token;

    private String returnUrl;
    public LoginReturnInfo() {
    }
}
