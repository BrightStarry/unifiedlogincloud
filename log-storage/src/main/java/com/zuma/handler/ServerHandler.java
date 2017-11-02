package com.zuma.handler;

import com.zuma.dto.LogMessage;
import com.zuma.executor.LogWriteExecutor;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 13:23
 * 服务端处理类
 */
@Slf4j
public class ServerHandler extends ChannelHandlerAdapter{
    private LogWriteExecutor logWriteExecutor = LogWriteExecutor.getInstance();



    /**
     * 读取到消息方法
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        log.info("server接收到{}的消息:{}", ctx.channel().id() ,(LogMessage)msg);//TODO
        logWriteExecutor.addLogToQueue((LogMessage)msg);
        //返回消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("success".getBytes()));
    }

    /**
     * 用户自定义事件触发
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果不是 空闲状态事件
        if (!(evt instanceof IdleStateEvent))
            super.userEventTriggered(ctx,evt);


        IdleStateEvent event = (IdleStateEvent) evt;
        //如果不是读取超时事件
        if(event.state() != IdleState.READER_IDLE)
            return;

        log.info("客户端空闲超时，关闭。channel={}",ctx.channel().id());
        //读取超时,关闭该通道
        ctx.channel().close();
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
