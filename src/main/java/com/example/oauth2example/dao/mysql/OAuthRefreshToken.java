package com.example.oauth2example.dao.mysql;

import com.example.oauth2example.utils.DateUtil;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class OAuthRefreshToken {
    private Long seq;
    private String jti;
    private Long userSeq;
    private String userId;
    private Long clientSeq;
    private String clientId;
    private String scope;
    private String nonce;
    private Date accessAt;
    private Date createdDate;
    private Date expireDate;
    private Integer type;

    public static OAuthRefreshToken create(OAuthClient oAuthClient, long userSeq, int type, String userId, String scope, String nonce) {
        OAuthRefreshToken vo = new OAuthRefreshToken();
        vo.setJti(UUID.randomUUID().toString());
        vo.setClientSeq(oAuthClient.getSeq());
        vo.setClientId(oAuthClient.getClientId());
        vo.setUserSeq(userSeq);
        vo.setUserId(userId);
        vo.setType(type);
        vo.setCreatedDate(DateUtil.clearMillisecond(new Date()));
        vo.setAccessAt(vo.getCreatedDate());
        vo.setScope(scope);
        vo.setNonce(nonce);

        return vo;
    }
}
