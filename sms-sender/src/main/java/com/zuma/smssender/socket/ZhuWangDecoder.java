package com.zuma.smssender.socket;

import com.zuma.smssender.dto.zhuwang.ZhuWangActiveTestAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangConnectAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangDeliverAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.ZhuWangCommandIdEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.EnumUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * author:ZhengXing
 * datetime:2017/11/23 0023 14:19
 * 消息解码器
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ZhuWangDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        //字节数组
        byte[] buf = new byte[byteBuf.readableBytes()];
        //读取数据到字节数组
        byteBuf.readBytes(buf);


        //开始解析数据,先提取出长度字段标识长度的数据,也就是该条消息

        //4位 消息长度
        int totalLength = CodeUtil.bytesToInt(ArrayUtils.subarray(buf, 0, 4));
        //获取到该长度的字节数组
        byte[] bytes = ArrayUtils.subarray(buf, 0, totalLength);

        //获取到响应类型,也就是哪个接口的响应,4位
        int commandId = CodeUtil.bytesToInt(ArrayUtils.subarray(bytes, 4, 8));
        //获取到对应接口枚举
        ZhuWangCommandIdEnum commandIdEnum = EnumUtil.getByCode(commandId, ZhuWangCommandIdEnum.class);


        //如果响应类型为空,表示无此接口
        if (commandIdEnum == null) {
            log.info("[筑望]服务器响应,未知接口.commandId:{},bytes:{},",commandId,bytes);
            throw new SmsSenderException(ErrorEnum.ZHUWANG_RESPONSE_MESSAGE_ERROR);
        }

        //连接请求响应
        switch (commandIdEnum) {
            //如果是连接请求
            case CMPP_CONNECT_RESP:
                connectResponse(bytes,list);
                break;
            //如果是链路检测-他们发过来的链路检测
            case CMPP_ACTIVE_TEST:
                activeTestResponse(bytes,list);
                break;
            //如果是链路检测响应-我们发过去,他们发回来的信息
            case CMPP_ACTIVE_TEST_RESP:
                activeTestResponse(bytes,list);
                break;
            //如果是发送短信
            case CMPP_SUBMIT_RESP:
                submitResponse(bytes,list);
                break;
            //如果是短信推送
            case CMPP_DELIVER:
                deliverRequest(bytes,list);
                break;
            //其他
            default:
                log.info("[筑望]服务器响应,未知接口.");
                throw new SmsSenderException(ErrorEnum.ZHUWANG_RESPONSE_MESSAGE_ERROR);
        }


    }

    /**
     * 连接请求响应
     */
    private void connectResponse(byte[] bytes, List<Object> list) throws Exception {
        //byte[]转对象
        ZhuWangConnectAPI.Response response = new ZhuWangConnectAPI.Response(bytes);
        //解码成功,返回该对象
        result(list, response);
    }

    /**
     * 链路检测响应
     */
    private void activeTestResponse(byte[] bytes, List<Object> list) throws IOException {
        ZhuWangActiveTestAPI.Response response = new ZhuWangActiveTestAPI.Response(bytes);
        result(list,response);
    }

    /**
     * 发送短信响应
     */
    private void submitResponse(byte[] bytes,List<Object> list) throws IOException{
        ZhuWangSubmitAPI.Response response = new ZhuWangSubmitAPI.Response(bytes);
        result(list,response);
    }

    /**
     * 短信推送请求
     */
    private void deliverRequest(byte[] bytes,List<Object> list) throws IOException {
        ZhuWangDeliverAPI.Request request = new ZhuWangDeliverAPI.Request(bytes);
        result(list,request);
    }





    /**
     * 将对象加入list返回给handler方法
     */
    private <T> void result(List<Object> list, T obj) {
        log.info("接收到响应:{}", obj);
        list.add(obj);
    }


}
