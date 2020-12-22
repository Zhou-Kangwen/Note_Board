package com.tal.note_board.dao.pojo;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;


public class Note implements Serializable {

    private String _id;

    @NotEmpty
    private Integer user_id;
    @NotEmpty
    private String content;

    private Date gmt_create;


    public Note(@NotEmpty int user_id, @NotEmpty String content) {
        this.user_id = user_id;
        this.content = content;
        this.gmt_create = new Date(System.currentTimeMillis());
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public void setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id='" + _id + '\'' +
                ", user_id=" + user_id +
                ", content='" + content + '\'' +
                ", gmt_create=" + gmt_create +
                '}';
    }
}
