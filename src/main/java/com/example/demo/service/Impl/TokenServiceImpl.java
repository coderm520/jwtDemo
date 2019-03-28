package com.example.demo.service.Impl;

import com.example.demo.model.*;
import com.example.demo.service.TokenService;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

@Service
public class  TokenServiceImpl implements TokenService {

    @Autowired
    private UserService userService;

    //过期时间
    private final static Integer ExpireMinites = 10;
    private final static String Secret = "dgdH234@#%df1*";
    private static final String MAC_INSTANCE_NAME = "HMacSHA256";
    private static final char[] BASE64URL_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
    private static final int[] BASE64URL_IALPHABET = new int[256];
    private static final int[] BASE64_IALPHABET = new int[256];

    private static final int IALPHABET_MAX_INDEX = BASE64_IALPHABET.length - 1;
    private final int[] IALPHABET=BASE64URL_IALPHABET;


    @Override
    public void getToken(String userName, String pwd) {

    }

    @Override
    public String getJwtToken(String userName, String pwd) {
        boolean loginResult=login(userName,pwd);
        if (loginResult) {
            UserInfo userInfo = userService.getByUserName(userName);
            String rid=userInfo!=null?userInfo.getRoleInfo().getRoleId().toString():"";

            JwtHeader header = new JwtHeader("HS256", "JWT");

            Long currentTimeMill = System.currentTimeMillis();
            Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTimeInMillis(currentTimeMill);
            calendar.add(calendar.MINUTE, ExpireMinites);
            String timeStampStr = String.valueOf(calendar.getTimeInMillis());

            JwtPayload payload = new JwtPayload("coderm520.github.io", timeStampStr, "subtest", "jser", "0", String.valueOf(currentTimeMill), "jti", userName,rid);

            try {
                String headerStr = getJwtStr(header);
                String payloadStr = getJwtStr(payload);
                String signatureStr = getJwtSignatureStr(headerStr, headerStr);

                return getFinalToken(headerStr, payloadStr, signatureStr);
            } catch (JsonProcessingException je) {
            } catch (UnsupportedEncodingException ue) {
            } catch (NoSuchAlgorithmException ne) {
            } catch (InvalidKeyException ie) {
            }
        }
        return "";
    }

    @Override
    public JwtReturnInfo checkJwt(String jwtStr) {
        String[] jwtArr = jwtStr.split("\\.");
        JwtModel jwtModel=new JwtModel();
        JwtReturnInfo jwtReturnInfo=new JwtReturnInfo();
        jwtReturnInfo.setHttpStatus(HttpStatus.FORBIDDEN);
        if (jwtArr!=null&&jwtArr.length>0){
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String headerStr=new String(Base64.getUrlDecoder().decode(jwtArr[0]), StandardCharsets.US_ASCII);
                JwtHeader header = objectMapper.readValue(headerStr, JwtHeader.class);
                //String payloadStr=new String(Base64.getUrlDecoder().decode(jwtArr[1]), StandardCharsets.US_ASCII);
                String payloadStr=new String(decodeFast(jwtArr[1].toCharArray()), StandardCharsets.US_ASCII);
                JwtPayload payload = objectMapper.readValue(payloadStr, JwtPayload.class);
                String signatureStr=new String(Base64.getUrlDecoder().decode(jwtArr[2]), StandardCharsets.US_ASCII);
                JwtSignature signature = objectMapper.readValue(signatureStr, JwtSignature.class);
                jwtModel.setHeader(header);
                jwtModel.setPayload(payload);
                jwtModel.setSignature(signature);
                return checkJwt(jwtModel);
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return jwtReturnInfo;
    }

    final byte[] decodeFast(char[] sArr) throws IOException {

        // Check special case
        int sLen = sArr != null ? sArr.length : 0;
        if (sLen == 0) {
            return new byte[0];
        }

        int sIx = 0, eIx = sLen - 1;    // Start and end index after trimming.

        // Trim illegal chars from start
        while (sIx < eIx && BASE64URL_IALPHABET[sArr[sIx]] < 0) {
            sIx++;
        }

        // Trim illegal chars from end
        while (eIx > 0 && BASE64URL_IALPHABET[sArr[eIx]] < 0) {
            eIx--;
        }

        // get the padding count (=) (0, 1 or 2)
        int pad = sArr[eIx] == '=' ? (sArr[eIx - 1] == '=' ? 2 : 1) : 0;  // Count '=' at end.
        int cCnt = eIx - sIx + 1;   // Content count including possible separators
        int sepCnt = sLen > 76 ? (sArr[76] == '\r' ? cCnt / 78 : 0) << 1 : 0;

        int len = ((cCnt - sepCnt) * 6 >> 3) - pad; // The number of decoded bytes
        byte[] dArr = new byte[len];       // Preallocate byte[] of exact length

        // Decode all but the last 0 - 2 bytes.
        int d = 0;
        for (int cc = 0, eLen = (len / 3) * 3; d < eLen; ) {

            // Assemble three bytes into an int from four "valid" characters.
            int i = ctoi(sArr[sIx++]) << 18 | ctoi(sArr[sIx++]) << 12 | ctoi(sArr[sIx++]) << 6 | ctoi(sArr[sIx++]);

            // Add the bytes
            dArr[d++] = (byte) (i >> 16);
            dArr[d++] = (byte) (i >> 8);
            dArr[d++] = (byte) i;

            // If line separator, jump over it.
            if (sepCnt > 0 && ++cc == 19) {
                sIx += 2;
                cc = 0;
            }
        }

        if (d < len) {
            // Decode last 1-3 bytes (incl '=') into 1-3 bytes
            int i = 0;
            for (int j = 0; sIx <= eIx - pad; j++) {
                i |= ctoi(sArr[sIx++]) << (18 - j * 6);
            }

            for (int r = 16; d < len; r -= 8) {
                dArr[d++] = (byte) (i >> r);
            }
        }

        return dArr;
    }
    private int ctoi(char c) {
        int i = c > IALPHABET_MAX_INDEX ? -1 : IALPHABET[c];
        if (i < 0) {
            //String msg = "Illegal " + getName() + " character: '" + c + "'";
            //throw new IOException(msg);
        }
        return i;
    }

    @Override
    public JwtReturnInfo checkJwt(JwtModel jwtModel)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, JsonProcessingException {

        JwtReturnInfo jwtReturnInfo=new JwtReturnInfo();
        String jwtSignatureStr=getJwtSignatureStr(jwtModel.getHeader(),jwtModel.getPayload());
        if (!jwtSignatureStr.equals(jwtModel.getSignature())){
            jwtReturnInfo.setHttpStatus(HttpStatus.UNAUTHORIZED);
            return jwtReturnInfo;
        }

        //过期时间
        JwtPayload payload = jwtModel.getPayload();
        Long expTimeStamp = Long.valueOf(payload.getExp());
        if (expTimeStamp<System.currentTimeMillis()){
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

    private <T> String getJwtStr(T jwtObject)
            throws JsonProcessingException, UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String result = objectMapper.writeValueAsString(jwtObject);
        System.out.println(result);
        result = Base64.getUrlEncoder().encodeToString(result.getBytes(StandardCharsets.US_ASCII));
        if (result.endsWith("=")) {
            result = result.substring(0, result.length() - 1);
        }
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
        byte[] bytes = hmac_sha256.doFinal(result.getBytes(StandardCharsets.US_ASCII));

        String signature = Base64.getUrlEncoder().encodeToString(bytes);
        if (signature.endsWith("=")) {
            signature = signature.substring(0, signature.length() - 1);
        }
        if (signature.endsWith("=")) {
            signature = signature.substring(0, signature.length() - 1);
        }
        return signature;
    }

    private String getJwtSignatureStr(JwtHeader jwtHeader, JwtPayload jwtPayload)
            throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String jwtHeaderStr = getJwtStr(jwtHeader);
        String jwtPayloadStr = getJwtStr(jwtPayload);
        return getJwtSignatureStr(jwtHeaderStr, jwtPayloadStr);
    }

    private String getFinalToken(JwtModel jwtModel)
            throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        ObjectMapper objectMapper = new ObjectMapper();
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
