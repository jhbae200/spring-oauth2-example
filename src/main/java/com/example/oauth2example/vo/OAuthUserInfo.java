package com.example.oauth2example.vo;

import com.example.oauth2example.dao.mysql.OAuthClientUser;
import com.example.oauth2example.dao.mysql.User;
import lombok.Data;


@Data
public class OAuthUserInfo {
    //User info
    private Long userSeq;
    private String id;
    private String nickname;
    private int type;
    //OAuth info
    private Long clientUserSeq;
    private String clientUserId;
    private Long clientSeq;

    public static OAuthUserInfo create(User user, OAuthClientUser oAuthClientUser) {
        OAuthUserInfo oAuthUserInfo = new OAuthUserInfo();
        oAuthUserInfo.setUserSeq(user.getSeq());
        oAuthUserInfo.setId(user.getId());
        oAuthUserInfo.setNickname(user.getNickname());
        oAuthUserInfo.setType(user.getType());
        oAuthUserInfo.setClientUserSeq(oAuthClientUser.getSeq());
        oAuthUserInfo.setClientUserId(oAuthClientUser.getClientUserId());
        oAuthUserInfo.setClientSeq(oAuthClientUser.getClientSeq());
        return oAuthUserInfo;
    }
}
