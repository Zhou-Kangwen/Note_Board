package com.tal.note_board.service.Impl;

import com.tal.note_board.dao.mysql.UserMapper;
import com.tal.note_board.dao.pojo.User;
import com.tal.note_board.service.UserService;
import com.tal.note_board.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisUtil redisUtil;


    @Override
    public boolean register(User user) throws Exception {
        //接受查询结果
        User user1;
        try {
            user1 = userMapper.findUserByName(user.getUserName());
            //判断是否存在用户名
            if (user1 == null) {
                try {
                    userMapper.save(user);
                } catch (Exception e) {
                    log.error("存储数据失败！");
                    throw new Exception("存储数据错误！");
                }
                return true;
            }
        } catch (Exception e) {
            log.error("查询失败");
            throw new Exception("查询用户名失败！");
        }
        return false;
    }


    @Override
    public boolean login(String userName, String password) throws Exception {

        User user;
        try {
            user = userMapper.findUserByUserNameAndPassword(userName, password);
        } catch (Exception e) {
            log.error("查询失败");
            throw new Exception("查询错误！");
        }

        if (user == null) {
            return false;
        }
        return true;
    }

}
