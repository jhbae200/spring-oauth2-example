package com.example.oauth2example.jwt;

import com.example.oauth2example.dao.mysql.OAuthJwtRsaKey;
import com.example.oauth2example.repository.mysql.OAuthJwtRsaKeyRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.BadJWTException;
import com.nimbusds.jwt.proc.DefaultJWTClaimsVerifier;
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Reader;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.keyValue;


@Slf4j
@Component
public class JwtBuilder {
    private static final int TOKEN_EXPIRE_TIME = 3600; // default 3600
    private final OAuthJwtRsaKeyRepository OAuthJwtRsaKeyRepository;
    private final ConcurrentHashMap<String, Store> keyStoreMap = new ConcurrentHashMap<>();
    private final JWTClaimsSetVerifier claimsSetVerifier;
    private final String domain;
    private String liveKey;

    /**
     * 해당 컴포넌트 로드시 OAuthJwtRsaKey 테이블 쿼리해서 {key_name}으로 {@code keyStoreMap}에 저장.
     *
     * @throws IOException              file io exception
     * @throws InvalidKeySpecException  invalid key spec exception
     * @throws NoSuchAlgorithmException can't load rsa algorithm
     * @throws JOSEException            jose library exception
     */
    public JwtBuilder(@Value("${domain}") String domain, @Autowired OAuthJwtRsaKeyRepository OAuthJwtRsaKeyRepository) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, JOSEException {
        this.OAuthJwtRsaKeyRepository = OAuthJwtRsaKeyRepository;
        List<OAuthJwtRsaKey> OAuthJwtRsaKeys = OAuthJwtRsaKeyRepository.selectAll();
        Assert.notEmpty(OAuthJwtRsaKeys, "no search keys");
        for (OAuthJwtRsaKey OAuthJwtRsaKey : OAuthJwtRsaKeys) {
            Store store = getStore(OAuthJwtRsaKey.getPublicKey(), OAuthJwtRsaKey.getPrivateKey(), OAuthJwtRsaKey.getName());
            keyStoreMap.put(OAuthJwtRsaKey.getName(), store);
            liveKey = OAuthJwtRsaKey.getName();
        }
        this.domain = domain;
        this.claimsSetVerifier = new DefaultJWTClaimsVerifier(new JWTClaimsSet.Builder().issuer(domain).build(),
                new HashSet<>(Arrays.asList("sub", "iat", "exp", "aud", "jti")));

        log.debug("load {} keys. {}", keyStoreMap.size(), keyValue("keys", keyStoreMap.keySet().toArray()));
    }

    private Store getStore(Reader pub, Reader rsa, String key) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, JOSEException {
        RSAKey jwk = new RSAKey.Builder((RSAPublicKey) KeyUtils.readPublicKey(pub, "RSA"))
                .privateKey(KeyUtils.readPrivateKey(rsa, "RSA"))
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(key).build();
        Store store = new Store();
        store.setJwsHeader(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(key).build());
        store.setSigner(new RSASSASigner(jwk));
        store.setVerifier(new RSASSAVerifier(jwk));
        store.setRsaKey(jwk);
        return store;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5) // 5분
    public void JwtKeyLoad() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException, JOSEException {
        List<OAuthJwtRsaKey> OAuthJwtRsaKeys = OAuthJwtRsaKeyRepository.selectAll();
        if (OAuthJwtRsaKeys != null && OAuthJwtRsaKeys.size() > 0) {
            List<String> fileKeyList = OAuthJwtRsaKeys.stream().map(OAuthJwtRsaKey::getName).collect(Collectors.toList());
            List<String> removeKeys = new ArrayList<>();
            List<String> addKeys = new ArrayList<>();
            Enumeration<String> keys2 = keyStoreMap.keys();
            while (keys2.hasMoreElements()) {
                String removeKey = keys2.nextElement();
                if (fileKeyList.indexOf(removeKey) == -1) {
                    keyStoreMap.remove(removeKey);
                    removeKeys.add(removeKey);
                }
            }
            for (OAuthJwtRsaKey OAuthJwtRsaKey : OAuthJwtRsaKeys) {
                if (!keyStoreMap.containsKey(OAuthJwtRsaKey.getName())) {
                    Store store = getStore(OAuthJwtRsaKey.getPublicKey(), OAuthJwtRsaKey.getPrivateKey(), OAuthJwtRsaKey.getName());
                    keyStoreMap.put(OAuthJwtRsaKey.getName(), store);
                    liveKey = OAuthJwtRsaKey.getName();
                    addKeys.add(OAuthJwtRsaKey.getName());
                }
            }
            if (removeKeys.size() > 0 || addKeys.size() > 0) {
                log.debug("removeKeys: {}, addKeys: {}", removeKeys, addKeys);
                log.debug("{} keys. keys: {}", keyStoreMap.size(), keyStoreMap.keySet());
            }
        }
    }

    public String createToken(JWTClaimsSet jwtClaimsSet) throws JOSEException {
        JWSHeader jwsHeader = keyStoreMap.get(liveKey).getJwsHeader();
        Assert.notNull(jwsHeader, liveKey + " jwsHeader is not null");
        JWSSigner signer = keyStoreMap.get(liveKey).getSigner();
        Assert.notNull(signer, liveKey + " signer is not null");

        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
//
////    public OAuthAccessToken parsingAccessToken(String token) throws ParseException, JOSEException, InvalidTokenException {
////        JWTClaimsSet claimsSet = this.parsingAndVerifyJWT(token);
////
////        OAuthAccessToken authAccessToken = new OAuthAccessToken();
////        authAccessToken.setJti(claimsSet.getJWTID());
////        authAccessToken.setSub(claimsSet.getStringClaim(Claims.USER_ID));
////        authAccessToken.setCreatedDate(claimsSet.getIssueTime());
////        authAccessToken.setExpireDate(claimsSet.getExpirationTime());
////
////        return authAccessToken;
////    }
//
    public JWTClaimsSet parsingAndVerifyJWT(String token) throws ParseException, JOSEException, BadJWTException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String key = signedJWT.getHeader().getKeyID();
        JWSVerifier verifier = keyStoreMap.get(key).getVerifier();
        Assert.notNull(verifier, key + " is not registered key.");
        if (!signedJWT.verify(verifier)) throw new BadJWTException("invalid token");
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        claimsSetVerifier.verify(jwtClaimsSet, null);
        return jwtClaimsSet;
    }

    public JWKSet getJwkSet() {
        List<JWK> jwkList = keyStoreMap.keySet().stream().map(s -> keyStoreMap.get(s).getRsaKey()).collect(Collectors.toList());
        return new JWKSet(jwkList);
    }

    public JWSHeader getDefaultJwsHeader() {
        return getJwsHeader(liveKey);
    }

    public JWSHeader getJwsHeader(String keyId) {
        JwtBuilder.Store store = keyStoreMap.get(keyId);
        Assert.notNull(store, "not found store by keyId: "+keyId);
        return store.getJwsHeader();
    }


    @Data
    private static class Store {
        private RSAKey rsaKey;
        private KeyPair keyPair;
        private JWSHeader jwsHeader;
        private JWSSigner signer;
        private JWSVerifier verifier;
    }

}
