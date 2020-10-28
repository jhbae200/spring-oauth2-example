package com.example.oauth2example.component;

import com.example.oauth2example.dao.mysql.OAuthClient;
import com.example.oauth2example.dao.mysql.OAuthClientUser;
import com.example.oauth2example.dao.mysql.User;
import com.example.oauth2example.data.dao.TestOAuthClient;
import com.example.oauth2example.data.dao.TestOAuthClientUser;
import com.example.oauth2example.data.dao.TestUser;
import com.example.oauth2example.exception.DBException;
import com.example.oauth2example.repository.mysql.OAuthClientUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class OAuthClientUserComponentTest {
    OAuthClientUserComponent oAuthClientUserComponent;
    @Mock
    OAuthClientUserRepository oAuthClientUserRepository;

    @BeforeEach
    void before() {
        Mockito.when(oAuthClientUserRepository.select1stByUserSeqAndClientSeq(TestUser.seq, TestOAuthClient.seq)).thenReturn(TestOAuthClientUser.getTestData());

        oAuthClientUserComponent = new OAuthClientUserComponent(oAuthClientUserRepository);
    }

    @Test
    void getOrCreateOAuthClientUser() throws DBException {
        User user2 = new User();
        user2.setSeq(2L);

        OAuthClientUser oAuthClientUser2 = new OAuthClientUser();
        oAuthClientUser2.setUserSeq(user2.getSeq());
        oAuthClientUser2.setClientSeq(1L);

        Mockito.when(oAuthClientUserRepository.insert(Mockito.any(OAuthClientUser.class))).thenReturn(1);

        OAuthClientUser testData = oAuthClientUserComponent.getOrCreateOAuthClientUser(TestOAuthClient.getTestData(), TestUser.getTestData());
        Assertions.assertEquals(TestOAuthClientUser.getTestData(), testData);

        OAuthClientUser testData2 = oAuthClientUserComponent.getOrCreateOAuthClientUser(TestOAuthClient.getTestData(), user2);
        Assertions.assertEquals(user2.getSeq(), testData2.getUserSeq());
    }
}