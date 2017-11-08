package com.zuma.smssender.strategy;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.SmsAccountCollection;
import com.zuma.smssender.dto.request.ZhangYouRequest;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CodeUtil;
import org.apache.commons.lang3.ArrayUtils;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 15:46
 * 掌游，策略实现类
 */
public class ZhangYouSendSmsStrategy implements SendSmsStrategy<ZhangYouRequest> {

    @Override
    public ZhangYouRequest sendSms(SendSmsForm sendSmsForm,PhoneOperatorEnum[] containOperators) {
        //帐号集合
        CommonSmsAccount[][] allAccounts = SendSmsStrategy.accounts.getAccounts();
        //掌游帐号集合
        CommonSmsAccount[] accounts = new CommonSmsAccount[3];
        //遍历包含的运营商集合，每有一个运营商就需要发送一次请求,并从中获取掌游通道对应的帐号数组
        for (int i = 0; i < containOperators.length; i++) {
            //获取当前通道、当前运营商帐号
            CommonSmsAccount account = allAccounts[ChannelEnum.ZHANG_YOU.getCode()][containOperators[i].getCode()];
            //签名
            String sign = CodeUtil.StringToMd5(account.getAKey() + account.getCKey());
            


        }


        return null;
    }
}
