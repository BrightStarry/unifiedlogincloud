package com.zuma.serializer;

import com.google.gson.Gson;
import com.zuma.dto.LogMessage;
import com.zuma.factory.GsonFactory;
import com.zuma.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 10:41
 * 日志消息解码器   byte[] --> LogMessage
 */
public class LogDecoder extends MessageToMessageDecoder<ByteBuf> {

    private GsonFactory gsonFactory = GsonFactory.getInstance();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //字节数组
        byte[] buf = new byte[byteBuf.readableBytes()];
        //读取数据到字节数组
        byteBuf.readBytes(buf);
        list.add(KryoUtil.byte2Object(buf, LogMessage.class));

    }

    private void gson(ByteBuf byteBuf, List<Object> list) throws UnsupportedEncodingException {
        //字节数组
        byte[] buf = new byte[byteBuf.readableBytes()];
        //读取数据到字节数组
        byteBuf.readBytes(buf);
        //转为String
        String str = new String(buf, "UTF-8");

        Gson gson = gsonFactory.build();
        LogMessage logMessage = gson.fromJson(str, LogMessage.class);
        list.add(logMessage);
    }
}
