package com.example.oauth2example.oauth2;

import lombok.Getter;

import java.util.Arrays;

public enum CodeChallengeType {
    PLAIN("plain", 0),
    SHA256("S256", 1);

    @Getter
    private String title;
    @Getter
    private int val;

    CodeChallengeType(String title, int val) {
        this.title = title;
        this.val = val;
    }

    public static CodeChallengeType findType(String title) {
        return Arrays.stream(CodeChallengeType.values()).filter(type -> type.getTitle().equals(title)).findAny().orElse(null);
    }

    public static CodeChallengeType findType(int val) {
        return Arrays.stream(CodeChallengeType.values()).filter(type -> type.getVal() == val).findAny().orElse(null);
    }

    @Override
    public String toString() {
        return title;
    }
}

