package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Insert("INSERT INTO USERS (username, password, salt, firstname, lastname) VALUES(#{username}, #{password}, #{salt}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);

    @Update("UPDATE USERS SET username = #{username}, firstname = #{firstname}, lastname = #{lastname} WHERE userid = #{userid}")
    User update(User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userid}")
    void delete(@Param("id") int id);

}
