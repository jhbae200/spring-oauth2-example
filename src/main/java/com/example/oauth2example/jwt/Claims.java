package com.example.oauth2example.jwt;

public interface Claims {
    String SUBJECT = "sub";
    String AUDIENCE = "aud";
    String EXPIRATION = "exp";
    String ISSUER = "iss";
    String ISSUED_AT = "iat";
    String JTI = "jti";
    String ID = "id";
    String SCOPE = "scope";
    String NICKNAME = "nickname";
    String TYPE = "type";
    String NONCE = "nonce";
    String AT_HASH = "at_hash";
}
