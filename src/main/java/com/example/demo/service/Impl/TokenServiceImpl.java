package com.example.demo.service.Impl;

import com.example.demo.model.JwtHeader;
import com.example.demo.model.JwtModel;
import com.example.demo.model.JwtPayload;
import com.example.demo.model.JwtSignature;
import com.example.demo.service.TokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    //过期时间
    private final static Integer ExpireMinites = 10;
    private final static String Secret = "dgdH234@#%df1*";
    private static final String MAC_INSTANCE_NAME = "HMacSHA256";

    @Override
    public void getToken(String userName, String pwd) {

    }

    @Override
    public String getJwtToken(String userName, String pwd) {
        JwtHeader header = new JwtHeader("HS256", "JWT");

        Long currentTimeMill= System.currentTimeMillis();
        Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(currentTimeMill);
        calendar.add(calendar.MINUTE, ExpireMinites);
        String timeStampStr = String.valueOf(calendar.getTimeInMillis());

        JwtPayload payload = new JwtPayload("coderm520.github.io", timeStampStr, "subtest", "jser", "0", String.valueOf(currentTimeMill), "jti", userName);

        try {
            String headerStr = getJwtHeaderStr(header);
            String payloadStr = getJwtPayloadStr(payload);
            String signatureStr = getJwtSignatureStr(headerStr, headerStr);

            return getFinalToken(headerStr, payloadStr, signatureStr);
        } catch (JsonProcessingException je) {
        } catch (UnsupportedEncodingException ue) {
        } catch (NoSuchAlgorithmException ne) {
        } catch (InvalidKeyException ie) {
        }

        return "";
    }


    private String getJwtHeaderStr(JwtHeader jwtHeader)
            throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jwtHeader);
        System.out.println(result);
        result = Base64.getUrlEncoder().encodeToString(result.getBytes("US-ASCII"));
        if (result.endsWith("=")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String getJwtPayloadStr(JwtPayload jwtPayload)
            throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jwtPayload);
        System.out.println(result);

        result = Base64.getUrlEncoder().encodeToString(result.getBytes("US-ASCII"));
        if (result.endsWith("=")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String getJwtSignatureStr(String jwtHeader, String jwtPayload)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String result = jwtHeader + "." + jwtPayload;
        Mac hmac_sha256 = Mac.getInstance(MAC_INSTANCE_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(Secret.getBytes(), MAC_INSTANCE_NAME);
        hmac_sha256.init(keySpec);
        byte[] bytes = hmac_sha256.doFinal(result.getBytes("US-ASCII"));

        String signature = Base64.getUrlEncoder().encodeToString(bytes);
        if (signature.endsWith("=")) {
            signature = signature.substring(0, signature.length() - 1);
        }
        return signature;
    }

    private String getJwtSignatureStr(JwtHeader jwtHeader, JwtPayload jwtPayload)
            throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String jwtHeaderStr = getJwtHeaderStr(jwtHeader);
        String jwtPayloadStr = getJwtPayloadStr(jwtPayload);
        return getJwtSignatureStr(jwtHeaderStr, jwtPayloadStr);
    }

    private String getFinalToken(JwtModel jwtModel)
            throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        ObjectMapper objectMapper = new ObjectMapper();
        String headerStr = getJwtHeaderStr(jwtModel.getHeader());
        String payloadStr = getJwtPayloadStr(jwtModel.getPayload());
        String signatureStr = getJwtSignatureStr(headerStr, payloadStr);
        System.out.println("header is :" + headerStr);
        System.out.println("payload is :" + payloadStr);
        System.out.println("signature is :" + signatureStr);

        String result = getFinalToken(headerStr, payloadStr, signatureStr);
        return result;
    }

    private String getFinalToken(String headerStr, String payloadStr, String signatureStr) {
        String result = String.format("%s.%s.%s", headerStr, payloadStr, signatureStr);
        return result;
    }
}
