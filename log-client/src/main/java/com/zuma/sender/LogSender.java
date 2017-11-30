package com.zuma.sender;

import com.zuma.dto.LogMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * author:ZhengXing
 * datetime:2017/10/31 0031 16:55
 * 日志发送器
 */
public class LogSender {
    private static Channel ctx;

    /**
     * 发送日志
     * @param logMessage
     */
    public static void send(LogMessage logMessage) {
        ctx.writeAndFlush(logMessage);
    }

    /**
     * 设置通道处理器上下文
     * @param ctx
     */
    public static void setChannelHandlerContext(Channel ctx) {
        LogSender.ctx = ctx;
    }

    /**
     * 连接是否可用
     */
    public static boolean isAvailable(){
        return ctx != null && ctx.isWritable();
    }
}
