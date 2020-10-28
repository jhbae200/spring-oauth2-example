package com.example.oauth2example.service;

import com.example.oauth2example.component.OAuthComponent;
import com.example.oauth2example.component.UserComponent;
import com.example.oauth2example.dao.mysql.OAuthClient;
import com.example.oauth2example.dao.mysql.User;
import com.example.oauth2example.dto.OAuthPageDTO;
import com.example.oauth2example.exception.AuthPageException;
import com.example.oauth2example.repository.redis.OAuthCodeRepository;
import com.example.oauth2example.vo.OAuthCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthPageService {
    private final OAuthComponent oAuthComponent;
    private final UserComponent userComponent;
    private final OAuthCodeRepository oAuthCodeRepository;
    private final Hashids hashids;
    @Value("${oauth2.codeChar}")
    private String codeChar;
    @Value("${oauth2.codeStart}")
    private String codeStart;

    public void checkRedirectUri(OAuthPageDTO.GetAuthRequest getAuthRequest) throws AuthPageException {
        try {
            oAuthComponent.validateClientAndRedirectUri(getAuthRequest.getClient_id(), getAuthRequest.getRedirect_uri());
        } catch (IllegalArgumentException e) {
            log.error("Select not found error.", e);
            throw new AuthPageException("access_denied", e.getMessage());
        } catch (Exception e) {
            log.error("error.", e);
            throw new AuthPageException("server_error", e.getMessage());
        }
    }

    /**
     * login process.
     * response query param: https://tools.ietf.org/html/rfc6749#section-4.1.2
     * error code list: https://tools.ietf.org/html/rfc6749#section-4.1.2.1
     *
     * @param postAuthRequest query param(https://tools.ietf.org/html/rfc6749#section-4.1.2)
     * @return redirect URI(error code: https://tools.ietf.org/html/rfc6749#section-4.1.2.1)
     */
    public String postAuth(OAuthPageDTO.PostAuthRequest postAuthRequest, BindingResult bindingResult) throws Exception {
        User user = userComponent.usernamePasswordCheck(postAuthRequest.getUsername(), postAuthRequest.getPassword(), bindingResult);
        return buildRedirectUri(postAuthRequest, user);
    }

    private String buildRedirectUri(OAuthPageDTO.AuthRequest authRequest, User user) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(authRequest.getRedirect_uri());
        if (!"code".equals(authRequest.getResponse_type())) {
            uriBuilder.queryParam("error", "unsupported_response_type");
            return uriBuilder.toUriString();
        }
        OAuthClient oAuthClient = oAuthComponent.validateClientAndRedirectUri(authRequest.getClient_id(), authRequest.getRedirect_uri());
        // client validation
        try {
            // oauth code insert
            OAuthCode oAuthCode = OAuthCode.create(oAuthClient.getSeq(), user.getId(), user.getType(), authRequest);
            oAuthCodeRepository.save(oAuthCode);

            // TODO response_type include token query param add token

            uriBuilder.queryParam("code", this.getCode(oAuthClient.getSeq(), oAuthCode.getCode()));
            if (authRequest.getState() != null && !authRequest.getState().isEmpty()) {
                uriBuilder.queryParam("state", authRequest.getState());
            }

        } catch (IllegalArgumentException e) {
            log.error("Select not found error.", e);
            uriBuilder.queryParam("error", "access_denied");
            uriBuilder.queryParam("error_description", e.getMessage());
        } catch (Exception e) {
            log.error("error.", e);
            uriBuilder.queryParam("error", "server_error");
            uriBuilder.queryParam("error_description", e.getMessage());
        }

        return uriBuilder.toUriString();
    }

    public String getCode(Long clientSeq, String code) {
        return String.format(codeStart + "%s" + codeChar + "%s", hashids.encode(clientSeq), code);
    }
}
