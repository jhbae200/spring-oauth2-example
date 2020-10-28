package com.example.oauth2example.validator.resolvers;

import com.example.oauth2example.validator.annotations.Equals;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EqualsResolver extends Resolver implements ConstraintValidator<Equals, String> {

    private Equals equals;

    @Override
    public void initialize(Equals constraintAnnotation) {
        this.equals = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if(!equals.value().equals(value)) {
            addConstraintViolation(context, "unsupported_value : " + value);
            return false;
        }

        return true;
    }
}
