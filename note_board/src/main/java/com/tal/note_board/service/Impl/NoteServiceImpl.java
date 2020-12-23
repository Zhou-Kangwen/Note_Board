package com.tal.note_board.service.Impl;

import com.tal.note_board.dao.mongo.NoteMapper;
import com.tal.note_board.dao.pojo.Note;
import com.tal.note_board.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteMapper noteMapper;

    @Override
    public boolean saveNote(int user_id, String content) throws Exception {
        Note note = new Note(user_id, content);
        try {
            noteMapper.saveNote(note);
        } catch (Exception e) {
            log.error("存数据失败");
            throw new Exception("存数据错误！");
        }
        return true;

    }

    @Override
    public boolean deleteNoteById(int user_id , String _id) throws Exception {
        boolean res;
        try {
            res = noteMapper.deleteNoteById(user_id,_id);
        } catch (Exception e) {
            log.error("删除失败！");
            throw new Exception("删除数据错误！");
        }

        return res;
    }

    @Override
    public List<Note> findAll(int page) throws Exception {
        List<Note> all;
        try {
            all = noteMapper.findAll(page);
        } catch (Exception e) {
            log.error("查询数据失败");
            throw new Exception("查询数据错误！");
        }
        return all;
    }

    @Override
    public List<Note> findUserNote(int user_id, int page) throws Exception {
        List<Note> userNote;
        try {
            userNote = noteMapper.findByUserId(user_id, page);
        } catch (Exception e) {
            log.error("查询数据失败");
            throw new Exception("查询数据错误！");
        }
        return userNote;
    }
}
