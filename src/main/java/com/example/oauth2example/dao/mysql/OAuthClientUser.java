package com.example.oauth2example.dao.mysql;

import lombok.Data;

import java.util.Date;

@Data
public class OAuthClientUser {
    private Long seq;
    private Long userSeq;
    private String clientUserId;
    private Long clientSeq;
    private Date createdDate;
}
