package com.example.oauth2example.component;


import com.example.oauth2example.MessageSourceImpl;
import com.example.oauth2example.dao.mysql.OAuthClient;
import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import com.example.oauth2example.dto.Parameters;
import com.example.oauth2example.exception.DBException;
import com.example.oauth2example.repository.RefreshTokenRepository;
import com.example.oauth2example.validator.ValidatorCode;
import com.example.oauth2example.vo.OAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthRefreshTokenComponent {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MessageSourceImpl messageSource;

    public OAuthRefreshToken createRefreshToken(OAuthClient oAuthClient, OAuthUserInfo userInfo, String scope, String nonce) throws DBException {
        OAuthRefreshToken oAuthRefreshToken = OAuthRefreshToken.create(oAuthClient, userInfo.getUserSeq(), userInfo.getType(), userInfo.getClientUserId(), scope, nonce);

        int count = refreshTokenRepository.deleteByClientSeqAndUserId(userInfo.getClientUserId(), oAuthClient.getSeq());
        if (count > 0)
            log.debug("{} user refresh token count:{} deleted.", userInfo.getClientUserSeq(), count);

        int result = refreshTokenRepository.insert(oAuthRefreshToken);
        if (result != 1) {
            throw new DBException("Data Insert Exception.", result);
        }

        log.debug("{} user refresh token seq:{} inserted.", userInfo.getClientUserSeq(), oAuthRefreshToken.getSeq());

        return oAuthRefreshToken;
    }

    public OAuthRefreshToken getRefreshToken(String refresh_token, BindingResult bindingResult) throws BindException {
        OAuthRefreshToken oAuthRefreshToken = refreshTokenRepository.select1stByJti(refresh_token);
        if (oAuthRefreshToken == null) {
            bindingResult.rejectValue(Parameters.REFRESH_TOKEN, ValidatorCode.INVALID_REFRESH_TOKEN.getErrorCode(),
                    Objects.requireNonNull(messageSource.getMessage(ValidatorCode.INVALID_REFRESH_TOKEN)));
            throw new BindException(bindingResult);
        }
        refreshTokenRepository.updateAccessDate(oAuthRefreshToken);
        return oAuthRefreshToken;
    }

    /**
     * refreshToken revoke.
     *
     * @param token refresh token(uuid format)
     * @throws ResponseStatusException refresh token이 없으면 403
     */
    public void revokeRefreshToken(String token) {
        OAuthRefreshToken oAuthRefreshToken = refreshTokenRepository.select1stByJti(token);
        if (oAuthRefreshToken == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        int count = refreshTokenRepository.delete(oAuthRefreshToken);
        if (count == 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
