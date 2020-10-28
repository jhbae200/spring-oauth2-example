package com.example.oauth2example.component;

import com.example.oauth2example.MessageSourceImpl;
import com.example.oauth2example.dto.Parameters;
import com.example.oauth2example.repository.mysql.UserRepository;
import com.example.oauth2example.validator.Validator;
import com.example.oauth2example.validator.ValidatorCode;
import com.example.oauth2example.dao.mysql.User;
import com.example.oauth2example.dao.mysql.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserComponent {
    private final UserRepository userRepository;
    private final MessageSourceImpl messageSource;

    public User usernamePasswordCheck(String username, String password, BindingResult bindingResult) throws BindException {
        User user = userRepository.selectByIdAndType(username, UserType.EMAIL.getType());
        if (user == null) {
            bindingResult.rejectValue(Parameters.USERNAME, ValidatorCode.NOT_EXIST_EMAIL.getErrorCode(),
                    Objects.requireNonNull(messageSource.getMessage(ValidatorCode.NOT_EXIST_EMAIL)));
            throw new BindException(bindingResult);
        }
        checkPassword(user.getPassword(), password, bindingResult, Parameters.PASSWORD);
        return user;
    }

    public void checkPassword(String userPassword, String password, BindingResult bindingResult, String rejectFieldName) throws BindException {
        if (!Validator.confirmPassword(userPassword, password)) {
            bindingResult.rejectValue(rejectFieldName, ValidatorCode.MIS_MATCH_PASSWORD.getErrorCode(),
                    Objects.requireNonNull(messageSource.getMessage(ValidatorCode.MIS_MATCH_PASSWORD)));
            throw new BindException(bindingResult);
        }
    }

    public User getUserById(String userId, int userType) {
        User user = userRepository.selectByIdAndType(userId, userType);

        return user;
    }
}
