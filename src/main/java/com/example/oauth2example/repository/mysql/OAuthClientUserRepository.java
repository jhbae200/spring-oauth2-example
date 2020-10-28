package com.example.oauth2example.repository.mysql;

import com.example.oauth2example.dao.mysql.OAuthClientUser;
import com.example.oauth2example.vo.OAuthUserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OAuthClientUserRepository {
    @Select("SELECT seq, userSeq, clientUserId, clientSeq, createdDate FROM `OAuthClientUser` WHERE userSeq = #{userSeq} AND clientSeq = #{clientSeq}")
    OAuthClientUser select1stByUserSeqAndClientSeq(@Param("userSeq") Long userSeq, @Param("clientSeq") Long clientSeq);

    @Insert("INSERT INTO `OAuthClientUser` (`userSeq`, `clientUserId`, `clientSeq`) VALUES (#{userSeq}, #{clientUserId}, #{clientSeq})")
    @Options(useGeneratedKeys = true, keyProperty = "seq")
    int insert(OAuthClientUser oAuthClientUser);

    @Select("SELECT U.seq AS userSeq, UI.id, U.nickname, UI.type, OCU.clientSeq, OCU.seq AS clientUserSeq, OCU.clientUserId " +
            "FROM `OAuthClientUser` OCU " +
            "INNER JOIN User U ON U.seq = OCU.userSeq AND U.deletedDate IS NULL " +
            "INNER JOIN UserIds UI on OCU.userSeq = UI.userSeq AND UI.type = #{type} " +
            "WHERE OCU.clientUserId = #{clientUserId}")
    OAuthUserInfo select1stByClientAllUserId(@Param("clientUserId") String clientUserId, @Param("type") Integer type);
}
