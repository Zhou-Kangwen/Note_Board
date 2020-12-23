package com.tal.note_board.controller;

import com.tal.note_board.dao.pojo.User;
import com.tal.note_board.rocketmq.JmsConfig;
import com.tal.note_board.rocketmq.RegisterProducer;
import com.tal.note_board.service.UserService;
import com.tal.note_board.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/note")
public class RegisterController {


    @Autowired
    private UserService userService;
    @Autowired
    private EncryptUtil encryptUtil;
    @Autowired
    private RegisterProducer registerProducer;




    @PostMapping(value = "/register")
    public boolean register(@RequestBody @Validated User user) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        log.info("注册方法入参：User对象");
        boolean res = userService.register(user);

        if (res) {
            log.info("注册成功，调用MQ发送邮件给当前邮箱！");
            callback(user.getEmail());
            log.info("注册成功并给用户成功发送邮件！");
            return true;
        }
        log.info("注册失败！");
        return false;

    }

    @GetMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(String userName, String password) throws Exception{

        log.info("登陆方法入参：username（用户名），password（密码）");
        Map<String,Object> m = new HashMap<>();
        if (userService.login(userName, password)) {
            m.put("Message", "登陆成功！");
            //创建token
            long l = System.currentTimeMillis() + (long) (1000 * 60 * 30);
            String res = userName + "_" + l;
            String token = encryptUtil.Base64Encode(res);
            m.put("token", token);

            return new ResponseEntity<>(m, HttpStatus.ACCEPTED);

        } else {
            m.put("Error", "用户名或密码错误！");
            return new ResponseEntity<>(m, HttpStatus.BAD_REQUEST);
        }

    }

    public void callback(String email) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        // 创建消息  主题   二级分类   消息内容好的字节数组
        Message message = new Message(JmsConfig.TOPIC, "tag", (email).getBytes());
        SendResult send = registerProducer.getProducer().send(message);

    }

}
