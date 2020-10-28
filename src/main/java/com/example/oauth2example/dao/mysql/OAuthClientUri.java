package com.example.oauth2example.dao.mysql;

import lombok.Data;

@Data
public class OAuthClientUri {
    private long seq;
    private long clientSeq;
    private String redirectUri;
}
