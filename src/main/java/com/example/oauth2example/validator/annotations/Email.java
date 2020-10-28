package com.example.oauth2example.validator.annotations;

import com.example.oauth2example.validator.resolvers.EmailResolver;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {EmailResolver.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
public @interface Email {
    String message() default "{validations.pattern.Email}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean checkDuplicated() default false;
}
