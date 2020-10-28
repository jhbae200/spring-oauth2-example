package com.example.oauth2example.validator.annotations;

import com.example.oauth2example.validator.resolvers.EnumResolver;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {EnumResolver.class})
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface Enum {

    String message() default "{validations.NotSupport}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends java.lang.Enum<?>> enumClass();
    boolean ignoreCase() default false;
}
