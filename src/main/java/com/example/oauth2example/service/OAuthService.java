package com.example.oauth2example.service;

import com.example.oauth2example.component.OAuthClientUserComponent;
import com.example.oauth2example.component.OAuthComponent;
import com.example.oauth2example.component.OAuthRefreshTokenComponent;
import com.example.oauth2example.component.UserComponent;
import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.example.oauth2example.dto.AuthDTO;
import com.example.oauth2example.exception.CodeException;
import com.example.oauth2example.exception.DBException;
import com.example.oauth2example.oauth2.CodeChallengeType;
import com.example.oauth2example.repository.mysql.OAuthClientRepository;
import com.example.oauth2example.dao.mysql.OAuthClient;
import com.example.oauth2example.dao.mysql.OAuthClientUser;
import com.example.oauth2example.dao.mysql.User;
import com.example.oauth2example.repository.mysql.OAuthClientUserRepository;
import com.example.oauth2example.repository.redis.OAuthCodeRepository;
import com.example.oauth2example.utils.SHA256Utils;
import com.example.oauth2example.vo.*;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {
    private final OAuthClientRepository oAuthClientRepository;
    private final UserComponent userComponent;
    private final OAuthClientUserComponent oAuthClientUserComponent;
    private final OAuthRefreshTokenComponent oAuthRefreshTokenComponent;
    private final OAuthComponent oAuthComponent;
    private final OAuthClientUserRepository oAuthClientUserRepository;
    private final OAuthCodeRepository oAuthCodeRepository;
    private final Hashids hashids;
    @Value("${oauth2.codeChar}")
    private String codeChar;
    @Value("${oauth2.codeStart}")
    private String codeStart;

    public AuthDTO.TokenResponse postTokenPassword(AuthDTO.TokenPasswordRequest tokenRequest, BindingResult bindingResult) throws BindException, DBException, JOSEException {
        OAuthClient oAuthClient = oAuthClientRepository.select1stByClientId(tokenRequest.getClient_id());
        Assert.notNull(oAuthClient, "Not Found client_id");

        if (!tokenRequest.getClient_secret().equals(oAuthClient.getClientSecret()))
            throw new IllegalArgumentException("invalid client");

        User user = userComponent.usernamePasswordCheck(tokenRequest.getUsername(), tokenRequest.getPassword(), bindingResult);

        return response(oAuthClient, user, tokenRequest.getScope(), tokenRequest.getNonce());
    }

    private AuthDTO.TokenResponse response(OAuthClient oAuthClient, User user, String scope, String nonce) throws DBException, JOSEException {
        OAuthClientUser oAuthClientUser = oAuthClientUserComponent.getOrCreateOAuthClientUser(oAuthClient, user);
        OAuthUserInfo userInfo = OAuthUserInfo.create(user, oAuthClientUser);
        OAuthRefreshToken oAuthRefreshToken = oAuthRefreshTokenComponent.createRefreshToken(oAuthClient, userInfo, scope, nonce);
        return response(userInfo, oAuthRefreshToken);
    }

    public AuthDTO.TokenResponse response(OAuthUserInfo userInfo, OAuthRefreshToken oAuthRefreshToken) throws JOSEException {
        OAuthAccessToken oAuthAccessToken = OAuthAccessToken.create(userInfo, oAuthRefreshToken);
        String accessToken = oAuthComponent.createAccessToken(oAuthAccessToken);
        String atHash = oAuthComponent.getATHash(accessToken);
        OAuthIdToken oAuthIdToken = OAuthIdToken.create(userInfo, oAuthRefreshToken, atHash);
        String idToken = oAuthComponent.createIdToken(oAuthIdToken);
        return AuthDTO.TokenResponse.create(accessToken, oAuthRefreshToken.getJti(), oAuthAccessToken.getExpiresIn(), idToken);
    }

    public AuthDTO.TokenResponse postTokenRefreshToken(AuthDTO.TokenRefreshRequest tokenRequest, BindingResult bindingResult) throws BindException, JOSEException {
        OAuthClient oAuthClient = oAuthClientRepository.select1stByClientId(tokenRequest.getClient_id());
        Assert.notNull(oAuthClient, "invalid client id");

        //TODO: redis cache not possible hit..
        OAuthRefreshToken oAuthRefreshToken = oAuthRefreshTokenComponent.getRefreshToken(tokenRequest.getRefresh_token(), bindingResult);
        OAuthUserInfo oAuthUserInfo = oAuthClientUserRepository.select1stByClientAllUserId(oAuthRefreshToken.getUserId(), oAuthRefreshToken.getType());
        Assert.notNull(oAuthUserInfo, "user failed");
        if (!oAuthUserInfo.getClientSeq().equals(oAuthClient.getSeq())) {
            throw new IllegalArgumentException("invalid client");
        }

        return response(oAuthUserInfo, oAuthRefreshToken);
    }

    public void postRevoke(AuthDTO.TokenRevokeRequest tokenRevokeRequest) {
        if (tokenRevokeRequest.getToken_type_hint() != null) {
            if (!"refresh_token".equals(tokenRevokeRequest.getToken_type_hint())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unsupported_token_type");
            }
        }
        oAuthRefreshTokenComponent.revokeRefreshToken(tokenRevokeRequest.getToken());
    }

    public AuthDTO.TokenResponse postTokenAuthCode(AuthDTO.TokenCodeRequest tokenRequest) throws CodeException, NoSuchAlgorithmException, DBException, JOSEException {
        SecureCode secureCode = parsingCode(tokenRequest.getCode());

        OAuthClient oAuthClient = oAuthClientRepository.select1stByClientId(tokenRequest.getClient_id());
        Assert.notNull(oAuthClient, "Not Found client_id");
        if (oAuthClient.getSeq() != secureCode.getClientSeq()) {
            throw new IllegalArgumentException("invalid client");
        }

        OAuthCode oAuthCode = validateCode(tokenRequest, secureCode, oAuthClient); // 인증


        User user = userComponent.getUserById(oAuthCode.getUserId(), oAuthCode.getUserType());
        Assert.notNull(user, "invalid user");

        return response(oAuthClient, user, oAuthCode.getScope(), oAuthCode.getNonce());
    }

    private SecureCode parsingCode(String code) throws CodeException {
        if (!code.startsWith(codeStart)) {
            throw new CodeException("invalid code");
        }

        code = code.replaceFirst("^" + codeStart, "");

        String[] splitCode = code.split(codeChar);
        if (splitCode.length != 2) {
            throw new CodeException("invalid code");
        }

        long[] clientSeq = hashids.decode(splitCode[0]);
        if (clientSeq.length != 1) {
            throw new CodeException("invalid code");
        }
        return new SecureCode(splitCode[1], (int) clientSeq[0]);
    }

    private OAuthCode validateCode(AuthDTO.TokenCodeRequest tokenRequest, SecureCode secureCode, OAuthClient oAuthClient) throws NoSuchAlgorithmException {
        OAuthCode oAuthCode = oAuthCodeRepository.get(secureCode.getClientSeq(), secureCode.getCode());
        Assert.notNull(oAuthCode, "Not Found code");
        oAuthCodeRepository.delete(oAuthCode);

        if (oAuthCode.getCodeChallenge() != null) { // code_verifier 로 인증
            this.checkCodeVerifier(oAuthCode.getCodeChallengeType(), oAuthCode.getCodeChallenge(), tokenRequest.getCode_verifier());
        } else { // client_secret 으로 인증
            if (!oAuthClient.getClientSecret().equals(tokenRequest.getClient_secret())) {
                throw new IllegalArgumentException("invalid client");
            }
        }

        return oAuthCode;
    }

    private boolean checkCodeVerifier(CodeChallengeType type, String codeChallenge, String codeVerifier) throws NoSuchAlgorithmException {
        log.debug("challengeType: {}, codeVerifier: {}", type, codeVerifier);
        switch (type) {
            case PLAIN:
                if (!codeChallenge.equals(codeVerifier))
                    throw new IllegalArgumentException("invalid code_verifier");
                break;
            case SHA256:
                if (!SHA256Utils.matchSHA256(codeVerifier, codeChallenge))
                    throw new IllegalArgumentException("invalid code_verifier");
                break;
            default:
                throw new IllegalArgumentException("invalid code_verifier");
        }

        return true;
    }
}
