package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.UserFile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserFileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<UserFile> getUserFiles(int userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insert(UserFile userFile);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    UserFile findById(int fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void delete(int fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    UserFile getFile(String filename);
}
