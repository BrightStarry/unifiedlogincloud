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
    public static Integer PORT;//端口号
    public static Integer SO_BACKLOG;//TCP第一二握手队列大小
    public static Integer SO_SNDBUF;//发送缓冲区大小
    public static Integer SO_RCVBBUF;//接收缓冲区大小
    public static Integer RETRY_NUM;//自动重连次数，超过后停止n秒再次重连
    public static Integer STOP_TIME;//重连x次失败后，暂停重连秒数
    public static Integer READ_IDLE_TIMEOUT;//读取空闲超时时间,秒

    public static void setPORT(Integer PORT) {
        LogServerConfig.PORT = PORT;
    }

    public static void setSoBacklog(Integer soBacklog) {
        SO_BACKLOG = soBacklog;
    }

    public static void setSoSndbuf(Integer soSndbuf) {
        SO_SNDBUF = soSndbuf;
    }

    public static void setSoRcvbbuf(Integer soRcvbbuf) {
        SO_RCVBBUF = soRcvbbuf;
    }

    public static void setRetryNum(Integer retryNum) {
        RETRY_NUM = retryNum;
    }

    public static void setStopTime(Integer stopTime) {
        STOP_TIME = stopTime;
    }

    public static void setReadIdleTimeout(Integer readIdleTimeout) {
        READ_IDLE_TIMEOUT = readIdleTimeout;
    }
}
