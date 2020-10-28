package com.example.oauth2example.config;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.example.oauth2example.vo.OAuthCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Value("${spring.redis.host}") String host, @Value("${spring.redis.port}") Integer port) {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, OAuthRefreshToken> redisTemplateRefreshToken(@Autowired RedisConnectionFactory factory) {
        RedisTemplate<String, OAuthRefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OAuthRefreshToken.class));
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, OAuthCode> redisTemplateOAuthCode(@Autowired RedisConnectionFactory factory) {
        RedisTemplate<String, OAuthCode> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(OAuthCode.class));
        return redisTemplate;
    }
}
