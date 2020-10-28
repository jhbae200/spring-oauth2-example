package com.example.oauth2example.dao.mysql;

import lombok.Data;

import java.util.Date;

@Data
public class OAuthClient {
    private Long seq;
    private String clientId;
    private String clientSecret;
    private String serviceName;
    private Date createdDate;
}
