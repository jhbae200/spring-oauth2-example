package com.example.oauth2example.repository.mysql;

import com.example.oauth2example.dao.mysql.OAuthClientUri;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OAuthClientUriRepository {
    @Select("SELECT seq, clientSeq, redirectUri FROM OAuthClientUri WHERE clientSeq = #{clientSeq} AND redirectUri = #{redirectUri} LIMIT 1")
    OAuthClientUri select1stByClientSeqAndRedirectUri(@Param("clientSeq") Long clientSeq, @Param("redirectUri") String redirectUri);
}
