package com.example.oauth2example.config.interceptors;

import com.example.oauth2example.ExampleHandlerMapping;
import com.example.oauth2example.component.OAuthComponent;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class BearerInterceptor implements HandlerInterceptor {
    private final OAuthComponent oAuthComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String token = authorization.substring("Bearer ".length());
        if (token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        request.setAttribute(ExampleHandlerMapping.JWT_CLAIM_SET, oAuthComponent.parsingAndValidateAccessToken(token));
        return true;
    }
}
