package com.zuma.smssender.strategy;

import com.zuma.smssender.config.SmsAccountCollection;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.form.SendSmsForm;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:25
 * 发送短信参数策略模式 接口
 */
public interface SendSmsStrategy<T> {
    //帐号s
    SmsAccountCollection accounts = new SmsAccountCollection();

    //发送短信，根据实体和　手机号数组包含的所有不同运营商数组
    T sendSms(SendSmsForm sendSmsForm, PhoneOperatorEnum[] containOperators);
}
