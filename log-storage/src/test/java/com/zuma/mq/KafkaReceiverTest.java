package com.zuma.mq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 17:08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class KafkaReceiverTest {

    @Autowired
    private KafkaReceiver kafkaReceiver;

    @Test
    public void receive() throws Exception {
        kafkaReceiver.receive(Sink.INPUT_A);
    }

}