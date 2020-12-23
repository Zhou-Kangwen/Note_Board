package com.tal.note_board.controller;

import com.tal.note_board.dao.pojo.Note;
import com.tal.note_board.service.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;



    @PostMapping(value = "/write")
    public boolean writeNote(@RequestBody Note note) throws Exception {
        log.info("登陆入参： Note 对象");
        boolean res = noteService.saveNote(note.getUser_id(), note.getContent());
        log.info("返回结果为布尔值，表示留言是否成功："+ res);
        return res;
    }

    @GetMapping(value = "/findAll/{page}")
    public List<Note> findAll(@PathVariable(name = "page") int page) throws Exception {

        log.info("查询所有参数： page(页码)");
        List<Note> list = noteService.findAll(page);
        log.info("返回结果为list，表示第"+page+"页的全部留言： "+ list);

        return list;
    }

    @GetMapping(value = "/findUserNote/{user_id}/{page}")
    public List<Note> findUserNote(@PathVariable(name = "user_id") int user_id, @PathVariable(name = "page") int page) throws Exception {

        log.info("查询所有参数：user_id(用户id) page(页码)");
        List<Note> list = noteService.findUserNote(user_id, page);
        log.info("返回结果为list，表示用户"+user_id+"的第"+page+"页的全部留言： "+ list);

        return list;
    }

    @DeleteMapping(value = "/delete/{user_id}/{_id}")
    public boolean deleteNoteById(@PathVariable(name = "user_id") int user_id, @PathVariable(name = "_id") String _id) throws Exception {

        log.info("查询所有参数：user_id(用户id) _id(mongo中的objectID)");
        boolean res = noteService.deleteNoteById(user_id,_id);
        log.info("返回结果为布尔值，表示删除是否成功："+ res);

        return res;
    }

}
