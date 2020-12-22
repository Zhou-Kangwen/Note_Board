package com.tal.note_board.service.Impl;

import com.tal.note_board.dao.mongo.NoteMapper;
import com.tal.note_board.dao.pojo.Note;
import com.tal.note_board.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public boolean saveNote(int user_id, String content) {
        Note note = new Note(user_id, content);
        try {
            //try catch
            noteMapper.saveNote(note);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @Override
    public boolean deleteNoteById(int user_id , String _id) {

        try {
            if (noteMapper.deleteNoteById(user_id,_id)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Note> findAll(int page) {
        List<Note> all = null;
        try {
            all = noteMapper.findAll(page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

    @Override
    public List<Note> findUserNote(int user_id, int page) {
        List<Note> userNote = null;
        try {
            userNote = noteMapper.findByUserId(user_id, page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userNote;
    }
}
