package com.tal.note_board.rocketmq;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

@Component
public class RegisterProducer {
    /**
     * 生产组,生产者必须在生产组内
     */
    private String producerGroup = "register_producer_group";

    private DefaultMQProducer producer;

    public RegisterProducer() {
        producer = new DefaultMQProducer(producerGroup);
        // 指定nameServer地址,多个地址之间以 ; 隔开
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        start();
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    /**
     * 对象在使用之前必须调用一次,并且只能初始化一次
     */
    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一般在应用上下文,使用上下文监听器,进行关闭
     */
    public void shutdown() {
        producer.shutdown();
    }
}
