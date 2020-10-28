package com.example.oauth2example.repository.mysql;

import com.example.oauth2example.dao.mysql.OAuthClient;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OAuthClientRepository {
    @Select("SELECT seq, clientId, clientSecret, serviceName, innerService, createdDate, deletedDate FROM OAuthClient WHERE clientId = #{clientId} AND deletedDate IS NULL")
    OAuthClient select1stByClientId(String clientId);

    @Select("SELECT seq, clientId, clientSecret, serviceName, innerService, createdDate, deletedDate FROM OAuthClient WHERE seq = #{seq} AND deletedDate IS NULL")
    OAuthClient select1stBySeq(int seq);
}
