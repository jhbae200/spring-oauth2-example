package com.example.oauth2example.dto;

import com.example.oauth2example.validator.annotations.Email;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import javax.validation.constraints.NotBlank;
import java.util.Date;

public class AuthDTO {

    @Data
    public static class TokenRequest {
        @NotBlank(message = "{validations.NotEmpty}")
        private String grant_type;
        @NotBlank(message = "{validations.NotEmpty}")
        private String client_id;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TokenCodeRequest extends TokenRequest {
        @NotBlank(message = "{validations.NotEmpty}")
        private String code;
        private String code_verifier;
        private String client_secret;
        private Integer os = 0;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TokenRefreshRequest extends TokenRequest {
        public static final String REFRESH_TOKEN = "refresh_token";

        @NotBlank(message = "{validations.NotEmpty}")
        private String refresh_token;
    }

    @Data
    public static class TokenResponse {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("token_type")
        private String tokenType;
        @JsonProperty("expires_in")
        private long expiresIn;
        @JsonProperty("refresh_token")
        private String refreshToken;
        @JsonProperty("id_token")
        private String idToken;

        public static TokenResponse create(String accessToken, String jti, long expiresIn) {
            AuthDTO.TokenResponse tokenResponse = new AuthDTO.TokenResponse();
            tokenResponse.setAccessToken(accessToken);
            tokenResponse.setRefreshToken(jti);
            tokenResponse.setExpiresIn(expiresIn);
            tokenResponse.setTokenType("Bearer");
            tokenResponse.setIdToken(accessToken);

            return tokenResponse;
        }

        public static TokenResponse create(String accessToken, String jti, long expiresIn, String idToken) {
            AuthDTO.TokenResponse tokenResponse = new AuthDTO.TokenResponse();
            tokenResponse.setAccessToken(accessToken);
            tokenResponse.setRefreshToken(jti);
            tokenResponse.setExpiresIn(expiresIn);
            tokenResponse.setTokenType("Bearer");
            tokenResponse.setIdToken(idToken);

            return tokenResponse;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TokenPasswordRequest extends TokenRequest {
        @NotBlank(message = "{validations.NotEmpty}")
        @Email
        private String username;
        @NotBlank(message = "{validations.NotEmpty}")
        private String password;
        @NotBlank(message = "{validations.NotEmpty}")
        private String client_secret;
        private String scope;
        private String nonce;
    }

    @Data
    public static class TokenRevokeRequest {
        @NotBlank(message = "{validations.NotEmpty}")
        private String token;
        private String token_type_hint;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TokenGuestRequest extends TokenRequest {
        public static final String DEVICE_ID = "device_id";
        public static final String PASSWORD = "password";

        @NotBlank(message = "{validations.NotEmpty}")
        private String device_id;
        @NotBlank(message = "{validations.NotEmpty}")
        private String password;
        private String nonce;
        private Integer os = 0;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TokenSocialRequest extends TokenRequest {
        @NotBlank(message = "{validations.NotEmpty}")
        private String access_token;
        @NotBlank(message = "{validations.NotEmpty}")
        private String service;
        @NotBlank(message = "{validations.NotEmpty}")
        private String client_secret;
        private String guest_code;
        private String scope;
        private String nonce;
        private Integer os = 0;
    }

    @Data
    public static class AuthResponse {
        private String redirect_uri;
    }
}
