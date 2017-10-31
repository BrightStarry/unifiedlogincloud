package com.zuma.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 17:06
 */
@EnableBinding(Sink.class)
@Slf4j
public class KafkaReceiver {

    @StreamListener(Sink.INPUT_A)
    public void receive(String vote) {
        log.info("receive message = " + vote);
    }
}
