package com.example.oauth2example.vo;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.*;

@Data
public class OAuthIdToken {
    private String jti;
    private String sub;
    private String aud;
    private List<String> scope;
    private String id;
    private String nickname;
    private int type;
    private Date createdDate;
    private Date expireDate;
    private String atHash;
    @JsonIgnore
    private String nonce;

    public static OAuthIdToken create(OAuthUserInfo userInfo, OAuthRefreshToken oAuthRefreshToken, String atHash) {
        OAuthIdToken oAuthIdToken = new OAuthIdToken();
        oAuthIdToken.setJti(UUID.randomUUID().toString());
        oAuthIdToken.setSub(userInfo.getClientUserId());
        oAuthIdToken.setAud(oAuthRefreshToken.getClientId());
        Set<String> scope = new HashSet<>();
        scope.add("open_id");
        scope.add("profile");
        if (oAuthRefreshToken.getScope() != null && !oAuthRefreshToken.getScope().isEmpty()) {
            scope.addAll(Arrays.asList(oAuthRefreshToken.getScope().split(" ")));
        }
        oAuthIdToken.setScope(new ArrayList<>(scope));
        oAuthIdToken.setId(userInfo.getId());
        oAuthIdToken.setNickname(userInfo.getNickname());
        oAuthIdToken.setType(userInfo.getType());
        oAuthIdToken.setAtHash(atHash);
        oAuthIdToken.setNonce(oAuthRefreshToken.getNonce());
        return oAuthIdToken;
    }

    public long getExpiresIn() {
        return (long) Math.floor((this.getExpireDate().getTime() - new Date().getTime()) / 1000.0);
    }
}
