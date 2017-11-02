package com.zuma.sender;

import com.zuma.dto.LogMessage;
import com.zuma.netty.LogClientBootstrap;
import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.*;

/**
 * author:Administrator
 * datetime:2017/11/2 0002 11:25
 */
public class LogSenderTest {
    @Test
    public void send() throws Exception {

        LogClientBootstrap.start();
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    while(true){
                        LogMessage logMessage = LogMessage.builder()
                                .serviceId(new Random().nextInt(4) + 1)
                                .moduleName(new String[]{"A", "B", "C", "D"}[new Random().nextInt(4)])
                                .content("2017-11-" + new String[]{"01", "02", "11", "13", "22", "10", "09"}[new Random().nextInt(7)] + " " + new String[]{"01", "02", "11", "13", "22", "10", "09"}[new Random().nextInt(7)] + ":08:21.122  INFO 7848 --- [ntLoopGroup-1-2] com.zuma.handler.ServerHandler           : server接收到bba283b0消息:LogMessage(serviceId=null, channel=null, moduleId=null, time=Wed Nov 01 14:08:21 CST 2017, content=2  17:19:39.663 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.pageSize: 8192)")
                                .date(new Date[]{null, new Date()}[new Random().nextInt(2)])
                                .build();
                        if(LogSender.isAvailable())
                            LogSender.send(logMessage);
                    }
                }
            });
        }
        Thread.sleep(Integer.MAX_VALUE);
    }

}