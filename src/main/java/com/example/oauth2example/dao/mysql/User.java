package com.example.oauth2example.dao.mysql;

import lombok.Data;

import java.security.SecureRandom;
import java.util.Date;

@Data
public class User {
    private Long seq;
    private String id;
    private String nickname;
    private Date createdDate;
    private Date updatedDate;
    private int type;
    private String password;
}
