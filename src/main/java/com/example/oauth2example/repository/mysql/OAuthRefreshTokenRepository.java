package com.example.oauth2example.repository.mysql;

import com.example.oauth2example.dao.mysql.OAuthRefreshToken;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Mapper
@Repository
public interface OAuthRefreshTokenRepository {
    @Insert("INSERT INTO `OAuthRefreshToken` (`jti`, `userSeq`, `userId`, `clientSeq`, `createdDate`, `expireDate`, `scope`, `nonce`, `type`) \n" +
            "VALUES (#{jti}, #{userSeq}, #{userId}, #{clientSeq}, #{createdDate}, #{expireDate}, #{scope}, #{nonce}, #{type})")
    @Options(useGeneratedKeys = true, keyProperty = "seq")
    int insert(OAuthRefreshToken oAuthRefreshToken);

    @Select("SELECT rt.seq, rt.jti, rt.userSeq, rt.userId, rt.clientSeq, ac.clientId, rt.accessDate, rt.createdDate, rt.expireDate, rt.scope, rt.nonce, rt.type \n" +
            "FROM OAuthRefreshToken rt \n" +
            "INNER JOIN OAuthClient ac on rt.clientSeq = ac.seq \n" +
            "WHERE rt.userId = #{userId} AND rt.clientSeq = #{clientSeq}")
    List<OAuthRefreshToken> selectByClientSeqAndUserId(String userId, Long clientSeq);

    @Select("SELECT rt.seq, rt.jti, rt.userSeq, rt.userId, rt.clientSeq, ac.clientId, rt.accessDate, rt.createdDate, rt.expireDate, rt.scope, rt.nonce, rt.type \n" +
            "FROM OAuthRefreshToken rt \n" +
            "INNER JOIN OAuthClient ac on rt.clientSeq = ac.seq \n" +
            "WHERE rt.userSeq = #{userSeq}")
    List<OAuthRefreshToken> selectByUserSeq(long userSeq);

    @Select("SELECT rt.seq, rt.jti, rt.userSeq, rt.userId, rt.clientSeq, ac.clientId, rt.accessDate, rt.createdDate, rt.expireDate, rt.scope, rt.nonce, rt.type \n" +
            "FROM OAuthRefreshToken rt \n" +
            "INNER JOIN OAuthClient ac on rt.clientSeq = ac.seq \n" +
            "WHERE rt.jti = #{jti}")
    OAuthRefreshToken select1stByJti(String jti);

    @Select("SELECT rt.seq, rt.jti, rt.userSeq, rt.userId, rt.clientSeq, ac.clientId, rt.accessDate, rt.createdDate, rt.expireDate, rt.scope, rt.nonce, rt.type \n" +
            "FROM OAuthRefreshToken rt \n" +
            "INNER JOIN OAuthClient ac on rt.clientSeq = ac.seq AND ac.clientId = #{clientId} \n" +
            "WHERE rt.userId = #{userId}")
    OAuthRefreshToken selectOneByUserIdAndClientId(@Param("userId") String userId, @Param("clientId") String clientId);

    @Select("SELECT rt.seq, rt.jti, rt.userSeq, rt.userId, rt.clientSeq, ac.clientId, rt.accessDate, rt.createdDate, rt.expireDate, rt.scope, rt.nonce, rt.type \n" +
            "FROM OAuthRefreshToken rt \n" +
            "INNER JOIN OAuthClient ac on rt.clientSeq = ac.seq \n" +
            "WHERE rt.seq = #{seq}")
    OAuthRefreshToken selectBySeq(long seq);

    @Update("UPDATE OAuthRefreshToken SET accessDate = #{accessDate} WHERE seq = #{seq}")
    void updateAccessDate(@Param("seq") long seq, @Param("accessDate") Date accessDate);

    @Delete("DELETE FROM OAuthRefreshToken WHERE clientSeq = #{clientSeq} ORDER BY createdDate LIMIT #{limit}")
    int deleteByClientSeqLimit(@Param("clientSeq") int clientSeq, @Param("limit") int limit);

    @Delete("DELETE FROM OAuthRefreshToken WHERE seq = #{seq}")
    int deleteBySeq(long seq);

    @Delete("<script>\n" +
            "DELETE FROM OAuthRefreshToken WHERE seq IN \n" +
            "<foreach item=\"seq\" collection=\"seqs\" separator=\",\" open=\"(\" close=\")\">" +
            "#{seq}" +
            "</foreach></script>")
    int deleteBySeqList(@Param("seqs") List<Long> seqs);

    @Select("SELECT rt.seq, rt.jti, rt.userSeq, rt.userId, rt.clientSeq, ac.clientId, rt.accessDate, rt.createdDate, rt.expireDate, rt.scope, rt.nonce, rt.type " +
            "FROM OAuthRefreshToken rt " +
            "INNER JOIN OAuthClient ac on rt.clientSeq = ac.seq " +
            "WHERE accessDate < DATE_SUB(now(), INTERVAL 2 MONTH)")
    List<OAuthRefreshToken> selectByOld();
}
