package com.zuma.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zuma.dto.LogMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.springframework.boot.json.GsonJsonParser;

import java.nio.ByteOrder;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 10:41
 * 日志消息解码器
 */
public class LogDecoder extends MessageToMessageDecoder<ByteBuf>{


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //字节数组
        byte[] buf = new byte[byteBuf.readableBytes()];
        //读取数据到字节数组
        byteBuf.readBytes(buf);
        //转为String
        String str = new String(buf,"UTF-8");

        Gson gson = new Gson();
        LogMessage logMessage = gson.fromJson(str, LogMessage.class);
        list.add(logMessage);
    }
}
