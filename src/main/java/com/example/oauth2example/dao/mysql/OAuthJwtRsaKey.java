package com.example.oauth2example.dao.mysql;

import lombok.Data;

import java.io.StringReader;

@Data
public class OAuthJwtRsaKey {
    //    private int seq;
    private String name;
    private StringReader privateKey;
    private StringReader publicKey;
//    private Date regDate;
}
