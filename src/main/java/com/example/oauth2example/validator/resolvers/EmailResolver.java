package com.example.oauth2example.validator.resolvers;

import com.example.oauth2example.validator.Validator;
import com.example.oauth2example.validator.annotations.Email;
import com.example.oauth2example.repository.mysql.UserRepository;
import com.example.oauth2example.dao.mysql.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class EmailResolver extends Resolver implements ConstraintValidator<Email, String> {
    private final UserRepository userRepository;
    private Email email;

    @Override
    public void initialize(Email constraintAnnotation) {
        this.email = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(StringUtils.isEmpty(value)) {
            addConstraintViolation(context, "{validations.NotEmpty}");
            return false;
        }

        if(!Validator.isEmailPattern(value)) {
            addConstraintViolation(context, "{validations.pattern.Email}");
            return false;
        }

        if(email.checkDuplicated()) {
            String isExistEmail = this.userRepository.selectIdByEmail(value, UserType.EMAIL.getType());
            if (isExistEmail != null) {
                super.addConstraintViolation(context, "{validations.duplicate.Email}");
                return false;
            }
        }

        return true;
    }
}
