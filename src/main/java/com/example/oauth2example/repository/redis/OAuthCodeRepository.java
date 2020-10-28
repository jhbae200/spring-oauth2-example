package com.example.oauth2example.repository.redis;

import com.example.oauth2example.vo.OAuthCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class OAuthCodeRepository {
    private static final String PREFIX = "OAUTH_CODE:";
    private final RedisTemplate<String, OAuthCode> redisTemplate;
    private final ValueOperations<String, OAuthCode> valueOperations;

    public OAuthCodeRepository(@Autowired RedisTemplate<String, OAuthCode> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.redisTemplate.opsForValue();
    }

    public void save(OAuthCode oAuthCode) {
        String key = PREFIX + oAuthCode.getClientSeq() + ":" + oAuthCode.getCode();
        valueOperations.set(key, oAuthCode);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    public OAuthCode get(int clientSeq, String code) {
        String key = PREFIX + clientSeq + ":" + code;
        return valueOperations.get(key);
    }

    public void delete(OAuthCode oAuthCode) {
        String key = PREFIX + oAuthCode.getClientSeq() + ":" + oAuthCode.getCode();
        Boolean wasRemoved = redisTemplate.delete(key);
        if (wasRemoved == null || !wasRemoved) {
            log.debug("key: {}, key is absent.", key);
        }
    }
}
