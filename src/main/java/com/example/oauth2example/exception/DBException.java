package com.example.oauth2example.exception;

public class DBException extends Exception {
    private final int result;

    public DBException(String message, int result) {
        super(message + ", result: " + result);
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
