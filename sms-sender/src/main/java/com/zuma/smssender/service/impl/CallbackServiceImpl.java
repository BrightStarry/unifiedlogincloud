package com.zuma.smssender.service.impl;

import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.service.CallbackService;
import com.zuma.smssender.template.sendsms.callback.KuanXinSendSmsCallbackTemplate;
import com.zuma.smssender.template.sendsms.callback.QunZhengSendSmsCallbackTemplate;
import com.zuma.smssender.template.sendsms.callback.SendSmsCallbackTemplate;
import com.zuma.smssender.template.sendsms.callback.ZhangYouSendSmsCallbackTemplate;
import com.zuma.smssender.template.smsup.KuanXinSmsUpCallbackTemplate;
import com.zuma.smssender.template.smsup.SmsUpCallbackTemplate;
import com.zuma.smssender.template.smsup.ZhangYouSmsUpCallbackTemplate;
import org.springframework.stereotype.Service;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 09:46
 * 发送短信回调处理
 */
@Service
public class CallbackServiceImpl implements CallbackService {
    //发送短信回调处理模版类
    private SendSmsCallbackTemplate[] sendSmsCallbackTemplates = new SendSmsCallbackTemplate[]{
            new ZhangYouSendSmsCallbackTemplate(),
            new KuanXinSendSmsCallbackTemplate(),
            new QunZhengSendSmsCallbackTemplate()
    };

    //短信上行处理模版类
    private SmsUpCallbackTemplate[] smsUpCallbackTemplates = new SmsUpCallbackTemplate[]{
        new ZhangYouSmsUpCallbackTemplate(),
        new KuanXinSmsUpCallbackTemplate(),
    };

    @Override
    public <T> boolean sendSmsCallbackHandle(T response, ChannelEnum channelEnum) {
        sendSmsCallbackTemplates[channelEnum.getCode()].callbackHandle(response);
        return true;
    }


    @Override
    public <T> boolean smsUpCallbackHandle(T response, ChannelEnum channelEnum) {
        smsUpCallbackTemplates[channelEnum.getCode()].callbackHandle(response);
        return true;
    }

}
