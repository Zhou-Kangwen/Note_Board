package com.tal.note_board.dao.mongo;

import com.tal.note_board.dao.pojo.Note;

import java.util.List;

public interface NoteMapper {
    /**
     * @param note
     * @return
     */
    void saveNote(Note note);

    /**
     * 搜索全部Note
     *
     * @return
     */
    List<Note> findAll(int page);

    /**
     * 获取对应user_id的note
     *
     * @param user_id
     * @return
     */
    List<Note> findByUserId(int user_id, int page);

    /**
     * 删除对应objectID的note
     *
     * @param objectId
     * @return
     */
    boolean deleteNoteById(int user_id, String objectId);

}