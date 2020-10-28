package com.example.oauth2example.controller;

import com.example.oauth2example.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users/v1")
@RequiredArgsConstructor
public class UserController {
    /**
     * 유저 정보 요청
     *
     * @param userId {@link UserIdPathVariableInterceptor}
     * @return userinfo
     */
    @GetMapping(value = "/{clientUserId}")
    public ResponseEntity<UserDTO.UserResponse> getUserInfo(@PathVariable String clientUserId) {
        return ResponseEntity.ok().build();
    }
}