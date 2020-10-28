package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.OAuthClientUser;

import java.util.Date;

public class TestOAuthClientUser {
    public static final long seq = 1L;
    public static final long userSeq = 1L;
    public static final String clientUserId = "bfd0f660-cdb4-494f-9a88-63fee3b8ee30";
    public static final long clientSeq = 1L;
    public static final Date createdDate = new Date(1603359529827L); //2020-10-22T09:38:49.827Z


    public static OAuthClientUser getTestData() {
        OAuthClientUser oAuthClientUser = new OAuthClientUser();
        oAuthClientUser.setSeq(seq);
        oAuthClientUser.setUserSeq(userSeq);
        oAuthClientUser.setClientUserId(clientUserId);
        oAuthClientUser.setClientSeq(clientSeq);
        oAuthClientUser.setCreatedDate(createdDate);
        return oAuthClientUser;
    }
}
