package com.example.oauth2example.dto;

import com.example.oauth2example.vo.OAuthUserInfo;
import lombok.Data;

public class UserDTO {
    @Data
    public static class UserResponse {
        private String clientUserId;
        private String nickname;
        private String email;
        private int type;

        public static UserResponse create(OAuthUserInfo oAuthUserInfo) {
            UserResponse userResponse = new UserResponse();
            userResponse.setClientUserId(oAuthUserInfo.getClientUserId());
            userResponse.setNickname(oAuthUserInfo.getNickname());
            userResponse.setEmail(oAuthUserInfo.getId());
            userResponse.setType(oAuthUserInfo.getType());
            return userResponse;
        }
    }
}
