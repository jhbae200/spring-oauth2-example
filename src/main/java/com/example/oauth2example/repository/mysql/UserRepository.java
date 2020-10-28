package com.example.oauth2example.repository.mysql;

import com.example.oauth2example.dao.mysql.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {
    @Select("SELECT UI.id FROM UserIds UI LEFT JOIN User U on UI.userSeq = U.seq WHERE UI.id = #{email} AND UI.type = #{type} AND U.deletedDate IS NULL LIMIT 1")
    String selectIdByEmail(@Param("email") String email, @Param("type") int type);

    @Select("SELECT nickname FROM User WHERE nickname = #{nickname} AND deletedDate IS NULL")
    String selectNicknameByNickname(@Param("nickname") String nickname);

    @Select("SELECT * FROM User U INNER JOIN UserIds UI on U.seq = UI.userSeq WHERE UI.id = #{id} AND UI.type = #{type}")
    User selectByIdAndType(@Param("id") String id, @Param("type") int type);
}
