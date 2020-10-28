package com.example.oauth2example.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA256Utils {
    public static String toSHA256Base64(String str) throws NoSuchAlgorithmException {
        byte[] data = str.getBytes();
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        digester.update(data);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digester.digest());
    }

    public static boolean matchSHA256(String str, String sha256str) throws NoSuchAlgorithmException {
        return toSHA256Base64(str).equals(sha256str);
    }
}
