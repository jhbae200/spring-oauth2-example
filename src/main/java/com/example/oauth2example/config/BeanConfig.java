package com.example.oauth2example.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeanConfig {
    @Bean
    public Hashids hashids(@Value("${hashids.salt}") String salt, @Value("${hashids.length}") int length) {
        return new Hashids(salt, length);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(clientHttpRequestFactory());
    }

    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(10000); // milliseconds
        factory.setConnectTimeout(10000);// milliseconds

        HttpClient httpClient = HttpClientBuilder.create()//
                .setMaxConnTotal(50)
                .setMaxConnPerRoute(50)
                .build();
        factory.setHttpClient(httpClient);
        return factory;
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
}
