package com.example.oauth2example.controller;

import com.example.oauth2example.data.dao.TestOAuthClient;
import com.example.oauth2example.data.dao.TestOAuthClientUri;
import com.example.oauth2example.data.dao.TestUser;
import com.example.oauth2example.dto.Parameters;
import com.example.oauth2example.jwt.resolvers.JWTClaimsSetResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {"spring.datasource.initialization-mode=always"})
@ExtendWith(SpringExtension.class)
class OAuthPageControllerTest {
    private final JWTClaimsSetResolver jwtClaimsSetResolver = new JWTClaimsSetResolver();
    protected MockMvc mockMvc;
    @Autowired
    private Validator validator;
    @Autowired
    private OAuthPageController oAuthPageController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(oAuthPageController)
                .setValidator(validator)
                .setCustomArgumentResolvers(jwtClaimsSetResolver)
                .build();
    }

    @Test
    void getAuth() throws Exception {
        mockMvc.perform(get("/oauth2/v1/auth")).andExpect(status().isBadRequest());
        mockMvc.perform(get("/oauth2/v1/auth")
                .queryParam(Parameters.RESPONSE_TYPE, "code")
                .queryParam(Parameters.CLIENT_ID, TestOAuthClient.clientId)
                .queryParam(Parameters.REDIRECT_URI, TestOAuthClientUri.redirectUri)
        ).andExpect(status().isOk());
    }

    @Test
    void postAuth() throws Exception {
        mockMvc.perform(post("/oauth2/v1/auth")).andExpect(model().hasErrors());
        mockMvc.perform(post("/oauth2/v1/auth")
                .queryParam(Parameters.RESPONSE_TYPE, "code")
                .queryParam(Parameters.CLIENT_ID, TestOAuthClient.clientId + "else")
                .queryParam(Parameters.REDIRECT_URI, TestOAuthClientUri.redirectUri)
                .param(Parameters.USERNAME, TestUser.id)
                .param(Parameters.PASSWORD, TestUser.originalPassword)
        ).andExpect(status().isInternalServerError());

        mockMvc.perform(post("/oauth2/v1/auth")
                .queryParam(Parameters.RESPONSE_TYPE, "code")
                .queryParam(Parameters.CLIENT_ID, TestOAuthClient.clientId)
                .queryParam(Parameters.REDIRECT_URI, TestOAuthClientUri.redirectUri + "else")
                .param(Parameters.USERNAME, TestUser.id)
                .param(Parameters.PASSWORD, TestUser.originalPassword)
        ).andExpect(status().isInternalServerError());

        mockMvc.perform(post("/oauth2/v1/auth")
                .queryParam(Parameters.RESPONSE_TYPE, "code")
                .queryParam(Parameters.CLIENT_ID, TestOAuthClient.clientId)
                .queryParam(Parameters.REDIRECT_URI, TestOAuthClientUri.redirectUri)
                .param(Parameters.USERNAME, TestUser.id)
                .param(Parameters.PASSWORD, TestUser.originalPassword)
        ).andExpect(result -> {
            String redirectUrl = result.getResponse().getRedirectedUrl();
            assert redirectUrl != null;
            if (!redirectUrl.startsWith(TestOAuthClientUri.redirectUri))
                Assertions.fail("redirectUrl expected: not startWith " + TestOAuthClientUri.redirectUri);
            if (!redirectUrl.contains("code="))
                Assertions.fail("redirectUrl expected: not contains code.");
        });
    }
}