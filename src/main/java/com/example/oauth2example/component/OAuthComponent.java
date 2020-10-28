package com.example.oauth2example.component;

import com.example.oauth2example.dao.mysql.OAuthClient;
import com.example.oauth2example.dao.mysql.OAuthClientUri;
import com.example.oauth2example.jwt.Claims;
import com.example.oauth2example.jwt.JwtBuilder;
import com.example.oauth2example.repository.mysql.OAuthClientRepository;
import com.example.oauth2example.repository.mysql.OAuthClientUriRepository;
import com.example.oauth2example.utils.DateUtil;
import com.example.oauth2example.vo.OAuthAccessToken;
import com.example.oauth2example.vo.OAuthIdToken;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.BadJWTException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthComponent {
    private static final int TOKEN_EXPIRE_TIME = 3600; // default 3600
    private final JwtBuilder jwtBuilder;
    private final OAuthClientRepository oAuthClientRepository;
    private final OAuthClientUriRepository oAuthClientUriRepository;
    @Value("${domain}")
    @Setter
    private String domain;

    /**
     * OAuthAccessToken 으로 jwt string 만드는 함수.
     * createdDate, expireDate 미설정시 기본값 삽입.
     *
     * @param oAuthAccessToken access token dto
     * @return jwt string
     * @throws JOSEException jwt making error
     */
    public String createAccessToken(OAuthAccessToken oAuthAccessToken) throws JOSEException {
        if (oAuthAccessToken.getCreatedDate() == null) {
            oAuthAccessToken.setCreatedDate(DateUtil.clearMillisecond(new Date()));
        }
        if (oAuthAccessToken.getExpireAt() == null) {
            oAuthAccessToken.setExpireAt(new DateTime(oAuthAccessToken.getCreatedDate()).plusSeconds(TOKEN_EXPIRE_TIME).toDate());
        }
        return jwtBuilder.createToken(this.getJWTClaims(oAuthAccessToken));
    }

    private JWTClaimsSet getJWTClaims(OAuthAccessToken oAuthAccessToken) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(domain)
                .subject(oAuthAccessToken.getSub())
                .audience(oAuthAccessToken.getAud())
                .jwtID(oAuthAccessToken.getJti())
                .claim(Claims.SCOPE, oAuthAccessToken.getScope())
                .claim(Claims.TYPE, oAuthAccessToken.getType())
                .issueTime(oAuthAccessToken.getCreatedDate())
                .expirationTime(oAuthAccessToken.getExpireAt());
        if (oAuthAccessToken.getNonce() != null) {
            builder.claim(Claims.NONCE, oAuthAccessToken.getNonce());
        }
        return builder.build();
    }


    public String createIdToken(OAuthIdToken oAuthIdToken) throws JOSEException {
        if (oAuthIdToken.getCreatedDate() == null) {
            oAuthIdToken.setCreatedDate(DateUtil.clearMillisecond(new Date()));
        }
        if (oAuthIdToken.getExpireDate() == null) {
            oAuthIdToken.setExpireDate(new DateTime(oAuthIdToken.getCreatedDate()).plusSeconds(TOKEN_EXPIRE_TIME).toDate());
        }

        return jwtBuilder.createToken(this.getJWTClaims(oAuthIdToken));
    }

    private JWTClaimsSet getJWTClaims(OAuthIdToken oAuthIdToken) {
        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .issuer(domain)
                .jwtID(oAuthIdToken.getJti())
                .subject(oAuthIdToken.getSub())
                .audience(oAuthIdToken.getAud())
                .claim(Claims.ID, oAuthIdToken.getId())
                .claim(Claims.NICKNAME, oAuthIdToken.getNickname())
                .claim(Claims.TYPE, oAuthIdToken.getType())
                .issueTime(oAuthIdToken.getCreatedDate())
                .expirationTime(oAuthIdToken.getExpireDate());
        if (oAuthIdToken.getScope() != null) {
            builder.claim(Claims.SCOPE, oAuthIdToken.getScope());
        }
        if (oAuthIdToken.getAtHash() != null) {
            builder.claim(Claims.AT_HASH, oAuthIdToken.getAtHash());
        }
        if (oAuthIdToken.getNonce() != null) {
            builder.claim(Claims.NONCE, oAuthIdToken.getNonce());
        }
        return builder.build();
    }

    public String getATHash(String accessToken) {
        JWSHeader jwsHeader = jwtBuilder.getDefaultJwsHeader();
        String hashAlg = null;
        JWSAlgorithm signingAlg = jwsHeader.getAlgorithm();
        if (signingAlg.equals(JWSAlgorithm.HS256) || signingAlg.equals(JWSAlgorithm.ES256) || signingAlg.equals(JWSAlgorithm.RS256) || signingAlg.equals(JWSAlgorithm.PS256)) {
            hashAlg = "SHA-256";
        } else if (signingAlg.equals(JWSAlgorithm.ES384) || signingAlg.equals(JWSAlgorithm.HS384) || signingAlg.equals(JWSAlgorithm.RS384) || signingAlg.equals(JWSAlgorithm.PS384)) {
            hashAlg = "SHA-384";
        } else if (signingAlg.equals(JWSAlgorithm.ES512) || signingAlg.equals(JWSAlgorithm.HS512) || signingAlg.equals(JWSAlgorithm.RS512) || signingAlg.equals(JWSAlgorithm.PS512)) {
            hashAlg = "SHA-512";
        }
        if (hashAlg != null) {
            try {
                MessageDigest hasher = MessageDigest.getInstance(hashAlg);
                hasher.reset();
                hasher.update(accessToken.getBytes());

                byte[] hashBytes = hasher.digest();
                byte[] hashBytesLeftHalf = Arrays.copyOf(hashBytes, hashBytes.length / 2);
                return Base64URL.encode(hashBytesLeftHalf).toString();
            } catch (NoSuchAlgorithmException e) {
                log.error("No such algorithm error: ", e);
            }
        }
        return null;
    }

    public JWTClaimsSet parsingAndValidateAccessToken(String token) {
        return this.getJWTClaimSet(token);
    }

    /**
     * 토큰 검증, 토큰 파싱
     * 실패하면 401 (jwt 토큰 자체가 맞지 않기 때문)
     *
     * @param token jwt token
     * @return claimsSet
     */
    private JWTClaimsSet getJWTClaimSet(String token) {
        JWTClaimsSet jwtClaimsSet = null;
        try {
            jwtClaimsSet = jwtBuilder.parsingAndVerifyJWT(token);
        } catch (ParseException | JOSEException | BadJWTException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid token", e);
        }

        return jwtClaimsSet;
    }

    public OAuthClient validateClientAndRedirectUri(String clientId, String redirectUri) {
        OAuthClient oAuthClient = oAuthClientRepository.select1stByClientId(clientId);
        Assert.notNull(oAuthClient, "Not Found client_id");
        OAuthClientUri oAuthClientUri = oAuthClientUriRepository.select1stByClientSeqAndRedirectUri(oAuthClient.getSeq(), redirectUri);
        Assert.notNull(oAuthClientUri, "Not Match redirect_uri");
        return oAuthClient;
    }
}
