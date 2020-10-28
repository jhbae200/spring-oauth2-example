package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.OAuthClient;

import java.util.Date;

public class TestOAuthClient {
    public static final long seq = 1L;
    public static final String clientId = "testClientId";
    public static final String clientSecret = "testClientSecret";
    public static final String serviceName = "bookshelf";
    public static final Date createdDate = new Date(1603359529827L); //2020-10-22T09:38:49.827Z

    public static OAuthClient getTestData() {
        OAuthClient oAuthClient = new OAuthClient();
        oAuthClient.setSeq(seq);
        oAuthClient.setClientId(clientId);
        oAuthClient.setClientSecret(clientSecret);
        oAuthClient.setServiceName(serviceName);
        oAuthClient.setCreatedDate(createdDate);
        return oAuthClient;
    }
}
