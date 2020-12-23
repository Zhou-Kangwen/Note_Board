package com.tal.note_board.dao.mysql.Impl;

import com.tal.note_board.dao.mysql.UserMapper;
import com.tal.note_board.dao.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserMapperImpl implements UserMapper {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public User findUserByName(String userName) {
        User user;
        String sql = "select * from tal_user where username = ? limit 1";
        user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), userName);

        return user;
    }

    @Override
    public void save(User user){
        String sql = "insert into tal_user (username, password, email,gender) values(?,?,?,?)";

        jdbcTemplate.update(sql, user.getUserName(),
                user.getPassword(),
                user.getEmail(),
                user.getGender()
        );

    }

    @Override
    public User findUserByUserNameAndPassword(String userName, String password){
        String sql = "select * from tal_user where username = ? and password = ?";

        User user;
        user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), userName, password);

        return user;
    }


}
