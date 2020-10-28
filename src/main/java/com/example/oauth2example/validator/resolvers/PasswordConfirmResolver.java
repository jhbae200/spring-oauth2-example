package com.example.oauth2example.validator.resolvers;

import com.example.oauth2example.validator.Validator;
import com.example.oauth2example.validator.annotations.PasswordConfirm;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordConfirmResolver extends Resolver implements ConstraintValidator<PasswordConfirm, Object> {

    @SneakyThrows
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Class<?> c = value.getClass();

        String password = (String) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(c, "password")).getReadMethod().invoke(value);
        String passwordConfirm = (String) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(c, "passwordConfirm")).getReadMethod().invoke(value);

        if(Validator.regexPassword(password) && password.equals(passwordConfirm)) {
            return true;
        }

        addConstraintViolation(context, "{validations.passwords.Equal}");
        return false;
    }
}
