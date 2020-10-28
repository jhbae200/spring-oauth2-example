package com.example.oauth2example.dao.mysql;

import lombok.Data;

import java.util.Date;

@Data
public class OAuthClientUserLog {
    private long seq;
    private long userSeq;
    private String clientUserId;
    private Integer clientSeq;
    private int type;
    private String data;
    private Date createdDate;
    private Integer os;
}
