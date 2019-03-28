package com.example.demo.service.Impl;

import com.example.demo.model.RoleInfo;
import com.example.demo.model.UserInfo;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserInfo getByUserName(String userName) {
        UserInfo userInfo=new UserInfo(1,"miaofb");
        RoleInfo roleInfo=new RoleInfo(1,"admin");
        userInfo.setRoleInfo(roleInfo);

        return userInfo;
    }
}
