package com.zuma.smssender.listener;

import com.zuma.smssender.socket.SocketConnectBootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * author:ZhengXing
 * datetime:2017/11/23 0023 14:37
 * 容器启动监听器
 */
@Component
@Slf4j
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SocketConnectBootstrap socketConnectBootstrap;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //启动bootstrap,连接到服务器
        socketConnectBootstrap.start();
    }
}
