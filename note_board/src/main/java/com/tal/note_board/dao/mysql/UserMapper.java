package com.tal.note_board.dao.mysql;

import com.tal.note_board.dao.pojo.User;

public interface UserMapper {

    /**
     * 通过用户名查user
     * @param userName
     * @return
     * @throws Exception
     */
    User findUserByName(String userName);

    /**
     * 存用户信息
     * @param user
     * @throws Exception
     */
    void save(User user);

    /**
     * 验证用户名和密码
     * @param userName
     * @param password
     * @return
     * @throws Exception
     */
    User findUserByUserNameAndPassword(String userName, String password);
}
