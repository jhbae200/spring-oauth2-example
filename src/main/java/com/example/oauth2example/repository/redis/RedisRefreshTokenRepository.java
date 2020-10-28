package com.example.oauth2example.repository.redis;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class RedisRefreshTokenRepository {
    private static final String PREFIX = "REFRESH_V1:";
    private final RedisTemplate<String, OAuthRefreshToken> redisTemplate;
    private final ValueOperations<String, OAuthRefreshToken> valueOperations;

    public RedisRefreshTokenRepository(@Autowired RedisTemplate<String, OAuthRefreshToken> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = this.redisTemplate.opsForValue();
    }

    public void save(OAuthRefreshToken oAuthRefreshToken) {
        String key = PREFIX + oAuthRefreshToken.getClientId() + ":" + oAuthRefreshToken.getUserId() + ":" + oAuthRefreshToken.getSeq();
        valueOperations.set(key, oAuthRefreshToken);
    }

    public OAuthRefreshToken get(String clientId, String userId, long seq) {
        String key = PREFIX + clientId + ":" + userId + ":" + seq;

        return valueOperations.get(key);
    }

    public void delete(OAuthRefreshToken oAuthRefreshToken) {
        String key = PREFIX + oAuthRefreshToken.getClientId() + ":" + oAuthRefreshToken.getUserId() + ":" + oAuthRefreshToken.getSeq();
        Boolean wasRemoved = redisTemplate.delete(key);
        if (wasRemoved == null || !wasRemoved) {
            log.debug("key: {}, key is absent.", key);
        }
    }

    public void deleteList(List<OAuthRefreshToken> oAuthRefreshTokenList) {
        Long removeCount = redisTemplate.delete(oAuthRefreshTokenList.stream()
                .map(value -> PREFIX + value.getClientId() + ":" + value.getUserId() + ":" + value.getSeq())
                .collect(Collectors.toList()));
        if (removeCount == oAuthRefreshTokenList.size()) {
            log.debug("remove count: {}, actual count: {}", removeCount, oAuthRefreshTokenList.size());
        }
    }
}
