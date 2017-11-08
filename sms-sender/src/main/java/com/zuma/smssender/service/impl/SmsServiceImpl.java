package com.zuma.smssender.service.impl;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.request.ZhangYouRequest;
import com.zuma.smssender.entity.Platform;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.SmssAndPhonesRelationEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.service.PlatformService;
import com.zuma.smssender.service.SmsService;
import com.zuma.smssender.strategy.SendSmsStrategy;
import com.zuma.smssender.strategy.ZhangYouSendSmsStrategy;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.EnumUtil;
import com.zuma.smssender.util.PhoneUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:31
 * 短信服务
 */
@Service
@Setter
@Slf4j
public class SmsServiceImpl implements SmsService{
    //发送短信接口策略
    private SendSmsStrategy<?> sendSmsStrategy;


    //发送短信接口参数策略实现类数组，根据channel code 取
    private SendSmsStrategy<?>[] sendSmsStrategies = new SendSmsStrategy<?>[]{
        new ZhangYouSendSmsStrategy()
    };

    //发送短信接口url，根据chanel code获取url
    private String[] urls = new String[]{
            Config.ZHANGYOU_SEND_SMS_URL,
            Config.KUANXIN_SEND_SMS_URL,
            Config.QUNZHENG_SEND_SMS_URL};



    @Autowired
    private PlatformService platformService;

    @Override
    public ResultDTO<?> sendSms(SendSmsForm sendSmsForm) {
        /**
         * 参数校验
         */
        //确认平台存在
        Platform platform = platformService.findOne(Long.valueOf(sendSmsForm.getPlatformId()));

        //确认签名
        String realSign = CodeUtil.StringToMd5(platform.getToken() + sendSmsForm.getPhones() + sendSmsForm.getMillisecond());
        if(!sendSmsForm.getSign().equals(realSign)){
            log.error("【API发送短信接口】签名不匹配.currentSign={}",sendSmsForm.getSign());
        }


        //获取通道枚举
        ChannelEnum channelEnum = EnumUtil.getByCode(Integer.valueOf(sendSmsForm.getChannel()), ChannelEnum.class);
        //确认指定的通道是否存在，此时还有一种情况就是通道未指定
        if(!StringUtils.isEmpty(sendSmsForm.getChannel()) &&
                channelEnum == null){
              log.error("【API发送短信接口】通道不存在.channel={}",sendSmsForm.getChannel());
            ;throw new SmsSenderException(ErrorEnum.CHANNEL_EMPTY);
        }

        //确认手机号码
        String[] phones = StringUtils.split(sendSmsForm.getPhones());
        //如果手机号数超限
        if (phones.length > Config.MAX_PHONE_NUM) {
            log.error("【API发送短信接口】手机号码数目超过最大值:{},当前数目:{}",Config.MAX_PHONE_NUM,phones.length);
            throw new SmsSenderException(ErrorEnum.PHONE_NUMBER_OVER);
        }

        //确认短信消息
        String[] smsMessages = StringUtils.split(sendSmsForm.getSmsMessage(), Config.SMS_MESSAGE_SEPARATOR);
        //判断 短信消息-手机号  一对一 或 一对多 或多对多
        SmssAndPhonesRelationEnum smssAndPhonesRelationEnum =
                smsMessages.length == 1 ?
                        //当短信数为1的情况下， 手机号数也为1，则为one-one；  否则就是 one-multi
                (phones.length == 1 ? SmssAndPhonesRelationEnum.ONE_ONE : SmssAndPhonesRelationEnum.ONE_MULTI) :
                        //当短信数为多的情况下， 手机号数和其相等，则Multi-multi, 否则就是 other
                (phones.length == smsMessages.length ? SmssAndPhonesRelationEnum.MULTI_MULTI : SmssAndPhonesRelationEnum.OTHER);
        //如果不符合规范
        if (smssAndPhonesRelationEnum.equals(SmssAndPhonesRelationEnum.OTHER)) {
            log.error("【API发送短信接口】手机号和短信消息无法对应.短信消息数：{},手机号数:{}", smsMessages.length, phones.length);
            throw new SmsSenderException(ErrorEnum.SMSS_LEN_AND_PHONES_LEN_MISMATCH);
        }
        //--------------------参数校验END-------------------------------

        //如果指定了通道
        if(!StringUtils.isEmpty(sendSmsForm.getChannel())){
            //确认channel和手机运营商是否吻合
            //获取每个手机号的运营商枚举
            PhoneOperatorEnum[] phoneOperators = PhoneUtil.getPhoneOperator(phones);
            //获取通道支持的运营商数组
            PhoneOperatorEnum[] phoneOperatorSupports = channelEnum.getPhoneOperatorSupport();
            //用来统计当前号码数组包含的不同运营商
            PhoneOperatorEnum[] containOperators = new PhoneOperatorEnum[3];
            //遍历每个手机号的运营商枚举数组
            for (PhoneOperatorEnum temp : phoneOperators) {
                //如果通道不包含某运营商，则失败
                if(!ArrayUtils.contains(phoneOperatorSupports, temp)){
                    log.error("【API发送短信接口】该通道:{}不支持运营商:{}的手机号码",channelEnum.getMessage(),temp.getMessage());
                    throw new SmsSenderException(ErrorEnum.UNSUPPORTED_OPERATOR);
                }
                //如果统计数组不存在该运营商，就加入
                if(containOperators.length != 3 && !ArrayUtils.contains(containOperators,temp)){
                    ArrayUtils.add(containOperators, temp);
                }
            }


        }


        return null;
    }


}
