package com.zuma.smssender.strategy;

import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.SmssAndPhonesRelationEnum;
import com.zuma.smssender.form.SendSmsForm;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 17:13
 * 宽信发送短信策略接口
 */
public class KuanXinSendSmsStrategy implements SendSmsStrategy {

    /**
     * 发送短信，根据实体和　手机号数组包含的所有不同运营商数组 和  短信消息-手机号对应关系
     *
     * @param sendSmsForm               api收到的消息
     * @param containOperators          手机号数组包含的所有不同的运营商
     * @param smssAndPhonesRelationEnum 短信消息-手机号 关系，例如1-1；1-*；*-*
     * @return 返回对象
     */
    @Override
    public ResultDTO<?> sendSms(SendSmsForm sendSmsForm, PhoneOperatorEnum[] containOperators, SmssAndPhonesRelationEnum smssAndPhonesRelationEnum) {
        return null;
    }
}
