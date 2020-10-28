package com.example.oauth2example.validator;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static final String EMAIL = "^(?:(?:[\\w`~!#$%^&*\\-=+;:{}'|,?\\/]+(?:(?:\\.(?:\"(?:\\\\?[\\w`~!#$%^&*\\-=+;:{}'|,?\\/\\.()<>\\[\\] @]|\\\\\"|\\\\\\\\)*\"|[\\w`~!#$%^&*\\-=+;:{}'|,?\\/]+))*\\.[\\w`~!#$%^&*\\-=+;:{}'|,?\\/]+)?)|(?:\"(?:\\\\?[\\w`~!#$%^&*\\-=+;:{}'|,?\\/\\.()<>\\[\\] @]|\\\\\"|\\\\\\\\)+\"))@(?:[a-zA-Z\\d\\-]+(?:\\.[a-zA-Z\\d\\-]+)*|\\[\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\])$";
    public static final String PASSWORD = "^(?=.*[a-zA-Z])(?=.*[\\d])[\\w$@$!%*#?&`'\"~^*(){}<>.\\/\\\\\\[\\]=+\\-_|]{6,20}$";
    public static final String NICKNAME = "^[A-Za-z0-9ㄱ-ㅎㅏ-ㅣ가-힣]{1,30}$";
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static boolean isEmailPattern(String email) {
        if (email == null) return false;
        Pattern pattern = Pattern.compile(EMAIL);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean regexPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD);
        Matcher matcher = pattern.matcher(password);

        return matcher.find();
    }

    public static boolean isNicknamePattern(String nickname) {
        Pattern pattern = Pattern.compile(NICKNAME);
        Matcher matcher = pattern.matcher(nickname);

        return matcher.matches();
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean confirmPassword(String userPassword, String passwordToCheck) {
        if (userPassword != null && passwordToCheck != null ) {
            return passwordEncoder.matches(passwordToCheck, userPassword);
        }

        return false;
    }

    public static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }

}

