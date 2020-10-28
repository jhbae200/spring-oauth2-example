package com.example.oauth2example.vo;

import com.example.oauth2example.dto.OAuthPageDTO;
import com.example.oauth2example.oauth2.CodeChallengeType;
import com.example.oauth2example.utils.RandomStr;
import lombok.Data;

import java.security.SecureRandom;
import java.util.Date;

@Data
public class OAuthCode {
    private Long clientSeq;
    private String userId;
    private int userType;
    private String code;
    private String redirectUri;
    private String state;
    private Date createdDate;
    private String codeChallenge;
    private String scope;
    private CodeChallengeType codeChallengeType;
    private String nonce;

    public static OAuthCode create(Long clientSeq, String userId, int userType, OAuthPageDTO.AuthRequest authRequest) {
        OAuthCode vo = new OAuthCode();
        vo.setClientSeq(clientSeq);
        vo.setCode(new RandomStr(16, new SecureRandom()).nextString());
        vo.setRedirectUri(authRequest.getRedirect_uri());
        vo.setScope(authRequest.getScope());
        vo.setState(authRequest.getState());
        vo.setNonce(authRequest.getNonce());
        vo.setUserId(userId);
        vo.setUserType(userType);
        if (authRequest.getCode_challenge() != null && !authRequest.getCode_challenge().isEmpty()) {
            vo.setCodeChallenge(authRequest.getCode_challenge());
            vo.setChallengeType(authRequest.getCode_challenge_method());
        }
        vo.setScope(authRequest.getScope());
        vo.setCreatedDate(new Date());
        return vo;
    }

    public void setChallengeType(String method) {
        this.codeChallengeType = CodeChallengeType.findType(method);
    }

    public void setChallengeType(int type) {
        this.codeChallengeType = CodeChallengeType.findType(type);
    }

}
