package com.zuma.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import javax.validation.constraints.NotNull;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 16:16
 *
 */
@EnableBinding(Source.class)
@Slf4j
public class KafkaSender {
    @Autowired
    private Source source;

    public void sendMessage( String message) {
        source.outputA().send(MessageBuilder.withPayload("message:" + message).build());
    }

}
