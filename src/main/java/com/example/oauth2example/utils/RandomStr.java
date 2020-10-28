package com.example.oauth2example.utils;

import java.util.Objects;
import java.util.Random;

public class RandomStr {

    public static final String lower = "abcdefghijklmnopqrstuvwxyz";
    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String digits = "0123456789";
    public static final String alphanum = lower + upper + digits;
    private final Random random;
    private final char[] symbols = alphanum.toCharArray();
    private final char[] buf;

    public RandomStr(int length, Random random) {
        if (length < 1) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.buf = new char[length];
    }

    public String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(alphanum.length())];

        return new String(buf);
    }
}
