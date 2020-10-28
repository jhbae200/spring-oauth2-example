package com.example.oauth2example.dao.mysql;

import java.util.Arrays;

public enum OAuthUserAction {
    SIGN(0),
    RESIGN(1),
    NICKNAME(2);


    private final int type;

    OAuthUserAction(int type) {
        this.type = type;
    }

    public static OAuthUserAction findByType(int type) {
        return Arrays.stream(OAuthUserAction.values()).filter(httpType -> httpType.getType() == type).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Not Supported Type"));
    }

    public int getType() {
        return type;
    }
}
