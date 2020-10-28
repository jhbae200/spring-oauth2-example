package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.OAuthClientUri;

public class TestOAuthClientUri {
    public static final long seq = 1L;
    public static final long clientId = TestOAuthClient.seq;
    public static final String redirectUri = "http://localhost:8080/echo";

    public static OAuthClientUri getTestData() {
        OAuthClientUri oAuthClientUri = new OAuthClientUri();
        oAuthClientUri.setSeq(seq);
        oAuthClientUri.setClientSeq(clientId);
        oAuthClientUri.setRedirectUri(redirectUri);
        return oAuthClientUri;
    }
}
