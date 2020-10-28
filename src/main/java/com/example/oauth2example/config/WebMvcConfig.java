package com.example.oauth2example.config;

import com.example.oauth2example.config.interceptors.BearerInterceptor;
import com.example.oauth2example.config.interceptors.UserIdPathVariableInterceptor;
import com.example.oauth2example.jwt.resolvers.JWTClaimsSetResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final BearerInterceptor bearerInterceptor;
    private final UserIdPathVariableInterceptor userIdPathVariableInterceptor;
    private final MessageSource messageSource;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(bearerInterceptor)
                .addPathPatterns("/oauth2/v1/userinfo")
                .addPathPatterns("/users/v1/**");
        registry.addInterceptor(userIdPathVariableInterceptor).addPathPatterns("/users/v1/**");
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new JWTClaimsSetResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);

        return factory;
    }
}
