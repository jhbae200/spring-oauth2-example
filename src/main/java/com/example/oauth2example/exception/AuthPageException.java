package com.example.oauth2example.exception;

public class AuthPageException extends Throwable {
    private String code;
    public AuthPageException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
