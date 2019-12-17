package com.example.demo.model.jwt;
import lombok.Data;

@Data
public class JwtHeader {
    private String alg;//算法

    private String typ;//令牌类型

    public JwtHeader(String alg,String typ){
        this.alg=alg;
        this.typ=typ;
    }

    //必须有无参构造函数 否则反序列化报错
    public JwtHeader() {
    }
}
