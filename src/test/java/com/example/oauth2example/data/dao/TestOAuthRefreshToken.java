package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestOAuthRefreshToken {
    public static final Long seq = 1L;
    public static final String jti = "83383eca-dd3f-4d03-a06e-0aa62c2af9ac";
    public static final String jti2 = "c082aab0-c821-43ec-a1e1-e4b32dff1ad5";
    public static final Long userSeq = TestUser.seq;
    public static final String userId = TestOAuthClientUser.clientUserId;
    public static final Long clientSeq = TestOAuthClient.seq;
    public static final String clientId = TestOAuthClient.clientId;
    public static final List<String> scope = Arrays.asList("open_id", "profile");
    public static final String nonce = "nb912eca1589tj01a";
    public static final Date accessAt = new Date(1603359529827L);
    public static final Date createdDate = new Date(1603359529827L);
    public static final Date expireDate = null;
    public static final Integer type = TestUser.type;

    public static OAuthRefreshToken getTestData() {
        OAuthRefreshToken oAuthRefreshToken = new OAuthRefreshToken();
        oAuthRefreshToken.setSeq(seq);
        oAuthRefreshToken.setJti(jti);
        oAuthRefreshToken.setUserSeq(userSeq);
        oAuthRefreshToken.setUserId(userId);
        oAuthRefreshToken.setClientSeq(clientSeq);
        oAuthRefreshToken.setClientId(clientId);
        oAuthRefreshToken.setScope(Strings.join(scope, ' '));
        oAuthRefreshToken.setNonce(nonce);
        oAuthRefreshToken.setAccessAt(accessAt);
        oAuthRefreshToken.setCreatedDate(createdDate);
        oAuthRefreshToken.setExpireDate(expireDate);
        oAuthRefreshToken.setType(type);
        return oAuthRefreshToken;
    }
}
