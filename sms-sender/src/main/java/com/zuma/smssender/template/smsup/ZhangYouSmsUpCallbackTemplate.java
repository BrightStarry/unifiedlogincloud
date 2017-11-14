package com.zuma.smssender.template.smsup;

import com.zuma.smssender.dto.response.sendsms.async.ZhangYouAsyncResponse;
import com.zuma.smssender.entity.SmsUpRecord;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 13:14
 * 掌游短信上行处理
 */
@Slf4j
public class ZhangYouSmsUpCallbackTemplate extends SmsUpCallbackTemplate<ZhangYouAsyncResponse> {
    @Override
    SmsUpRecord responseToSmsUpRecord(ZhangYouAsyncResponse response) {
        //短信消息字段 需要解码
        String msgContent = response.getMsgContent();
        msgContent = CodeUtil.urlEncodeToString(msgContent);
        msgContent = CodeUtil.base64ToString(msgContent);

        return SmsUpRecord.builder()
                .channelId(ChannelEnum.ZHANG_YOU.getCode())
                .channelName(ChannelEnum.ZHANG_YOU.getMessage())
                .phone(response.getMobileSource())
                .content(msgContent)
                .requestBody(CodeUtil.objectToJsonString(response))
                .upTime(DateUtil.stringToDate(response.getTimestamp()))//时间可能为空,如果解析失败
                .build();
    }
}
