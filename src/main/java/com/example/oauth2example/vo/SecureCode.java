package com.example.oauth2example.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SecureCode {
    private String code;
    private int clientSeq;
}