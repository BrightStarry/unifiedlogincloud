package com.zuma.handler;

import com.zuma.dto.LogMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 13:23
 * 服务端处理类
 */
@Slf4j
public class ServerHandler extends ChannelHandlerAdapter{
    /**
     * 读取到消息方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        log.info("server接收到消息:{}", (LogMessage)msg);

        //返回消息
        ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.copiedBuffer("success".getBytes()));

        //如果监听到关闭事件
        channelFuture.addListener(ChannelFutureListener.CLOSE);

    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("服务端异常.error={}", cause.getMessage());
        cause.printStackTrace();
    }
}
