package com.example.oauth2example.repository.mysql;

import com.example.oauth2example.dao.mysql.OAuthJwtRsaKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface OAuthJwtRsaKeyRepository {
    @Select("SELECT name, privateKey, publicKey FROM OAuthJwtRsaKey")
    List<OAuthJwtRsaKey> selectAll();
}
