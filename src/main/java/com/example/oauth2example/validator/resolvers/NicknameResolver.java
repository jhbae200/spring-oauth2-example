package com.example.oauth2example.validator.resolvers;

import com.example.oauth2example.repository.mysql.UserRepository;
import com.example.oauth2example.validator.Validator;
import com.example.oauth2example.validator.annotations.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class NicknameResolver extends Resolver implements ConstraintValidator<Nickname, String> {

    private Nickname nickname;
    private final UserRepository userRepository;

    @Override
    public void initialize(Nickname constraintAnnotation) {
        this.nickname = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (StringUtils.isEmpty(value)) {
            addConstraintViolation(context, "{validations.NotEmpty}");
            return false;
        }

        if (!Validator.isNicknamePattern(value)) {
            addConstraintViolation(context, "{validations.pattern.Nickname}");
            return false;
        }

        if (nickname.checkDuplicated() && userRepository.selectNicknameByNickname(value) != null) {
            addConstraintViolation(context, "{validations.duplicate.Nickname}");
            return false;
        }

        return true;
    }
}
