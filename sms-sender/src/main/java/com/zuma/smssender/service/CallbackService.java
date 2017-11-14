package com.zuma.smssender.service;

import com.zuma.smssender.enums.ChannelEnum;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 09:43
 * 接口，回调处理
 */
public interface CallbackService {
    //发送短信接口回调处理
    <T> boolean sendSmsCallbackHandle(T response, ChannelEnum channelEnum);

    //短信上行回调处理
    <T> boolean smsUpCallbackHandle(T response, ChannelEnum channelEnum);
}
