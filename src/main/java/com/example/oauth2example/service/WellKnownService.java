package com.example.oauth2example.service;

import com.example.oauth2example.dto.WellKnownDTO;
import com.example.oauth2example.jwt.Claims;
import com.example.oauth2example.jwt.JwtBuilder;
import com.example.oauth2example.oauth2.CodeChallengeType;
import com.example.oauth2example.oauth2.GrantType;
import com.example.oauth2example.oauth2.ResponseType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WellKnownService {
    private final JwtBuilder jwtBuilder;
    @Value("${domain}")
    private String domain;


    public Map<String, Object> getJwksJSON() {
        return jwtBuilder.getJwkSet().toJSONObject(true);
    }

    public WellKnownDTO.OpenIdConfiguration getOpenIdConfiguration() {
        WellKnownDTO.OpenIdConfiguration openIdConfiguration = new WellKnownDTO.OpenIdConfiguration();
        openIdConfiguration.setIssuer(domain);
        openIdConfiguration.setAuthorization_endpoint(domain + "/oauth2/v1/auth");
        openIdConfiguration.setToken_endpoint(domain + "/oauth2/v1/token");
        openIdConfiguration.setUserinfo_endpoint(domain + "/oauth2/v1/userinfo");
        openIdConfiguration.setRevocation_endpoint(domain + "/oauth2/v1/revoke");
        //        MvcUriComponentsBuilder.fromMethodName(WellKnownController.class, "getJwks");
        openIdConfiguration.setJwks_uri(domain + "/.well-known/jwks.json");
        openIdConfiguration.setScopes_supported(Arrays.asList("profile", "openid"));
        openIdConfiguration.setResponse_types_supported(Arrays.asList(ResponseType.CODE));
        openIdConfiguration.setGrant_types_supported(Arrays.asList(GrantType.AUTHORIZATION_CODE, GrantType.PASSWORD, GrantType.REFRESH_TOKEN));
        openIdConfiguration.setSubject_types_supported(Arrays.asList("public"));
        openIdConfiguration.setId_token_signing_alg_values_supported(Arrays.stream(CodeChallengeType.values()).map(CodeChallengeType::getTitle).collect(Collectors.toList()));
        openIdConfiguration.setClaim_types_supported(Arrays.stream(Claims.class.getFields()).map(field -> {
            try {
                return (String) field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()));

        return openIdConfiguration;
    }
}
