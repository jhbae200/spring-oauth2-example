package com.example.oauth2example.component;

import com.example.oauth2example.MessageSourceImpl;
import com.example.oauth2example.dao.mysql.User;
import com.example.oauth2example.dao.mysql.UserType;
import com.example.oauth2example.data.dao.TestUser;
import com.example.oauth2example.dto.Parameters;
import com.example.oauth2example.repository.mysql.UserRepository;
import lombok.Data;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.MapBindingResult;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class UserComponentTest {
    private static final String wrongPassword = "wrongPassword";
    @Mock
    UserRepository userRepository;
    UserComponent userComponent;

    @BeforeEach
    void before() {
        MessageSourceImpl messageSource = new MessageSourceImpl(new ResourceBundleMessageSource());
        userComponent = new UserComponent(userRepository, messageSource);
    }


    @Test
    void usernamePasswordCheck() throws BindException {
        User user = TestUser.getTestData();
        Mockito.when(userRepository.selectByIdAndType(TestUser.id, UserType.EMAIL.getType())).thenReturn(user);

        Map<String, String> request = new HashMap<>();
        request.put(Parameters.USERNAME, "");
        request.put(Parameters.PASSWORD, "");

        User testUser = userComponent.usernamePasswordCheck(TestUser.id, TestUser.originalPassword, new MapBindingResult(request, "request"));
        Assertions.assertEquals(testUser, user);
        Assertions.assertThrows(BindException.class, () -> {
            userComponent.usernamePasswordCheck(TestUser.id, wrongPassword, new MapBindingResult(request, "request"));
        });
    }

    @Test
    void checkPassword() {
        Map<String, String> request = new HashMap<>();
        request.put(Parameters.USERNAME, "");
        request.put(Parameters.PASSWORD, "");

        Assertions.assertDoesNotThrow(() -> {
            userComponent.checkPassword(TestUser.getTestData().getPassword(), TestUser.originalPassword, new MapBindingResult(request, "request"), Parameters.PASSWORD);
        });
        Assertions.assertThrows(BindException.class, () -> {
            userComponent.checkPassword(TestUser.getTestData().getPassword(), wrongPassword, new MapBindingResult(request, "request"), Parameters.PASSWORD);
        });
    }

    @Data
    public static class TestResponseDTO {
        private String username;
        private String password;
    }

}