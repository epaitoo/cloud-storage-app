package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> getUserNotes(int userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insert(Note note);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    Note findById(int noteid);

    @Update("UPDATE NOTES SET notetitle= #{notetitle}, notedescription=#{notedescription} WHERE noteid = #{noteid}")
    void update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    void delete(int noteid);

}
