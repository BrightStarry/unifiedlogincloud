package com.zuma.smssender.service.impl;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonResult;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.entity.Platform;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.SmsAndPhoneRelationEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.service.PlatformService;
import com.zuma.smssender.service.SmsService;
import com.zuma.smssender.template.KuanXinSendSmsTemplate;
import com.zuma.smssender.template.QunZhengSendSmsTemplate;
import com.zuma.smssender.template.SendSmsTemplate;
import com.zuma.smssender.template.ZhangYouSendSmsTemplate;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.EnumUtil;
import com.zuma.smssender.util.PhoneUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:31
 * 短信服务
 */
@Service
@Setter
@Slf4j
public class SmsServiceImpl implements SmsService {
    //发送短信接口参数策略实现类数组，根据channel code 取
    private SendSmsTemplate[] sendSmsStrategies = new SendSmsTemplate[]{
            new ZhangYouSendSmsTemplate(),
            new KuanXinSendSmsTemplate(),
            new QunZhengSendSmsTemplate()
    };


    @Autowired
    private PlatformService platformService;

    @Override
    public ResultDTO sendSms(SendSmsForm sendSmsForm) {
        /**
         * 参数校验
         */
        //确认平台存在
        Platform platform = platformService.findOne(sendSmsForm.getPlatformId());

        //确认签名
        String realSign = CodeUtil.stringToMd5(platform.getToken() + sendSmsForm.getPhones() + sendSmsForm.getTimestamp());
        if (!sendSmsForm.getSign().equals(realSign)) {
            log.error("【API发送短信接口】签名不匹配.currentSign={}", sendSmsForm.getSign());
        }


        //获取通道枚举
        ChannelEnum channelEnum = EnumUtil.getByCode(Integer.valueOf(sendSmsForm.getChannel()), ChannelEnum.class);
        //确认指定的通道是否存在，此时还有一种情况就是通道未指定
        if (!StringUtils.isEmpty(sendSmsForm.getChannel()) &&
                channelEnum == null) {
            log.error("【API发送短信接口】通道不存在.channel={}", sendSmsForm.getChannel());
            ;
            throw new SmsSenderException(ErrorEnum.CHANNEL_EMPTY);
        }

        //确认手机号码
        String[] phones = StringUtils.split(sendSmsForm.getPhones(), Config.PHONES_SEPARATOR);
        //如果手机号数超限
        if (phones.length > Config.MAX_PHONE_NUM) {
            log.error("【API发送短信接口】手机号码数目超过最大值:{},当前数目:{}", Config.MAX_PHONE_NUM, phones.length);
            throw new SmsSenderException(ErrorEnum.PHONE_NUMBER_OVER);
        }

        //确认短信消息
        String[] smsMessages = StringUtils.split(sendSmsForm.getSmsMessage(), Config.SMS_MESSAGE_SEPARATOR);
        //判断 短信消息-手机号  一对一 或 一对多 或多对多
        SmsAndPhoneRelationEnum smsAndPhoneRelationEnum =
                smsMessages.length == 1 ?
                        //当短信数为1的情况下， 手机号数也为1，则为one-one；  否则就是 one-multi
                        (phones.length == 1 ? SmsAndPhoneRelationEnum.ONE_ONE : SmsAndPhoneRelationEnum.ONE_MULTI) :
                        //当短信数为多的情况下， 手机号数和其相等，则Multi-multi, 否则就是 other
                        (phones.length == smsMessages.length ? SmsAndPhoneRelationEnum.MULTI_MULTI : SmsAndPhoneRelationEnum.OTHER);
        //如果不符合规范
        if (smsAndPhoneRelationEnum.equals(SmsAndPhoneRelationEnum.OTHER)) {
            log.error("【API发送短信接口】手机号和短信消息无法对应.短信消息数：{},手机号数:{}", smsMessages.length, phones.length);
            throw new SmsSenderException(ErrorEnum.SMS_LEN_AND_PHONE_LEN_MISMATCH);
        }
        //--------------------参数校验END-------------------------------

        //用来统计当前号码数组包含的不同运营商
        PhoneOperatorEnum[] containOperators = new PhoneOperatorEnum[3];

        //如果指定了通道
        if (!StringUtils.isEmpty(sendSmsForm.getChannel())) {
            //获取每个手机号的运营商枚举
            PhoneOperatorEnum[] phoneOperators = PhoneUtil.getPhoneOperator(phones);
            //获取通道支持的运营商数组
            PhoneOperatorEnum[] phoneOperatorSupports = channelEnum.getPhoneOperatorSupport();
            //确认channel和手机运营商是否吻合,并统计包含的所有不同运营商，遍历每个手机号的运营商枚举数组
            for (PhoneOperatorEnum temp : phoneOperators) {
                //如果通道不包含某运营商，则失败
                if (!ArrayUtils.contains(phoneOperatorSupports, temp)) {
                    log.error("【API发送短信接口】该通道:{}不支持运营商:{}的手机号码", channelEnum.getMessage(), temp.getMessage());
                    throw new SmsSenderException(ErrorEnum.UNSUPPORTED_OPERATOR);
                }
                //如果统计数组不存在该运营商，就加入,以此统计出所有不同运营商
                if (containOperators.length != 3 && !ArrayUtils.contains(containOperators, temp)) {
                    ArrayUtils.add(containOperators, temp);
                }
            }

            //调用指定通道对应的发送短信策略
            return sendSmsStrategies[channelEnum.getCode()].sendSms(
                    sendSmsForm.getPhones(),
                    sendSmsForm.getSmsMessage(),
                    sendSmsForm,
                    containOperators,
                    smsAndPhoneRelationEnum);
        }


        //如果未指定通道
        //该手机号数组可用通道
        List<ChannelEnum> availableChannel = new ArrayList<>();
        //遍历所有通道,提取可用通道集合
        for (ChannelEnum channelEach : ChannelEnum.values()) {
            //标识，该通道是否支持当前手机号数组
            boolean flag = true;
            //遍历该手机号数组包含的所有运营商
            for (PhoneOperatorEnum operatorEech : containOperators) {
                //如果不包含该运营商，则表示该通道不支持该手机号数组
                if (!ArrayUtils.contains(channelEach.getPhoneOperatorSupport(), operatorEech))
                    flag = false;
            }
            //将校验通过的channel加入可用通道数组
            if (flag)
                availableChannel.add(channelEach);
        }


        //失败，更换通道重新发送机制
        //返回对象集合,可能有多个
        List<ResultDTO> resultDTOList = new ArrayList<>();
        //循环所有可用通道
        for (ChannelEnum channelEach : availableChannel) {
            //发送短信
            ResultDTO resultDTO = sendSmsStrategies[channelEnum.getCode()]
                    .sendSms(
                            sendSmsForm.getPhones(),
                            sendSmsForm.getSmsMessage(),
                            sendSmsForm,
                            containOperators,
                            smsAndPhoneRelationEnum);
            //提取失败的数据
            //如果data为空说明全部成功
            if (resultDTO.getData() == null)
                return resultDTO;
            //如果有失败的数据


        }


        return null;
    }




}
