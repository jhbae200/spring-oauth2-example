package com.example.oauth2example.validator.annotations;

import com.example.oauth2example.validator.resolvers.NicknameResolver;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {NicknameResolver.class})
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
public @interface Nickname {

    String message() default "{validations.pattern.Nickname}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    boolean checkDuplicated() default false;
}
