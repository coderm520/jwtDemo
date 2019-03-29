package com.example.demo.service.Impl;

import com.example.demo.model.*;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UserService userService;

    //过期时间
    private final static Integer ExpireMinutes = 10;
    private final static String Secret = "dgdH234@#%df1*";
    private static final String MAC_INSTANCE_NAME = "HMacSHA256";

    @Override
    public void getToken(String userName, String pwd) { }

    @Override
    public String getJwtToken(String userName, String pwd) {
        boolean loginResult = login(userName, pwd);
        if (loginResult) {
            UserInfo userInfo = userService.getByUserName(userName);
            String rid = userInfo != null ? userInfo.getRoleInfo().getRoleId().toString() : "";

            JwtHeader header = new JwtHeader("HS256", "JWT");

            Long currentTimeMill = System.currentTimeMillis();
            //currentTimeMill=543546L;//for test
            Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(currentTimeMill);
            calendar.add(calendar.MINUTE, ExpireMinutes);
            String timeStampStr = String.valueOf(calendar.getTimeInMillis());

            JwtPayload payload = new JwtPayload("coderm520.github.io", timeStampStr, "subtest", "jser", "0", String.valueOf(currentTimeMill), "jti", userName, rid);

            try {
                String headerStr = getJwtStr(header);
                String payloadStr = getJwtStr(payload);
                String signatureStr = getJwtSignatureStr(headerStr, payloadStr);

                return getFinalToken(headerStr, payloadStr, signatureStr);
            } catch (JsonProcessingException je) {
            } catch (NoSuchAlgorithmException ne) {
            } catch (InvalidKeyException ie) {
            }
        }
        return "";
    }


    @Override
    public JwtReturnInfo checkJwt(String jwtStr)
            throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        JwtModel jwtModel = getJwtModelByJwtStr(jwtStr);
        return checkJwt(jwtModel);
    }

    @Override
    public JwtReturnInfo checkJwt(JwtModel jwtModel)
            throws NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {

        JwtReturnInfo jwtReturnInfo = new JwtReturnInfo();
        String jwtSignatureStr = getJwtSignatureStr(jwtModel.getHeader(), jwtModel.getPayload());
        if (!jwtSignatureStr.equals(jwtModel.getSignature())) {
            jwtReturnInfo.setHttpStatus(HttpStatus.UNAUTHORIZED);
            return jwtReturnInfo;
        }

        //过期时间
        JwtPayload payload = jwtModel.getPayload();
        Long expTimeStamp = Long.valueOf(payload.getExp());
        if (expTimeStamp < System.currentTimeMillis()) {
            jwtReturnInfo.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            return jwtReturnInfo;
        }

        jwtReturnInfo.setHttpStatus(HttpStatus.OK);
        jwtReturnInfo.setUserName(payload.getUname());
        jwtReturnInfo.setRoleId(Integer.valueOf(payload.getRid()));

        return jwtReturnInfo;
    }

    @Override
    public boolean login(String userName, String pwd) {
        return true;
    }


    private JwtModel getJwtModelByJwtStr(String jwtStr) {
        String[] jwtArr = jwtStr.split("\\.");
        JwtModel jwtModel = new JwtModel();

        if (jwtArr != null && jwtArr.length > 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String headerStr = new String(Base64.getUrlDecoder().decode(jwtArr[0]), StandardCharsets.US_ASCII);
                JwtHeader header = objectMapper.readValue(headerStr, JwtHeader.class);
                String payloadStr = new String(Base64Utils.decodeFromUrlSafeString(jwtArr[1]), StandardCharsets.US_ASCII);
                JwtPayload payload = objectMapper.readValue(payloadStr, JwtPayload.class);
                jwtModel.setHeader(header);
                jwtModel.setPayload(payload);
                jwtModel.setSignature(jwtArr[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jwtModel;
    }

    private <T> String getJwtStr(T jwtObject)
            throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jwtObject);
        System.out.println(result);
        result = Base64.getUrlEncoder().withoutPadding().encodeToString(result.getBytes(StandardCharsets.US_ASCII));
        return result;
    }

    private String getJwtSignatureStr(String jwtHeader, String jwtPayload)
            throws NoSuchAlgorithmException, InvalidKeyException {
        String result = jwtHeader + "." + jwtPayload;
        Mac hmac_sha256 = Mac.getInstance(MAC_INSTANCE_NAME);
        SecretKeySpec keySpec = new SecretKeySpec(Secret.getBytes(), MAC_INSTANCE_NAME);
        hmac_sha256.init(keySpec);
        byte[] bytes = hmac_sha256.doFinal(result.getBytes(StandardCharsets.US_ASCII));
        String signature = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return signature;
    }

    private String getJwtSignatureStr(JwtHeader jwtHeader, JwtPayload jwtPayload)
            throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        String jwtHeaderStr = getJwtStr(jwtHeader);
        String jwtPayloadStr = getJwtStr(jwtPayload);
        return getJwtSignatureStr(jwtHeaderStr, jwtPayloadStr);
    }

    private String getFinalToken(JwtModel jwtModel)
            throws JsonProcessingException, NoSuchAlgorithmException, InvalidKeyException {
        String headerStr = getJwtStr(jwtModel.getHeader());
        String payloadStr = getJwtStr(jwtModel.getPayload());
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
