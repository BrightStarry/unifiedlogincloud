package com.zuma.smssender.template.smsup;

import com.zuma.smssender.dto.response.QunZhengSmsUpResponse;
import com.zuma.smssender.entity.SmsUpRecord;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.DateUtil;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 14:05
 */
public class QunZhengSmsUpCallbackTemplate extends  SmsUpCallbackTemplate<QunZhengSmsUpResponse> {
    @Override
    SmsUpRecord responseToSmsUpRecord(QunZhengSmsUpResponse response) {
        return SmsUpRecord.builder()
                .channelId(ChannelEnum.QUN_ZHENG.getCode())
                .channelName(ChannelEnum.QUN_ZHENG.getMessage())
                .phone(response.getSms().getPhone())
                .content(response.getSms().getContent())
                .requestBody(CodeUtil.objectToJsonString(response))
                .upTime(DateUtil.stringToDate(response.getSms().getRecvDate(),DateUtil.FORMAT_B))//时间可能为空,如果解析失败
                .build();
    }
}
