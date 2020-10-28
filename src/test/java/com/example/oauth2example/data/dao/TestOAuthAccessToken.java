package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.UserType;
import com.example.oauth2example.vo.OAuthAccessToken;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestOAuthAccessToken {
    public static final Date createdDate = new Date(1603359529827L); //2020-10-22T09:38:49.827Z
    public static final Date expireDate = new Date(1603366729827L); //2020-10-22T11:38:49.827Z
    public static final Date expireDate2 = new Date(new Date().getTime() + 3600 * 1000); //2020-10-22T11:38:49.827Z
    public static final int type = UserType.EMAIL.getType();
    public static final String nonce = "nb912eca1589tj01a";
    public static String jti = "a9d36864-67fe-41df-a1ca-550822d3f7c6";
    public static final String sub = TestOAuthClientUser.clientUserId;
    public static final String aud = TestOAuthClient.clientId;
    public static final List<String> scope = Arrays.asList("open_id", "profile");

    public static OAuthAccessToken getTestData() {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
        oAuthAccessToken.setJti(jti);
        oAuthAccessToken.setSub(sub);
        oAuthAccessToken.setAud(aud);
        oAuthAccessToken.setScope(scope);
        oAuthAccessToken.setCreatedDate(createdDate);
        oAuthAccessToken.setExpireAt(expireDate);
        oAuthAccessToken.setType(type);
        oAuthAccessToken.setNonce(nonce);
        return oAuthAccessToken;
    }

    public static OAuthAccessToken getTestData2() {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
        oAuthAccessToken.setJti(jti);
        oAuthAccessToken.setSub(sub);
        oAuthAccessToken.setAud(aud);
        oAuthAccessToken.setScope(scope);
        oAuthAccessToken.setCreatedDate(createdDate);
        oAuthAccessToken.setExpireAt(expireDate2);
        oAuthAccessToken.setType(type);
        oAuthAccessToken.setNonce(nonce);
        return oAuthAccessToken;
    }
}
