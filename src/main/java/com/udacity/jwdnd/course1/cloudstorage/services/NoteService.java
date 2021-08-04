package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    /*
     * Get all the Notes Created by the User
     *
     * @param userId the id of the user
     * @return List<Notes>
     * */
    public List<Note> getUserNotes(int userid) {
        return noteMapper.getUserNotes(userid);
    }

    /*
     * Create A Note
     *
     * @param Note note, int userid
     * @return int row of the Table
     * */
    public int createNote(Note note, int userid) {
        return noteMapper.insert(new Note(null, note.getNotetitle(), note.getNotedescription(), userid));
    }


    /*
     * Show A Note
     *
     * @param int id
     * @return Note note
     * */
    public Note getNote(int id) { return noteMapper.findById(id); }


    /*
     * Update A Note
     *
     * @param Note note
     * @return void
     * */
    public void updateNote(Note note, int userid) {
        noteMapper.update(new Note(note.getNoteid(), note.getNotetitle(), note.getNotedescription(), userid));
    }

    /*
     * Delete A Note
     *
     * @param int id
     * @return void
     * */
    public void deleteNote(int id) { noteMapper.delete(id); }
}
