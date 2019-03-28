package com.example.demo.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleInfo {
    private Integer roleId;
    private String  roleName;

    private List<UserInfo> userInfoList;

    public RoleInfo(Integer roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public RoleInfo() {
    }
}
