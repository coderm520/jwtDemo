package com.example.demo.service;

import com.example.demo.model.UserInfo;

public interface UserService {
    UserInfo getByUserName(String userName);
}
