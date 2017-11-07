package com.zuma.handler;

import com.zuma.sender.LogSender;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * author:ZhengXing
 * datetime:2017/10/31 0031 13:39
 * 客户端处理类
 */
@Slf4j
public class ClientHandler extends ChannelHandlerAdapter {

    /**
     * 通道激活方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        log.info("客户端通道激活");
        LogSender.setChannelHandlerContext(ctx.channel());
    }

    /**
     * 读取事件
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("client接收到消息:{}", (String) msg);
    }
}
