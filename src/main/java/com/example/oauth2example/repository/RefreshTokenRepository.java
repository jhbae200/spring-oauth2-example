package com.example.oauth2example.repository;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.example.oauth2example.repository.mysql.OAuthRefreshTokenRepository;
import com.example.oauth2example.repository.redis.RedisRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RefreshToken Redis, DB 캐시 연동을 위해 작성
 * Redis에 값이 있으면 값 return
 * Redis에 값이 없다면 DB 값 return
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final int limit = 3;
    /**
     * Mybatis Repository (Database)
     */
    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;
    /**
     * Redis Repository
     */
    private final RedisRefreshTokenRepository redisRefreshTokenRepository;

    public int insert(OAuthRefreshToken oAuthRefreshToken) {
        int result = oAuthRefreshTokenRepository.insert(oAuthRefreshToken);
        redisRefreshTokenRepository.save(oAuthRefreshToken);
        return result;
    }

    public int delete(List<OAuthRefreshToken> refreshTokens) {
        int count = 0;
        count = oAuthRefreshTokenRepository.deleteBySeqList(refreshTokens.stream().map(OAuthRefreshToken::getSeq).collect(Collectors.toList()));
        redisRefreshTokenRepository.deleteList(refreshTokens);
        return count;
    }

    public int delete(OAuthRefreshToken refreshToken) {
        int count = 0;
        count = oAuthRefreshTokenRepository.deleteBySeq(refreshToken.getSeq());
        redisRefreshTokenRepository.delete(refreshToken);
        return count;
    }

    /**
     * delete by client seq and user Id.
     *
     *  limit is max refreshToken count.
     *  db에 최대 limit-1 만큼의 count만 존재하게 나머지 row는 삭제
     * <p>
     * List -> subList -> db delete, redis delete
     *
     * 다중 로그인 처리 구현 시 - 최대 다중 로그인 수 n개라 정해 놓았을 때, 현재 DB에 userId와 clientSeq 별로 refreshToken COUNT 수가 n개 이상이면
     * SELECT 시 ORDER BY createdDate ASC LIMIT 1 걸어서 DELETE 시킨 후 INSERT 한다.
     *
     * @param userId
     * @param clientSeq
     * @return
     */
    public int deleteByClientSeqAndUserId(String userId, Long clientSeq) {
        int count = 0;
        List<OAuthRefreshToken> oAuthRefreshTokens = oAuthRefreshTokenRepository.selectByClientSeqAndUserId(userId, clientSeq);
        if (oAuthRefreshTokens.size() >= limit) {
            count = delete(oAuthRefreshTokens.subList(0, oAuthRefreshTokens.size() + 1 - limit));
        }

        if(oAuthRefreshTokens.size() > 0) {
            count = delete(oAuthRefreshTokens);
        }

        return count;
    }

    public OAuthRefreshToken select1stByJti(String refreshToken) {
        return oAuthRefreshTokenRepository.select1stByJti(refreshToken);
    }

    public OAuthRefreshToken selectOneByUserIdAndClientId(String userId, String clientId) {
        return oAuthRefreshTokenRepository.selectOneByUserIdAndClientId(userId, clientId);
    }

    public List<OAuthRefreshToken> selectByUserSeq(long userSeq) {
        return oAuthRefreshTokenRepository.selectByUserSeq(userSeq);
    }

    public void updateAccessDate(OAuthRefreshToken oAuthRefreshToken) {
        oAuthRefreshToken.setAccessAt(new Date());
        oAuthRefreshTokenRepository.updateAccessDate(oAuthRefreshToken.getSeq(), oAuthRefreshToken.getAccessAt());
        redisRefreshTokenRepository.save(oAuthRefreshToken);
    }

    public OAuthRefreshToken select(String audience, String userId, long seq) {
        OAuthRefreshToken oAuthRefreshToken;
        oAuthRefreshToken = redisRefreshTokenRepository.get(audience, userId, seq);
        if (oAuthRefreshToken == null) {
            log.debug("missed key: {}, {}, {}", audience, userId, seq);
            oAuthRefreshToken = oAuthRefreshTokenRepository.selectBySeq(seq);
        }
        return oAuthRefreshToken;
    }

    public void deleteOldRefreshToken() {
        List<OAuthRefreshToken> deleteRefreshTokens = oAuthRefreshTokenRepository.selectByOld();
        if (deleteRefreshTokens != null && deleteRefreshTokens.size() > 0) {
            redisRefreshTokenRepository.deleteList(deleteRefreshTokens);
            int count = oAuthRefreshTokenRepository.deleteBySeqList(deleteRefreshTokens.stream().map(OAuthRefreshToken::getSeq).collect(Collectors.toList()));
            if (count > 0)
                log.debug("old refresh token count:{} deleted.", count);
        }
    }
}
