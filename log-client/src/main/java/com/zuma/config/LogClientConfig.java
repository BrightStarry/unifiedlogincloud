package com.zuma.config;

/**
 * author:ZhengXing
 * datetime:2017/10/31 0031 17:52
 * 日志客户端配置类
 */
public class LogClientConfig {
    public static final String SERVICE_ID = "0001";//服务id
    public static final String IP = "127.0.0.1";//ip
    public static final Integer PORT = 8080;//端口
    public static final Integer TIMEOUT_SECONDS = 300;//空闲超时,自动切断时间
    public static final Integer RETRY_NUM = 5;//自动重连次数，超过后停止n秒再次重连
    public static final Integer STOP_TIME = 10000;//重连x次失败后，暂停重连秒数
}
