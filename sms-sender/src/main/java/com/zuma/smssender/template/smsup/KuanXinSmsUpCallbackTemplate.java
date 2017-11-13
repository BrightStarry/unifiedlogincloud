package com.zuma.smssender.template.smsup;

import com.zuma.smssender.dto.response.KuanXinSmsUpResponse;
import com.zuma.smssender.entity.SmsUpRecord;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.DateUtil;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:39
 */
public class KuanXinSmsUpCallbackTemplate extends SmsUpCallbackTemplate<KuanXinSmsUpResponse> {
    @Override
    SmsUpRecord responseToSmsUpRecord(KuanXinSmsUpResponse response) {
        return SmsUpRecord.builder()
                .channelId(ChannelEnum.KUAN_XIN.getCode())
                .channelName(ChannelEnum.KUAN_XIN.getMessage())
                .phone(response.getMobile())
                .content(response.getMsgContent())
                .requestBody(CodeUtil.objectToJsonString(response))
                .upTime(DateUtil.stringToDate(response.getTime()))//时间可能为空,如果解析失败
                .build();
    }
}
