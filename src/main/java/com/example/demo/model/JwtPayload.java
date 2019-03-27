package com.example.demo.model;

import lombok.Data;

@Data
public class JwtPayload {
   /* iss (issuer)：签发人
    exp (expiration time)：过期时间
    sub (subject)：主题
    aud (audience)：受众
    nbf (Not Before)：生效时间
    iat (Issued At)：签发时间
    jti (JWT ID)：编号
    ps-可加入私有字段，JWT 默认是不加密的，任何人都可以读到，所以不要把秘密信息放在这个部分，要使用 Base64URL 算法转成字符串*/

    private String iss;
    private String exp;
    private String sub;
    private String aud;
    private String nbf;
    private String iat;
    private String jti;
    private String name;


    public JwtPayload(String iss, String exp, String sub, String aud, String nbf, String iat, String jti,String name) {
        this.iss = iss;
        this.exp = exp;
        this.sub = sub;
        this.aud = aud;
        this.nbf = nbf;
        this.iat = iat;
        this.jti = jti;
        this.name = name;
    }
}
