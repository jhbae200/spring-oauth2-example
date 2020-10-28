package com.example.oauth2example.data.dao;

import com.example.oauth2example.vo.OAuthIdToken;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestOAuthIdToken {
    public static final Date createdDate = new Date(1603359529827L); //2020-10-22T09:38:49.827Z
    public static final Date expireDate = new Date(1603366729827L); //2020-10-22T11:38:49.827Z
    public static final String nonce = "nb912eca1589tj01a";
    public static final String jti = "36D594BC-BC24-487E-B01B-6FDEAD7B8213";
    public static final String sub = TestOAuthClientUser.clientUserId;
    public static final String aud = TestOAuthClient.clientId;
    public static final List<String> scope = Arrays.asList("open_id", "profile");
    public static final String id = TestUser.id;
    public static final String nickname = TestUser.nickname;
    public static final int type = TestUser.type;
    public static final String atHash = "nzJhboce7mVk-W8CjyBRTw";

    public static OAuthIdToken getTestData() {
        OAuthIdToken oAuthIdToken = new OAuthIdToken();
        oAuthIdToken.setJti(jti);
        oAuthIdToken.setSub(sub);
        oAuthIdToken.setAud(aud);
        oAuthIdToken.setScope(scope);
        oAuthIdToken.setId(id);
        oAuthIdToken.setNickname(nickname);
        oAuthIdToken.setType(type);
        oAuthIdToken.setCreatedDate(createdDate);
        oAuthIdToken.setExpireDate(expireDate);
        oAuthIdToken.setAtHash(atHash);
        oAuthIdToken.setNonce(nonce);
        return oAuthIdToken;
    }
}
