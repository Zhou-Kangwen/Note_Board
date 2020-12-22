package com.tal.note_board.service.Impl;

import com.tal.note_board.dao.mysql.UserMapper;
import com.tal.note_board.dao.pojo.User;
import com.tal.note_board.service.UserService;
import com.tal.note_board.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;


    @Override
    public boolean register(User user) {
        //接受查询结果
        User user1 = null;

        try {
            user1 = userMapper.findUserByName(user.getUserName());
            //判断是否存在用户名
            if (user1 == null) {
                userMapper.save(user);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean login(String userName, String password) {

        User user = null;
        try {
            user = userMapper.findUserByUserNameAndPassword(userName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (user == null) {
            return false;
        }
        return true;
    }

}
