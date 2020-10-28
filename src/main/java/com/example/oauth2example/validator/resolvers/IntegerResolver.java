package com.example.oauth2example.validator.resolvers;


import com.example.oauth2example.validator.annotations.IntegerEquals;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IntegerResolver extends Resolver implements ConstraintValidator<IntegerEquals, Integer> {

    private IntegerEquals integer;

    @Override
    public void initialize(IntegerEquals constraintAnnotation) {
        this.integer = constraintAnnotation;
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(integer.value() != value) {
            return false;
        }

        return true;
    }
}
