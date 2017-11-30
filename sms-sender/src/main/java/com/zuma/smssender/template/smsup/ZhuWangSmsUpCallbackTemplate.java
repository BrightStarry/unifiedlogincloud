package com.zuma.smssender.template.smsup;

import com.zuma.smssender.dto.zhuwang.ZhuWangDeliverAPI;
import com.zuma.smssender.entity.SmsUpRecord;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.util.CodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * author:ZhengXing
 * datetime:2017/11/27 0027 14:17
 * 筑望 短信上行(Deliver接口)处理
 */
@Slf4j
@Component
public class ZhuWangSmsUpCallbackTemplate extends SmsUpCallbackTemplate<ZhuWangDeliverAPI.Request>{
    @Override
    SmsUpRecord responseToSmsUpRecord(ZhuWangDeliverAPI.Request response) {
        return SmsUpRecord.builder()
                .channelId(ChannelEnum.ZHU_WANG.getCode())
                .channelName(ChannelEnum.ZHU_WANG.getMessage())
                .phone(response.getSrcTerminalId())
                .content(response.getOtherMsgContent())
                .requestBody(CodeUtil.objectToJsonString(response))
                .upTime(new Date())//时间可能为空,如果解析失败
                .build();
    }
}
