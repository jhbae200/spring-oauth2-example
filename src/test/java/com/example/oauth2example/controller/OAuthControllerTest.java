package com.example.oauth2example.controller;

import com.example.oauth2example.ExampleHandlerMapping;
import com.example.oauth2example.config.interceptors.BearerInterceptor;
import com.example.oauth2example.data.TestJwtClaim;
import com.example.oauth2example.data.dao.TestOAuthClient;
import com.example.oauth2example.data.dao.TestOAuthRefreshToken;
import com.example.oauth2example.data.dao.TestUser;
import com.example.oauth2example.data.vo.TestOAuthCode;
import com.example.oauth2example.dto.Parameters;
import com.example.oauth2example.jwt.resolvers.JWTClaimsSetResolver;
import com.example.oauth2example.oauth2.GrantType;
import com.example.oauth2example.repository.redis.OAuthCodeRepository;
import com.example.oauth2example.vo.OAuthCode;
import com.google.gson.Gson;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.hashids.Hashids;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {"spring.datasource.initialization-mode=always"})
@ExtendWith(SpringExtension.class)
class OAuthControllerTest {
    private final JWTClaimsSetResolver jwtClaimsSetResolver = new JWTClaimsSetResolver();
    protected MockMvc mockMvc;
    @MockBean
    private BearerInterceptor bearerInterceptor;
    @Autowired
    private Validator validator;
    @Autowired
    private OAuthController oAuthController;
    @Autowired
    private OAuthCodeRepository oAuthCodeRepository;
    @Autowired
    private Hashids hashids;
    @Value("${oauth2.codeChar}")
    private String codeChar;
    @Value("${oauth2.codeStart}")
    private String codeStart;

    @BeforeEach
    public void setup() {
        Mockito.when(bearerInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).then(invocation -> {
            HttpServletRequest request = invocation.getArgument(0, HttpServletRequest.class);
            request.setAttribute(ExampleHandlerMapping.JWT_CLAIM_SET, TestJwtClaim.getTestData());
            return true;
        }).thenReturn(true);
        mockMvc = MockMvcBuilders
                .standaloneSetup(oAuthController)
                .setValidator(validator)
                .setCustomArgumentResolvers(jwtClaimsSetResolver)
                .addInterceptors(bearerInterceptor).build();
    }

    @Test
    void postToken() throws Exception {
        mockMvc.perform(post("/oauth2/v1/token")).andExpect(status().isBadRequest());
    }

    @Test
    void postTokenPassword() throws Exception {
        mockMvc.perform(post("/oauth2/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(Parameters.GRANT_TYPE, GrantType.PASSWORD)
                .param(Parameters.CLIENT_ID, TestOAuthClient.clientId)
                .param(Parameters.CLIENT_SECRET, TestOAuthClient.clientSecret)
                .param(Parameters.USERNAME, TestUser.id)
                .param(Parameters.PASSWORD, TestUser.originalPassword)
        ).andExpect(status().isOk()).andExpect(ResultMatcher.matchAll(
                MockMvcResultMatchers.jsonPath("access_token").exists(),
                MockMvcResultMatchers.jsonPath("token_type").value("Bearer"),
                MockMvcResultMatchers.jsonPath("expires_in").exists(),
                MockMvcResultMatchers.jsonPath("refresh_token").exists(),
                MockMvcResultMatchers.jsonPath("id_token").exists()
        ));
    }

    @Test
    void postTokenAuthCode() throws Exception {
        OAuthCode oAuthCode = TestOAuthCode.getTestDataS256();
        oAuthCodeRepository.save(oAuthCode);
        String code = String.format(codeStart + "%s" + codeChar + "%s", hashids.encode(oAuthCode.getClientSeq()), oAuthCode.getCode());

        mockMvc.perform(post("/oauth2/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(Parameters.GRANT_TYPE, GrantType.AUTHORIZATION_CODE)
                .param(Parameters.CLIENT_ID, TestOAuthClient.clientId)
                .param(Parameters.CODE, code)
                .param(Parameters.CODE_VERIFIER, TestOAuthCode.codeChallenge)
        ).andExpect(status().isOk()).andExpect(ResultMatcher.matchAll(
                MockMvcResultMatchers.jsonPath("access_token").exists(),
                MockMvcResultMatchers.jsonPath("token_type").value("Bearer"),
                MockMvcResultMatchers.jsonPath("expires_in").exists(),
                MockMvcResultMatchers.jsonPath("refresh_token").exists(),
                MockMvcResultMatchers.jsonPath("id_token").exists()
        )).andExpect(result -> {
            Gson gson = new Gson();
            Map json = gson.fromJson(result.getResponse().getContentAsString(), Map.class);
            JWTClaimsSet claimsSet = SignedJWT.parse((String) json.get("access_token")).getJWTClaimsSet();
            Assertions.assertEquals(oAuthCode.getNonce(),claimsSet.getStringClaim("nonce"));
        });
    }

    @Test
    void postTokenRefreshToken() throws Exception {
        mockMvc.perform(post("/oauth2/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param(Parameters.GRANT_TYPE, GrantType.REFRESH_TOKEN)
                .param(Parameters.CLIENT_ID, TestOAuthClient.clientId)
                .param(Parameters.REFRESH_TOKEN, TestOAuthRefreshToken.jti)
        ).andExpect(status().isOk()).andExpect(ResultMatcher.matchAll(
                MockMvcResultMatchers.jsonPath("access_token").exists(),
                MockMvcResultMatchers.jsonPath("token_type").value("Bearer"),
                MockMvcResultMatchers.jsonPath("expires_in").exists(),
                MockMvcResultMatchers.jsonPath("refresh_token").exists(),
                MockMvcResultMatchers.jsonPath("id_token").exists()
        ));
    }

    @Test
    void getUserInfoJson() throws Exception {
        mockMvc.perform(get("/oauth2/v1/userinfo")).andExpect(status().isOk());
    }

    @Test
    void postRevoke() throws Exception {
        mockMvc.perform(post("/oauth2/v1/revoke")
                .param(Parameters.TOKEN, TestOAuthRefreshToken.jti2)
                .param(Parameters.TOKEN_TYPE_HINT, "refresh_token")
        ).andExpect(status().isOk());

        mockMvc.perform(post("/oauth2/v1/revoke")
                .param(Parameters.TOKEN, TestOAuthRefreshToken.jti)
                .param(Parameters.TOKEN_TYPE_HINT, "access_token")
        ).andExpect(status().isBadRequest());

        mockMvc.perform(post("/oauth2/v1/revoke")
                .param(Parameters.TOKEN, TestOAuthRefreshToken.jti2)
        ).andExpect(status().isForbidden());
    }
}