package com.example.demo.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class JwtReturnInfo {
    private HttpStatus httpStatus;//httpStatusCode  签名错误401  成功200 签名过期500  token空403
    private String userName;//用户名称
    private Integer roleId;//用户角色id

    public JwtReturnInfo() {
    }

    public String toString(){
        return String.format("httpStatus is %d,userName is %s,roleId is %d",this.httpStatus.value(),this.userName,this.roleId);
    }

    //private Boolean checkResult;//校验结果
}
