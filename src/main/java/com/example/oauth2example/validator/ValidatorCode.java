package com.example.oauth2example.validator;

import lombok.Getter;
import org.springframework.validation.BindingResult;

public enum ValidatorCode {
    NOT_EQUAL("validations.NotEqual", "not equal"),
    NOT_EMPTY("validations.NotEmpty", "not empty"),
    NOT_SUPPORT("validations.NotSupport", "not support"),
    SIZE("validations.Size", "size must be between {0} and {1}"),
    INVALID_REFRESH_TOKEN("validations.invalid.RefreshToken", "invalid refresh_token"),
    INVALID_PATTERN_EMAIL("validations.pattern.Email", "invalid pattern email"),
    INVALID_PATTERN_NICKNAME("validations.pattern.Nickname", "invalid pattern nickname"),
    INVALID_PATTERN_PASSWORD("validations.pattern.Password", "invalid pattern password"),
    DUPLICATE_EMAIL("validations.duplicate.Email", "duplicate email"),
    DUPLICATE_NICKNAME("validations.duplicate.Nickname", "duplicate nickname"),
    NOT_EQUAL_PASSWORDS("validations.notEqual.Passwords", "not equal passwords"),
    MIS_MATCH_PASSWORD("validations.misMatch.Password", "Password is incorrect."),
    NOT_EXIST_EMAIL("validations.notExist.Email", "Email has not been registered.");

    @Getter
    private final String errorCode;
    @Getter
    private final String defaultMessage;

    ValidatorCode(String errorCode, String defaultMessage) {
        this.errorCode = errorCode;
        this.defaultMessage = defaultMessage;
    }

}
