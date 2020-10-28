package com.example.oauth2example.config.interceptors;

import com.example.oauth2example.component.OAuthComponent;
import com.example.oauth2example.data.dao.TestOAuthAccessToken;
import com.example.oauth2example.data.dao.TestOAuthJwtRasKey;
import com.example.oauth2example.repository.mysql.OAuthJwtRsaKeyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Collections;
import java.util.Map;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserIdPathVariableInterceptorTest {
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private OAuthJwtRsaKeyRepository oAuthJwtRsaKeyRepository;
    @Autowired
    private OAuthComponent oAuthComponent;

    @BeforeEach
    public void setup() {
        Mockito.when(oAuthJwtRsaKeyRepository.selectAll()).thenReturn(Collections.singletonList(TestOAuthJwtRasKey.getTestData()));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void preHandle() throws Exception {
        String accessToken = oAuthComponent.createAccessToken(TestOAuthAccessToken.getTestData2());

        mockMvc.perform(
                get("/users/v1/me")
        ).andExpect(status().isUnauthorized());
        mockMvc.perform(
                get("/users/v1/me").header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        ).andExpect(status().isOk()).andExpect(result -> {
            Object tmp = result.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Map<String, String> uriTemplateVars = (Map<String, String>) result.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            assertEquals("UserId", TestOAuthAccessToken.sub, uriTemplateVars.get("clientUserId"));
        });
    }
}