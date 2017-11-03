package com.zuma.config;

import com.esotericsoftware.kryo.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * author:Administrator
 * datetime:2017/11/1 0001 09:05
 * 服务端配置类
 */
@Component
@ConfigurationProperties(prefix = "logServerConfig")
@Data
public class LogServerConfig {
    private Integer port = 8080;//端口号
    private Integer soBacklog = 128;//TCP第一二握手队列大小
    private Integer soSndbuf = 32768;//发送缓冲区大小
    private Integer soRcvbbuf = 32768;//接收缓冲区大小
    private Integer retryNum = 5;//自动重连次数，超过后停止n秒再次重连
    private Integer stopTime = 5000;//重连x次失败后，暂停重连秒数
    private Integer readIdleTimeout = 20;//读取空闲超时时间,秒
    private Integer threadNum = 5;//服务端线程数
    private Integer maxThreadNum = 10;//服务端最大线程数（线程池全满后，在拒绝策略中扩充）


}
