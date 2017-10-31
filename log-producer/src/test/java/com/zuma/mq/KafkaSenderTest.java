package com.zuma.mq;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 16:26
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class KafkaSenderTest {

    @Autowired
    private KafkaSender kafkaSender;

    @Test
    public void sendMessage() throws Exception {
        for (int i = 0; i < 10; i++) {
            kafkaSender.sendMessage("test" + i);
        }
    }

}