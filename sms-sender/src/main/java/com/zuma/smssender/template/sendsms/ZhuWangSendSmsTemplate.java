package com.zuma.smssender.template.sendsms;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.socket.ZhuWangSender;
import com.zuma.smssender.util.CacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * author:ZhengXing
 * datetime:2017/11/27 0027 13:05
 */
@Component
@Slf4j
public class ZhuWangSendSmsTemplate extends SendSmsTemplate<ZhuWangSubmitAPI.Request,ZhuWangSubmitAPI.Response> {

    private static ZhuWangSender zhuWangSender;

    //暂时未将该template加入spring,采用该方法注入
    @Autowired
    public void setZhuWangSender(ZhuWangSender zhuWangSender) {
        ZhuWangSendSmsTemplate.zhuWangSender = zhuWangSender;
    }


    /**
     * 筑望平台获取响应方法,因为没有同步回调,所以,全部显示为成功
     */
    @Override
    ResultDTO<ErrorData> getResponse(CommonSmsAccount account, String phones, String smsMessage, SendSmsForm sendSmsForm) {
        Integer sequenceId;
        try {
            sequenceId = zhuWangSender.sendSms(smsMessage,phones);
        } catch (IOException e) {
            log.error("[筑望]发送短信异常.e:{}",e.getMessage(),e);
            throw new SmsSenderException(ErrorEnum.SOCKET_REQUEST_ERROR);
        }

        CommonCacheDTO cacheDTO = CommonCacheDTO.builder()
                .id(String.valueOf(sequenceId))//流水号
                .platformId(sendSmsForm.getPlatformId())//平台id
                .timestamp(sendSmsForm.getTimestamp())//时间戳
                .phones(phones)//手机号
                .smsMessage(smsMessage)//短信消息
                .build();
        //存入缓存,key使用 筑望前缀 + 流水号
        CacheUtil.put(Config.ZHUWANG_PRE + cacheDTO.getId(),cacheDTO);

        //筑望所有同步回调,都为成功
        return ResultDTO.success();
    }



    //该平台无需下面方法

    @Override
    ZhuWangSubmitAPI.Request toRequestObject(CommonSmsAccount account, String phones, String smsMessage) {
        return null;
    }

    @Override
    ZhuWangSubmitAPI.Response stringToResponseObject(String result) {
        return null;
    }
}
