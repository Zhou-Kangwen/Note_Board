package com.tal.note_board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NoteBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteBoardApplication.class, args);
    }

}
