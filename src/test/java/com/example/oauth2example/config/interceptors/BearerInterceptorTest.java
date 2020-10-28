package com.example.oauth2example.config.interceptors;

import com.example.oauth2example.component.OAuthComponent;
import com.example.oauth2example.data.dao.TestOAuthAccessToken;
import com.example.oauth2example.data.dao.TestOAuthJwtRasKey;
import com.example.oauth2example.jwt.JwtBuilder;
import com.example.oauth2example.repository.mysql.OAuthClientRepository;
import com.example.oauth2example.repository.mysql.OAuthClientUriRepository;
import com.example.oauth2example.repository.mysql.OAuthJwtRsaKeyRepository;
import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class BearerInterceptorTest {
    @Mock
    private OAuthJwtRsaKeyRepository oAuthJwtRsaKeyRepository;
    @Mock
    private OAuthClientRepository oAuthClientRepository;
    @Mock
    private OAuthClientUriRepository oAuthClientUriRepository;

    @Test
    void preHandle() throws NoSuchAlgorithmException, InvalidKeySpecException, JOSEException, IOException {
        Mockito.when(oAuthJwtRsaKeyRepository.selectAll()).thenReturn(Collections.singletonList(TestOAuthJwtRasKey.getTestData()));
        JwtBuilder jwtBuilder = new JwtBuilder("http://localhost:8080", oAuthJwtRsaKeyRepository);
        OAuthComponent oAuthComponent = new OAuthComponent(jwtBuilder, oAuthClientRepository, oAuthClientUriRepository);
        oAuthComponent.setDomain("http://localhost:8080");

        BearerInterceptor bearerInterceptor = new BearerInterceptor(oAuthComponent);
        // Test Case 1

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();

            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthComponent.createAccessToken(TestOAuthAccessToken.getTestData()));

            bearerInterceptor.preHandle(request, response, null);
        });


        Assertions.assertDoesNotThrow(() -> {
            // Test Case 2
            MockHttpServletRequest request = new MockHttpServletRequest();
            MockHttpServletResponse response = new MockHttpServletResponse();

            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();

            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + oAuthComponent.createAccessToken(TestOAuthAccessToken.getTestData2()));

            Assertions.assertTrue(bearerInterceptor.preHandle(request, response, null));
        });
    }
}