package com.example.oauth2example.validator.annotations;

import com.example.oauth2example.validator.resolvers.IntegerResolver;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {IntegerResolver.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
public @interface IntegerEquals {

    String message() default "{validations.NotEqual}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}