package com.example.oauth2example.component;

import com.example.oauth2example.dao.mysql.OAuthClient;
import com.example.oauth2example.dao.mysql.OAuthClientUser;
import com.example.oauth2example.dao.mysql.User;
import com.example.oauth2example.exception.DBException;
import com.example.oauth2example.repository.mysql.OAuthClientUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class OAuthClientUserComponent {
    private final OAuthClientUserRepository oAuthClientUserRepository;
    public OAuthClientUser getOrCreateOAuthClientUser(OAuthClient oAuthClient, User user) throws DBException {
        OAuthClientUser oAuthClientUser = oAuthClientUserRepository.select1stByUserSeqAndClientSeq(user.getSeq(), oAuthClient.getSeq());
        if (oAuthClientUser == null) {
            oAuthClientUser = new OAuthClientUser();
            oAuthClientUser.setUserSeq(user.getSeq());
            oAuthClientUser.setClientSeq(oAuthClient.getSeq());
            oAuthClientUser.setClientUserId(UUID.randomUUID().toString());
            int result = oAuthClientUserRepository.insert(oAuthClientUser);
            if (result != 1) throw new DBException("Data Insert Exception.", result);
//            this.insertClientUserLog(oAuthClientUser.getUserSeq(), oAuthClientUser.getClientUserId(), oAuthClient.getSeq(), OAuthUserAction.SIGN.getType(), null, os);
        }
        return oAuthClientUser;
    }

}
