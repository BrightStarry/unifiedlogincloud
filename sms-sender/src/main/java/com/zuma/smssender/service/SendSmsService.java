package com.zuma.smssender.service;

import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.form.SendSmsForm;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 15:56
 * 短信服务 抽象类 模版方法
 */
public interface SendSmsService {
    //发送短信
    ResultDTO sendSms(SendSmsForm sendSmsForm);


}
