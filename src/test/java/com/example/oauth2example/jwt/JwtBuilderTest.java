package com.example.oauth2example.jwt;

import com.example.oauth2example.data.dao.TestOAuthJwtRasKey;
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

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class JwtBuilderTest {
    JwtBuilder jwtBuilder;
    @Mock
    OAuthJwtRsaKeyRepository oAuthJwtRsaKeyRepository;

    @BeforeEach
    void init() throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, IOException {
        Mockito.when(oAuthJwtRsaKeyRepository.selectAll()).thenReturn(Collections.singletonList(TestOAuthJwtRasKey.getTestData()));
        jwtBuilder = new JwtBuilder("http://localhost:8080", oAuthJwtRsaKeyRepository);
    }

    @Test
    void jwtKeyLoad() {
        Mockito.when(oAuthJwtRsaKeyRepository.selectAll()).thenReturn(Collections.singletonList(TestOAuthJwtRasKey.getTestData2()));
        Assertions.assertDoesNotThrow(() -> {
            jwtBuilder.JwtKeyLoad();
        });
    }

    @Test
    void getJwkSet() {
        Assertions.assertNotNull(jwtBuilder.getJwkSet().getKeyByKeyId(TestOAuthJwtRasKey.getTestData().getName()));
    }

    @Test
    void createToken() {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().build();

        Assertions.assertDoesNotThrow(() -> {
            jwtBuilder.createToken(claimsSet);
        });
    }

    @Test
    void getDefaultJwsHeader() {
        Assertions.assertEquals(TestOAuthJwtRasKey.name, jwtBuilder.getDefaultJwsHeader().getKeyID());
    }

    @Test
    void getJwsHeader() {
        Assertions.assertEquals(TestOAuthJwtRasKey.name, jwtBuilder.getJwsHeader(TestOAuthJwtRasKey.name).getKeyID());
    }
}