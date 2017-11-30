package com.zuma.smssender.socket;

import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/11/23 0023 14:19
 * 可转为字节数组对象 的 消息编码器
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ZhuWangEncoder  extends MessageToByteEncoder<ToByteArray> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ToByteArray data,ByteBuf byteBuf) throws Exception {
        //使用toByteArray方法将其转为字节数组,并加入输出集合中
        try {
            byteBuf.writeBytes(data.toByteArray());
        } catch (Exception e) {
            log.error("[消息编码器]消息编码异常.data:{}",data);
            throw new SmsSenderException(ErrorEnum.ENCODE_ERROR);
        }
    }
}
