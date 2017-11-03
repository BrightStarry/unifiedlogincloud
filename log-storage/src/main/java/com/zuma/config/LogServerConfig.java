package com.zuma.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * author:Administrator
 * datetime:2017/11/1 0001 09:05
 * 服务端配置类
 */
@Component
@ConfigurationProperties(prefix = "logServerConfig")
public class LogServerConfig {
    public static Integer PORT = 8080;//端口号
    public static Integer SO_BACKLOG = 128;//TCP第一二握手队列大小
    public static Integer SO_SNDBUF = 32768;//发送缓冲区大小
    public static Integer SO_RCVBBUF = 32768;//接收缓冲区大小
    public static Integer RETRY_NUM = 5;//自动重连次数，超过后停止n秒再次重连
    public static Integer STOP_TIME = 5000;//重连x次失败后，暂停重连秒数
    public static Integer READ_IDLE_TIMEOUT = 20;//读取空闲超时时间,秒
    public static Integer THREAD_NUM = 10;//服务端线程数
    public static Integer MAX_THREAD_NUM = 20;//服务端最大线程数（线程池全满后，在拒绝策略中扩充）

    
}
