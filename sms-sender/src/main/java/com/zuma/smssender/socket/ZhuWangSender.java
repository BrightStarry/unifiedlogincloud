package com.zuma.smssender.socket;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.config.SmsAccountCollection;
import com.zuma.smssender.dto.zhuwang.ZhuWangActiveTestAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangConnectAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangDeliverAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.ZhuWangCommandIdEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.enums.error.ZhuWangDeliverStatEnum;
import com.zuma.smssender.enums.error.ZhuWangSubmitErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.DateUtil;
import com.zuma.smssender.util.ZhuWangUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.UploadContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * author:ZhengXing
 * datetime:2017/11/23 0023 14:29
 * 筑望 消息发送器
 * ..在里面直接连接的方法,还写死了帐号.不太好.
 */
@Component
@Slf4j
@Setter
public class ZhuWangSender {
    //帐号
    private CommonSmsAccount account = SmsAccountCollection.getInstance().
        getAccounts()[ChannelEnum.ZHU_WANG.getCode()][PhoneOperatorEnum.YIDONG.getCode()];


    /**
     * 发送短信,逐条发送
     * 返回流水号sequenceId
     */
    public Integer sendSms(String msg,String phone) throws IOException {

        //流水号
        Integer sequenceId = ZhuWangUtil.getSequenceId();
        //信息内容来源(SP_Id)
        String msgSrc = account.getAKey();
        //手机号
        String destTerminalId = phone;
        //消息内容
        byte[] msgContent= msg.getBytes("gb2312");
        //信息长度
        byte msgLength = (byte)msgContent.length;

        //构建对象
        ZhuWangSubmitAPI.Request request = new ZhuWangSubmitAPI.Request()
                .setMsgSrc(msgSrc)
                .setDestTerminalId(destTerminalId)
                .setMsgContent(msgContent)
                .setMsgLength(msgLength);
        request.setSequenceId(sequenceId);

        log.info("发送发送短信请求:{}",request);
        send(request);
        return sequenceId;
    }


    /**
     * 发送连接请求
     */
    public void sendConnectRequest() throws IOException {

        String serviceId = account.getAKey();
        String pwd = account.getCKey();
        String timestamp = DateUtil.dateToString(new Date(), DateUtil.FORMAT_C);
        ZhuWangConnectAPI.Request request = ZhuWangConnectAPI.Request.builder()
                .sourceAddr(serviceId)
                .authenticatorSource(CodeUtil.byteToMd5((serviceId +
                        "\0\0\0\0\0\0\0\0\0" +
                        pwd +
                        timestamp).getBytes()))
                .version((byte)32)
                .timestamp(Integer.parseInt(timestamp))
                .build();
        request.setCommandId(ZhuWangCommandIdEnum.CMPP_CONNECT.getCode());
        request.setSequenceId(ZhuWangUtil.getSequenceId());

        log.info("发送连接请求:{}",request);

        send(request);
    }

    /**
     * 发送短信推送响应
     */
    public void sendDeliverResponse(ZhuWangDeliverAPI.Request request, ZhuWangSubmitErrorEnum errorEnum) throws IOException {
        ZhuWangDeliverAPI.Response response = ZhuWangDeliverAPI.Response.builder()
                .msgId(request.getMsgId())
                .result(errorEnum.getCode().byteValue())
                .build();
        response.setCommandId(ZhuWangCommandIdEnum.CMPP_DELIVER_RESP.getCode());
        response.setSequenceId(request.getSequenceId());

        log.info("短信推送响应:{}",response);
        send(response);

    }

    /**
     * 发送心跳检测(空) 或 发送心跳检测响应(非空)
     * 因为响应需要匹配其请求的sequenceId
     */
    public void sendActiveTest(Integer sequenceId) throws IOException {
        ZhuWangActiveTestAPI.Request request =
                new ZhuWangActiveTestAPI.Request(
                        sequenceId == null ?
                                ZhuWangCommandIdEnum.CMPP_ACTIVE_TEST :
                                ZhuWangCommandIdEnum.CMPP_ACTIVE_TEST_RESP,
                        sequenceId);
        log.info("发送心跳检测{}:{}",sequenceId == null ? "" : "响应",request);
        send(request);
    }


    /**
     * 发送数据,第一次
     */
    public void send(ToByteArray data) {
        send1(data,0);
    }

    /**
     * 发送数据
     */
    public void send1(ToByteArray data,Integer retryNum) {
        try {
            SocketPair socketPair = SocketStore.getBestSocketPair();
            //获取信号量
            socketPair.getSemaphore().acquire();
            Channel channel = socketPair.getChannel();

            channel.writeAndFlush(data.toByteArray());
        } catch (Exception e) {
            //重试
            log.error("【筑望】发送数据失败.重试次数={},error={}",retryNum,e.getMessage());
            //超过3次后，不再发送，抛出异常记录
            if(retryNum >= 3){
                throw new SmsSenderException(ErrorEnum.SOCKET_REQUEST_ERROR);
            }
            //未超过三次重试
            send1(data,++retryNum);
        }
    }


}
