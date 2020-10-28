package com.example.oauth2example.validator.annotations;

import com.example.oauth2example.validator.resolvers.PasswordConfirmResolver;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = {PasswordConfirmResolver.class})
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface PasswordConfirm {

    String message() default "{validations.passwords.Equal}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
