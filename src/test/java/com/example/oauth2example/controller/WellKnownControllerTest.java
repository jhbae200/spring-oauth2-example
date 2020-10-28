package com.example.oauth2example.controller;

import com.example.oauth2example.data.dao.TestOAuthJwtRasKey;
import com.example.oauth2example.jwt.JwtBuilder;
import com.example.oauth2example.repository.mysql.OAuthJwtRsaKeyRepository;
import com.example.oauth2example.service.WellKnownService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class WellKnownControllerTest {
    @MockBean
    private OAuthJwtRsaKeyRepository oAuthJwtRsaKeyRepository;
    @Autowired
    private WellKnownService wellKnownService;
    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        Mockito.when(oAuthJwtRsaKeyRepository.selectAll()).thenReturn(Collections.singletonList(TestOAuthJwtRasKey.getTestData()));
        mockMvc = MockMvcBuilders.standaloneSetup(new WellKnownController(wellKnownService)).build();
    }

    @Test
    void getJwks() throws Exception {
        mockMvc.perform(get("/.well-known/jwks.json")).andExpect(status().isOk());
    }

    @Test
    void getOpenIdConfiguration()throws Exception {
        mockMvc.perform(get("/.well-known/openid-configuration")).andExpect(status().isOk());
    }
}