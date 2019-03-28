package com.example.demo.model;

import lombok.Data;

@Data
public class UserInfo {
    private Integer userId;
    private String userName;
    private String pwd;

    private RoleInfo roleInfo;


    public UserInfo() {
    }

    public UserInfo(Integer userId, String userName, String pwd) {
        this.userId = userId;
        this.userName = userName;
        this.pwd = pwd;
    }

    public UserInfo(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
}
