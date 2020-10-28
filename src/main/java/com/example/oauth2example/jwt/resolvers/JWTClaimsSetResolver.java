package com.example.oauth2example.jwt.resolvers;

import com.example.oauth2example.ExampleHandlerMapping;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class JWTClaimsSetResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return JWTClaimsSet.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object object = webRequest.getAttribute(ExampleHandlerMapping.JWT_CLAIM_SET, WebRequest.SCOPE_REQUEST);
        if (object == null) throw new IllegalAccessException("not set JWTClaimsSet");
        return object;
    }
}
