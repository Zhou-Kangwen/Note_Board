package com.tal.note_board.service;

import com.tal.note_board.dao.pojo.Note;
import com.tal.note_board.dao.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NoteService {
    /**
     * @param user_id
     * @param content
     * @return
     */
    boolean saveNote(int user_id, String content) throws Exception;

    /**
     * @param _id
     */
    boolean deleteNoteById(int user_id, String _id) throws Exception;

    /**
     * @param page
     * @return
     */
    List<Note> findAll(int page) throws Exception;


    List<Note> findUserNote(int user_id, int page) throws Exception;

}
