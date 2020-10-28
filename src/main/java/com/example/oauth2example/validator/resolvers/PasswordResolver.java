package com.example.oauth2example.validator.resolvers;

import com.example.oauth2example.validator.Validator;
import com.example.oauth2example.validator.annotations.Password;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordResolver extends Resolver implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(StringUtils.isEmpty(value)) {
            addConstraintViolation(context, "{validations.NotEmpty}");
            return false;
        }

        if (!Validator.regexPassword(value)) {
            addConstraintViolation(context, "{validations.pattern.Password}");
            return false;
        }

        return true;
    }
}
