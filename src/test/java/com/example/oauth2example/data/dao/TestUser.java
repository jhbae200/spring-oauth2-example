package com.example.oauth2example.data.dao;

import com.example.oauth2example.dao.mysql.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

public class TestUser {
    public static final long seq = 1L;
    public static final String id = "test@example.com";
    public static final String nickname = "test";
    public static final Date createdDate = new Date(1603359529827L); //2020-10-22T09:38:49.827Z
    public static final int type = 1;

    public static final String originalPassword = "test1234";

    public static User getTestData() {
        User user = new User();
        user.setSeq(seq);
        user.setId(id);
        user.setNickname(nickname);
        user.setCreatedDate(createdDate);
        user.setUpdatedDate(null);
        user.setType(type);
        user.setPassword(getEncodePassword());
        return user;
    }

    public static String getEncodePassword() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(originalPassword);
    }
}
