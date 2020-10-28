package com.example.oauth2example.data.vo;

import com.example.oauth2example.data.dao.TestOAuthClient;
import com.example.oauth2example.data.dao.TestOAuthClientUri;
import com.example.oauth2example.oauth2.CodeChallengeType;
import com.example.oauth2example.utils.SHA256Utils;
import com.example.oauth2example.vo.OAuthCode;
import org.apache.logging.log4j.util.Strings;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TestOAuthCode {
    public static final List<String> scope = Arrays.asList("open_id", "profile");
    public static String code = "testCode";
    public static String state = "testState";
    public static String codeChallenge = "testCodeChallenge";
    public static String nonce = "testNonce";

    public static OAuthCode getTestData() {
        OAuthCode oAuthCode = new OAuthCode();
        oAuthCode.setClientSeq(TestOAuthClient.seq);
        oAuthCode.setUserId(TestOAuthUserInfo.id);
        oAuthCode.setUserType(TestOAuthUserInfo.type);
        oAuthCode.setCode(code);
        oAuthCode.setRedirectUri(TestOAuthClientUri.redirectUri);
        oAuthCode.setState(state);
        oAuthCode.setCreatedDate(new Date());
        oAuthCode.setCodeChallengeType(CodeChallengeType.PLAIN);
        oAuthCode.setCodeChallenge(codeChallenge);
        oAuthCode.setScope(Strings.join(scope, ' '));
        oAuthCode.setNonce(nonce);
        return oAuthCode;
    }

    public static OAuthCode getTestDataS256() throws NoSuchAlgorithmException {
        OAuthCode oAuthCode = new OAuthCode();
        oAuthCode.setClientSeq(TestOAuthClient.seq);
        oAuthCode.setUserId(TestOAuthUserInfo.id);
        oAuthCode.setUserType(TestOAuthUserInfo.type);
        oAuthCode.setCode(code);
        oAuthCode.setRedirectUri(TestOAuthClientUri.redirectUri);
        oAuthCode.setState(state);
        oAuthCode.setCreatedDate(new Date());
        oAuthCode.setCodeChallengeType(CodeChallengeType.SHA256);
        oAuthCode.setCodeChallenge(SHA256Utils.toSHA256Base64(codeChallenge));
        oAuthCode.setScope(Strings.join(scope, ' '));
        oAuthCode.setNonce(nonce);
        return oAuthCode;
    }
}
