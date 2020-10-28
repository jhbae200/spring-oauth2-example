package com.example.oauth2example.component;

import com.example.oauth2example.data.TestStringData;
import com.example.oauth2example.data.dao.*;
import com.example.oauth2example.jwt.JwtBuilder;
import com.example.oauth2example.repository.mysql.OAuthClientRepository;
import com.example.oauth2example.repository.mysql.OAuthClientUriRepository;
import com.example.oauth2example.repository.mysql.OAuthJwtRsaKeyRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;


@ExtendWith(MockitoExtension.class)
class OAuthComponentTest {
    private static final String testAccessToken = "eyJraWQiOiJ0ZXN0UnNhS2V5IiwiYWxnIjoiUlMyNTYifQ.eyJhdF9oYXNoIjoibnpKa" +
            "GJvY2U3bVZrLVc4Q2p5QlJUdyIsInN1YiI6ImJmZDBmNjYwLWNkYjQtNDk0Zi05YTg4LTYzZmVlM2I4ZWUzMCIsImF1ZCI6InRlc3RDbGl" +
            "lbnRJZCIsInNjb3BlIjpbIm9wZW5faWQiLCJwcm9maWxlIl0sImlzcyI6Imh0dHA6XC9cL2xvY2FsaG9zdDo4MDgwIiwibmlja25hbWUiO" +
            "iJ0ZXN0IiwiaWQiOiJ0ZXN0QGV4YW1wbGUuY29tIiwidHlwZSI6MSwiZXhwIjoxNjAzMzY2NzI5LCJpYXQiOjE2MDMzNTk1MjksIm5vbmN" +
            "lIjoibmI5MTJlY2ExNTg5dGowMWEiLCJqdGkiOiIzNkQ1OTRCQy1CQzI0LTQ4N0UtQjAxQi02RkRFQUQ3QjgyMTMifQ.CMixZKmQ-8zcMU" +
            "TAAFtz755nRrqY3Sgd9G8CTKISQUZVd5SmVuTR8TuXM-iPxPqSVTRpG_V0lHHcIpUVkMp2gTd5Bxs4cFbIweE-NoOhEtFk_ujNLSUew99D" +
            "vVq3Pq8uWYzm4KUMdg96byE_16-padXdOeH7XXV7IU15RY2zS3KI1N9HT3b5PVRkGBMA415mC9mU1pe0M0h4paQhattiMiaXxfdQ9YYopJ" +
            "x-ecznRBZO2USrOFSBkJqapL0IBkLv76hm29v6sFWmucDB_54Uj4Bc54QycZJj9F9kFjLn-3liG9UhBSu6upOrWEcNVWIoL6Y7yAGN41dY" +
            "y8ZrHeICaA";
    @Mock
    private OAuthJwtRsaKeyRepository oAuthJwtRsaKeyRepository;
    @Mock
    private OAuthClientRepository oAuthClientRepository;
    @Mock
    private OAuthClientUriRepository oAuthClientUriRepository;
    private OAuthComponent oAuthComponent;
    private JwtBuilder jwtBuilder;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, IOException {
        Mockito.when(oAuthJwtRsaKeyRepository.selectAll()).thenReturn(Collections.singletonList(TestOAuthJwtRasKey.getTestData()));
        jwtBuilder = new JwtBuilder(TestStringData.domain, oAuthJwtRsaKeyRepository);
        oAuthComponent = new OAuthComponent(jwtBuilder, oAuthClientRepository, oAuthClientUriRepository);
        oAuthComponent.setDomain(TestStringData.domain);
    }

    @Test
    void createAccessToken() throws JOSEException {
        Assertions.assertNotNull(oAuthComponent.createAccessToken(TestOAuthAccessToken.getTestData()));
    }

    @Test
    void createIdToken() throws JOSEException {
        Assertions.assertNotNull(oAuthComponent.createIdToken(TestOAuthIdToken.getTestData()));
    }

    @Test
    void getATHash() {
        Assertions.assertNotNull(oAuthComponent.getATHash(testAccessToken));
    }

    @Test
    void parsingAndValidateAccessToken() throws JOSEException {
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            oAuthComponent.parsingAndValidateAccessToken(testAccessToken);
        });
        String testAccessToken2 = oAuthComponent.createAccessToken(TestOAuthAccessToken.getTestData2());
        JWTClaimsSet claim = oAuthComponent.parsingAndValidateAccessToken(testAccessToken2);
        Assertions.assertEquals(TestOAuthAccessToken.sub, claim.getSubject());
    }

    @Test
    void validateClientAndRedirectUri() {
        Mockito.when(oAuthClientRepository.select1stByClientId(TestOAuthClient.clientId)).thenReturn(TestOAuthClient.getTestData());
        Mockito.when(oAuthClientUriRepository.select1stByClientSeqAndRedirectUri(TestOAuthClient.seq, TestOAuthClientUri.redirectUri)).thenReturn(TestOAuthClientUri.getTestData());
        Assertions.assertDoesNotThrow(() -> oAuthComponent.validateClientAndRedirectUri(TestOAuthClient.clientId, TestOAuthClientUri.redirectUri));
    }
}