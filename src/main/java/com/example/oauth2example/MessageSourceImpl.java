package com.example.oauth2example;

import com.example.oauth2example.validator.ValidatorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageSourceImpl {
    private final MessageSource messageSource;

    public String getMessage(ValidatorCode validatorCode) {
        return messageSource.getMessage(ValidatorCode.NOT_EXIST_EMAIL.getErrorCode(), null, ValidatorCode.NOT_EXIST_EMAIL.getDefaultMessage(), LocaleContextHolder.getLocale());
    }
}
