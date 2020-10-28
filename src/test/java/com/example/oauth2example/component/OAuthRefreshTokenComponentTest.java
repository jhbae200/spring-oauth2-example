package com.example.oauth2example.component;

import com.example.oauth2example.MessageSourceImpl;
import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.example.oauth2example.data.dao.TestOAuthClient;
import com.example.oauth2example.data.dao.TestOAuthClientUser;
import com.example.oauth2example.data.dao.TestUser;
import com.example.oauth2example.exception.DBException;
import com.example.oauth2example.repository.RefreshTokenRepository;
import com.example.oauth2example.vo.OAuthUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OAuthRefreshTokenComponentTest {
    OAuthRefreshTokenComponent oAuthRefreshTokenComponent;
    @Mock
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void before() {
        oAuthRefreshTokenComponent = new OAuthRefreshTokenComponent(refreshTokenRepository, new MessageSourceImpl(new ResourceBundleMessageSource()));
    }

    @Test
    void createRefreshToken() throws DBException {
        Mockito.when(refreshTokenRepository.deleteByClientSeqAndUserId(Mockito.any(), Mockito.any())).thenReturn(1);
        Mockito.when(refreshTokenRepository.insert(Mockito.any())).then(invocation -> {
            ((OAuthRefreshToken) invocation.getArgument(0)).setSeq(1L);
            return 1;
        });
        OAuthUserInfo oAuthUserInfo = OAuthUserInfo.create(TestUser.getTestData(), TestOAuthClientUser.getTestData());
        OAuthRefreshToken oAuthRefreshToken = oAuthRefreshTokenComponent.createRefreshToken(TestOAuthClient.getTestData(), oAuthUserInfo, "profile", null);
        assertNotNull(oAuthRefreshToken);
        assertEquals(TestUser.seq, oAuthRefreshToken.getUserSeq());
    }
}