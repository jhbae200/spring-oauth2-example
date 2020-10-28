package com.example.oauth2example.dao.mysql;

import lombok.Data;

import java.util.Date;

@Data
public class OAuthClientHttp {
    private long seq;
    private long clientSeq;
    private String url;
    private long type;
    private Date createdDate;
}
