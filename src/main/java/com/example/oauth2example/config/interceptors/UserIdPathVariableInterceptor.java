package com.example.oauth2example.config.interceptors;

import com.example.oauth2example.ExampleHandlerMapping;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;



@Component
@RequiredArgsConstructor
public class UserIdPathVariableInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> uriTemplateVars = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (uriTemplateVars.containsKey("clientUserId")) {
            String clientUserId = uriTemplateVars.get("clientUserId");
            JWTClaimsSet jwtClaimsSet = (JWTClaimsSet) request.getAttribute(ExampleHandlerMapping.JWT_CLAIM_SET);

            if (jwtClaimsSet == null)
                throw new IllegalAccessException("not set JWTClaimsSet");

            if ("me".equals(clientUserId)) {
                uriTemplateVars.put("clientUserId", jwtClaimsSet.getSubject());
                clientUserId = jwtClaimsSet.getSubject();
            } else if (!jwtClaimsSet.getSubject().equals(clientUserId))
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);

            uriTemplateVars.put("clientUserId", clientUserId);
        }
        return true;
    }
}
