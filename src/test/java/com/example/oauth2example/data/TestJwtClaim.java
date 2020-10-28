package com.example.oauth2example.data;

import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;

public class TestJwtClaim {
    public static JWTClaimsSet getTestData() throws ParseException {
        return JWTClaimsSet.parse("{\n" +
                "  \"sub\": \"bfd0f660-cdb4-494f-9a88-63fee3b8ee30\",\n" +
                "  \"aud\": \"testClientId\",\n" +
                "  \"scope\": [\n" +
                "    \"open_id\",\n" +
                "    \"profile\"\n" +
                "  ],\n" +
                "  \"iss\": \"http://localhost:8080\",\n" +
                "  \"type\": 1,\n" +
                "  \"exp\": 1603788284,\n" +
                "  \"iat\": 1603359529,\n" +
                "  \"nonce\": \"nb912eca1589tj01a\",\n" +
                "  \"jti\": \"a9d36864-67fe-41df-a1ca-550822d3f7c6\"\n" +
                "}");
    }
}
