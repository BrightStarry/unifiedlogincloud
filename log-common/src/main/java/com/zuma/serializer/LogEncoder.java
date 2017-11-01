package com.zuma.serializer;

import com.google.gson.Gson;
import com.zuma.dto.LogMessage;
import com.zuma.factory.GsonFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 11:21
 * 日志消息编码器
 */
public class LogEncoder extends MessageToByteEncoder<LogMessage>{

    private GsonFactory gsonFactory = GsonFactory.getInstance();
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LogMessage logMessage, ByteBuf byteBuf) throws Exception {
        Gson gson = gsonFactory.build();
        String json = gson.toJson(logMessage);
        byteBuf.writeBytes(json.getBytes());
    }
}
