package com.example.oauth2example.controller;

import com.example.oauth2example.config.interceptors.BearerInterceptor;
import com.example.oauth2example.dto.AuthDTO;
import com.example.oauth2example.exception.CodeException;
import com.example.oauth2example.jwt.resolvers.JWTClaimsSetResolver;
import com.example.oauth2example.oauth2.GrantType;
import com.example.oauth2example.service.OAuthService;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth2/v1")
public class OAuthController {
    private final OAuthService oauthService;

    /**
     * token endpoint
     * https://tools.ietf.org/html/rfc6749#section-3.2
     * support grant type: password(innerService only), authorization_code, refresh_token
     * client authorization support POST, basic auth
     * {@code initBinder} validation check, override basic auth
     * TODO:: support implicit grant type.(https://tools.ietf.org/html/rfc6749#section-4.2)
     *
     * @param tokenRequest  check request info (https://tools.ietf.org/html/rfc6749#section-4.1.3, https://tools.ietf.org/html/rfc6749#section-4.3.2, https://tools.ietf.org/html/rfc6749#section-6)
     * @param bindingResult parameter error info.
     * @return response info (https://tools.ietf.org/html/rfc6749#section-4.1.4)
     * @throws Exception all fail exception.
     */
    @PostMapping(value = "/token")
    public ResponseEntity<AuthDTO.TokenResponse> postToken(@ModelAttribute @Valid AuthDTO.TokenRequest tokenRequest, BindingResult bindingResult) throws Exception {
        throw new BindException(bindingResult);
    }

    @PostMapping(value = "/token", params = "grant_type=" + GrantType.PASSWORD)
    public ResponseEntity<AuthDTO.TokenResponse> postTokenPassword(@ModelAttribute @Valid AuthDTO.TokenPasswordRequest tokenRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(oauthService.postTokenPassword(tokenRequest, bindingResult));
    }

    @PostMapping(value = "/token", params = "grant_type=" + GrantType.AUTHORIZATION_CODE)
    public ResponseEntity<AuthDTO.TokenResponse> postTokenAuthCode(@ModelAttribute @Valid AuthDTO.TokenCodeRequest tokenRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(oauthService.postTokenAuthCode(tokenRequest));
    }

    @PostMapping(value = "/token", params = "grant_type=" + GrantType.REFRESH_TOKEN)
    public ResponseEntity<AuthDTO.TokenResponse> postTokenRefreshToken(@ModelAttribute @Valid AuthDTO.TokenRefreshRequest tokenRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors())
            throw new BindException(bindingResult);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(oauthService.postTokenRefreshToken(tokenRequest, bindingResult));
    }
//
//
//    @PostMapping(value = "/token", params = "grant_type=" + GrantType.SOCIAL_ACCESS_TOKEN)
//    public ResponseEntity<AuthDTO.TokenResponse> postTokenSocial(@ModelAttribute @Valid AuthDTO.TokenSocialRequest tokenRequest, BindingResult bindingResult) throws Exception {
//        if (bindingResult.hasErrors())
//            throw new BindException(bindingResult);
//        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(oauthService.postTokenSocial(tokenRequest));
//    }

    /**
     * userinfo endpoint
     * https://openid.net/specs/openid-connect-core-1_0.html#UserInfo
     * support only Bearer Authorization jwt
     * see {@link BearerInterceptor}, {@link JWTClaimsSetResolver}
     *
     * @return JWTClaims info
     */
    @GetMapping(value = "/userinfo", produces = "application/json")
    public ResponseEntity<Map<String, Object>> getUserInfoJson(JWTClaimsSet jwtClaimsSet) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jwtClaimsSet.toJSONObject());
    }

    @PostMapping(value = "/revoke")
    public ResponseEntity postRevoke(@ModelAttribute @Valid AuthDTO.TokenRevokeRequest tokenRevokeRequest) {
        oauthService.postRevoke(tokenRevokeRequest);
        return ResponseEntity.ok().build();
    }
}
