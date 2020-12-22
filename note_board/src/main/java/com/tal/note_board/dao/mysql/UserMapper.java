package com.tal.note_board.dao.mysql;

import com.tal.note_board.dao.pojo.User;

public interface UserMapper {

    /**
     * 注册：通过用户名查找用户
     *
     * @param userName
     * @return
     */
    User findUserByName(String userName);

    /**
     * 注册：存储用户
     *
     * @param user
     */
    void save(User user);

    /**
     * 通过账号密码找用户
     *
     * @param userName
     * @param password
     * @return
     */
    User findUserByUserNameAndPassword(String userName, String password);
}
