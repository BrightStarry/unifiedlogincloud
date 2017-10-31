package com.zuma.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 16:04
 */
public interface Sink {
    String INPUT_A = "testa";

    @Input(Sink.INPUT_A)
    SubscribableChannel inputA();
}
