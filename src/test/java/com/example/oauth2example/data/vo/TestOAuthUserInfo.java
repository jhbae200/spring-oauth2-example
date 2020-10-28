package com.example.oauth2example.data.vo;

import com.example.oauth2example.data.dao.TestOAuthClient;
import com.example.oauth2example.data.dao.TestOAuthClientUser;
import com.example.oauth2example.data.dao.TestUser;
import com.example.oauth2example.vo.OAuthUserInfo;

public class TestOAuthUserInfo {
    public static long userSeq = TestUser.seq;
    public static String id = TestUser.id;
    public static String nickname = TestUser.nickname;
    public static int type = TestUser.type;
    public static long clientUserSeq = TestOAuthClientUser.seq;
    public static String clientUserId = TestOAuthClientUser.clientUserId;
    public static long clientSeq = TestOAuthClient.seq;

    public static OAuthUserInfo getTestData() {
        OAuthUserInfo oAuthUserInfo = new OAuthUserInfo();
        oAuthUserInfo.setUserSeq(userSeq);
        oAuthUserInfo.setId(id);
        oAuthUserInfo.setNickname(nickname);
        oAuthUserInfo.setType(type);
        oAuthUserInfo.setClientUserSeq(clientUserSeq);
        oAuthUserInfo.setClientUserId(clientUserId);
        oAuthUserInfo.setClientSeq(clientSeq);
        return oAuthUserInfo;
    }
}
