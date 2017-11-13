package com.zuma.smssender.template.sendsms;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.request.QunZhengSendSmsRequest;
import com.zuma.smssender.dto.response.sendsms.QunZhengSendSmsResponse;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.enums.error.QunZhengErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CacheUtil;
import com.zuma.smssender.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

@Slf4j
public class QunZhengSendSmsTemplate extends SendSmsTemplate<QunZhengSendSmsRequest, QunZhengSendSmsResponse> {
    @Override
    ResultDTO<ErrorData> getResponse(CommonSmsAccount account, String phones, String smsMessae, SendSmsForm sendSmsForm) {
        //转为 请求对象
        QunZhengSendSmsRequest request = toRequestObject(account, phones, smsMessae);
        QunZhengSendSmsResponse response = null;
        //发送请求，并返回ZhangYouResponse对象
        try {
            response = sendHttpRequest(request, Config.QUNZHENG_SEND_SMS_URL);
        } catch (SmsSenderException e) {
            //自定义异常捕获到,日志已经记录
            //返回异常返回对象
            return ResultDTO.error(e.getCode(), e.getMessage(), new ErrorData(phones, smsMessae));
        }
        //判断是否成功-根据api的response
        if (!QunZhengErrorEnum.SUCCESS.getCode().equals(response.getCode())) {
            //根据异常码获取异常枚举
            QunZhengErrorEnum errorEnum = EnumUtil.getByCode(response.getCode(), QunZhengErrorEnum.class);
            return ResultDTO.error(errorEnum, new ErrorData(phones, smsMessae));
        }

        //流水号处理
        //构建缓存对象
        CommonCacheDTO cacheDTO = CommonCacheDTO.builder()
                .id(response.getId())//流水号
                .platformId(sendSmsForm.getPlatformId())//平台id
                .timestamp(sendSmsForm.getTimestamp())//时间戳
                .phones(phones)//手机号
                .smsMessage(smsMessae)//短信消息
                .build();
        //存入缓存,key使用 掌游前缀 + 流水号
        CacheUtil.put(Config.QUNZHENG_PRE + cacheDTO.getId(), cacheDTO);

        //成功
        return ResultDTO.success(null);
    }

    @Override
    QunZhengSendSmsRequest toRequestObject(CommonSmsAccount account, String phones, String smsMessage) {
        try {
            //转为UTF-8
            smsMessage = new String(smsMessage.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("【群正sendSms】string字符转utf-8编码失败");
            throw new SmsSenderException(ErrorEnum.TRANSCODE_ERROR);
        }
        return QunZhengSendSmsRequest.builder()
                .flag("sendsms")
                .loginName(account.getAKey())
                .password(account.getBKey())
                .p(phones)
                .c(smsMessage)
                .build();
    }


    @Override
    QunZhengSendSmsResponse stringToResponseObject(String result) {
        try {
            //根据| 分割，获取[0]code 和[1]流水号
            String[] temp = StringUtils.split(result, ",");
            return QunZhengSendSmsResponse.builder()
                    .code(temp[0])
                    .id(temp[1])
                    .build();
        } catch (Exception e) {
            log.error("【群正sendSms】返回的string转为response对象失败.resultString={},error={}", result, e.getMessage(), e);
            throw new SmsSenderException(ErrorEnum.STRING_TO_RESPONSE_ERROR);
        }
    }
}
