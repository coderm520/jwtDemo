package com.example.demo.model;

import lombok.Data;

/**
 * @Description JwtPayload构造器
 * @Author m110266
 * @Date 2019/11/20 16:28
 **/
@Data
public class JwtPayloadBuilder {
    private JwtPayload jwtPayload;

    public JwtPayloadBuilder() {
        jwtPayload = new JwtPayload();
    }

    public JwtPayloadBuilder iss(String iss){
        jwtPayload.setIss(iss);
        return this;
    }
    public JwtPayloadBuilder exp(String exp){
        jwtPayload.setExp(exp);
        return this;
    }
    public JwtPayloadBuilder sub(String sub){
        jwtPayload.setSub(sub);
        return this;
    }
    public JwtPayloadBuilder aud(String aud){
        jwtPayload.setAud(aud);
        return this;
    }

    public JwtPayloadBuilder nbf(String nbf){
        jwtPayload.setNbf(nbf);
        return this;
    }
    public JwtPayloadBuilder iat(String iat){
        jwtPayload.setIat(iat);
        return this;
    }
    public JwtPayloadBuilder jti(String jti){
        jwtPayload.setJti(jti);
        return this;
    }
    public JwtPayloadBuilder uname(String uname){
        jwtPayload.setUname(uname);
        return this;
    }
    public JwtPayloadBuilder rid(String rid){
        jwtPayload.setRid(rid);
        return this;
    }


    public JwtPayload build(){
        return this.jwtPayload;
    }
}
