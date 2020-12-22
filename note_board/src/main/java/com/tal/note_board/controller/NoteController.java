package com.tal.note_board.controller;

import com.tal.note_board.dao.pojo.Note;
import com.tal.note_board.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;



    @PostMapping(value = "/write")
    public ResponseEntity<Map<String, Object>> writeNote(@RequestBody Note note) {
        Map<String, Object> m = new HashMap<>();
        if (noteService.saveNote(note.getUser_id(), note.getContent())) {
            m.put("Message", "发表留言成功！");
            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
        } else {
            m.put("Message", "某种神秘力量阻止了您留言！请检查您的留言格式！");
            return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(value = "/findAll/{page}")
    public ResponseEntity<Map<String, Object>> findAll(@PathVariable(name = "page", required = true) int page) {
        Map<String, Object> m = new HashMap<>();
        List<Note> list = noteService.findAll(page);
        if (list == null || list.size() == 0) {
            m.put("Message", "本页暂无留言，您可以尝试留言喔！");
            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
        } else {
            m.put("All Notes", list);
            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
        }
    }

    @GetMapping(value = "/findUserNote/{user_id}/{page}")
    public ResponseEntity<Map<String, Object>> findUserNote(@PathVariable(name = "user_id", required = true) int user_id, @PathVariable(name = "page", required = true) int page) {
        Map<String, Object> m = new HashMap<>();
        List<Note> list = noteService.findUserNote(user_id, page);
        if (list == null || list.size() == 0) {
            m.put("Message", "此页暂无留言，您可以尝试留言喔！");
            return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
        } else {
            m.put("All Notes of User " + user_id, list);
            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping(value = "/delete/{user_id}/{_id}")
    public ResponseEntity<Map<String, Object>> deleteNoteById(@PathVariable(name = "user_id", required = true) int user_id, @PathVariable(name = "_id", required = true) String _id) {
        Map<String, Object> m = new HashMap<>();

        if (noteService.deleteNoteById(user_id,_id)) {
            m.put("Message", "删除成功！");
            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);
        } else {
            m.put("Message", "当前用户没有_Id为：" + _id + " 的数据！");
            return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
        }
    }

}
