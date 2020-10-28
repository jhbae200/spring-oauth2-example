package com.example.oauth2example.dao.mysql;

import java.util.Arrays;

public enum UserType {
    EMAIL("email", 1),
    GOOGLE("google", 2),
    FACEBOOK("facebook", 3),
    KAKAO("kakao", 4);

    private final String service;
    private final int type;

    UserType(String service, int type) {
        this.service = service;
        this.type = type;
    }

    public static UserType findByLevel(int level) {
        return Arrays.stream(UserType.values()).filter(userType -> userType.getType() == level).findAny().orElseThrow(() ->
                new IllegalArgumentException("Not supported level")
        );
    }

    public static UserType findByService(String service) {
        return Arrays.stream(UserType.values()).filter(userType -> userType.getService().equals(service)).findAny().orElseThrow(() ->
                new IllegalArgumentException("Not supported service")
        );
    }

    public String getService() {
        return service;
    }

    public int getType() {
        return type;
    }
}
