package com.zuma.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * author:Administrator
 * datetime:2017/10/30 0030 16:13
 */
public interface Source {

    String OUTPUT_A = "sourcea";

    @Output(Source.OUTPUT_A)
    MessageChannel outputA();
}
