package com.example.oauth2example.utils;

/**
 * Created by parkjp on 2017-11-22.
 */
public class UUID {

    public static String getId() {
        return java.util.UUID.randomUUID().toString().toUpperCase();
    }
}
