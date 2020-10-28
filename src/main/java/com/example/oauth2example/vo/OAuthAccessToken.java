package com.example.oauth2example.vo;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.*;

@Data
public class OAuthAccessToken {
    private String jti;
    private String sub;
    private String aud;
    private List<String> scope;
    private Date createdDate;
    private Date expireAt;
    private int type;
    @JsonIgnore
    private String nonce;

    /**
     * make OAuthAccessToken dto process
     *
     * @param userInfo          User table vo
     * @param oAuthRefreshToken refresh info (aud, rid, scope, nonce)
     * @return OAuthAccessToken dto
     */
    public static OAuthAccessToken create(OAuthUserInfo userInfo, OAuthRefreshToken oAuthRefreshToken) {
        OAuthAccessToken oAuthAccessToken = new OAuthAccessToken();
        oAuthAccessToken.setJti(UUID.randomUUID().toString());
        oAuthAccessToken.setSub(userInfo.getClientUserId());
        oAuthAccessToken.setAud(oAuthRefreshToken.getClientId());
        Set<String> scope = new HashSet<>();
        scope.add("open_id");
        scope.add("profile");
        if (oAuthRefreshToken.getScope() != null && !oAuthRefreshToken.getScope().isEmpty()) {
            scope.addAll(Arrays.asList(oAuthRefreshToken.getScope().split(" ")));
        }
        oAuthAccessToken.setScope(new ArrayList<>(scope));
        oAuthAccessToken.setNonce(oAuthRefreshToken.getNonce());
        oAuthAccessToken.setType(userInfo.getType());
        return oAuthAccessToken;
    }


    public long getExpiresIn() {
        return (long) Math.floor((this.getExpireAt().getTime() - new Date().getTime()) / 1000.0);
    }
}
