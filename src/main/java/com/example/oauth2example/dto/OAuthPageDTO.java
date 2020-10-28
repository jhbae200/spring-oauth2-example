package com.example.oauth2example.dto;

import com.example.oauth2example.oauth2.CodeChallengeType;
import com.example.oauth2example.validator.annotations.Email;
import com.example.oauth2example.validator.annotations.Enum;
import com.example.oauth2example.validator.annotations.Equals;
import com.example.oauth2example.validator.annotations.Password;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

public class OAuthPageDTO {
    /**
     * OAuth 관련 공통 인자 DTO
     */
    @Data
    public static class AuthRequest {
        @Equals(value = "code")
        private String response_type;
        @NotBlank(message = "{validations.NotEmpty}")
        private String redirect_uri;
        @NotBlank(message = "{validations.NotEmpty}")
        private String client_id;
        private String state;
        private String scope;
        private String nonce;
        @Nullable
        @Length(min = 20, max = 128, message = "{validations.Size}")
        private String code_challenge;
        @Nullable
        @Enum(enumClass = CodeChallengeType.class, ignoreCase = true)
        private String code_challenge_method = CodeChallengeType.PLAIN.getTitle();
        private String lang = "en";
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GetAuthRequest extends AuthRequest {
        private String guest_code;
        private String login_block;

        // for showing login page
        private String username;
        private String password;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class PostAuthRequest extends AuthRequest {
        private String guest_code;
        private String login_block;

        @Email
        private String username;
        @Password
        private String password;
    }
}
