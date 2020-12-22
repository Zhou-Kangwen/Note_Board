package com.tal.note_board.service;

import com.tal.note_board.dao.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * @param userName
     * @param password
     * @return
     */
    boolean login(String userName, String password);

}
