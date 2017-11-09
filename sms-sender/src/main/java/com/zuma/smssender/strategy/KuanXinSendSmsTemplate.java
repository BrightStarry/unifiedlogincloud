package com.zuma.smssender.strategy;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.CommonResult;
import com.zuma.smssender.dto.request.KuanXinSendSmsRequest;
import com.zuma.smssender.dto.response.KuanXinSendSmsResponse;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.enums.error.KuanXinErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CacheUtil;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.EnumUtil;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * author:Administrator
 * datetime:2017/11/9 0009 17:13
 * 宽信发送短信策略接口
 */
public class KuanXinSendSmsTemplate extends SendSmsTemplate<KuanXinSendSmsRequest, KuanXinSendSmsResponse> {


    @Override
    ResultDTO<?> errorData(int apiRequestCount, List<ResultDTO<ErrorData>> resultDTOList) {
        //拼接返回对象<T>中的T
        CommonResult commonResult = new CommonResult(apiRequestCount, resultDTOList);
        //判断是否有异常返回
        if(CollectionUtils.isEmpty(resultDTOList))
            return ResultDTO.success(commonResult);
        //如果有异常
        return ResultDTO.error(ErrorEnum.SEND_SMS_ERROR, commonResult);
    }

    @Override
    ResultDTO<ErrorData> getResponse(CommonSmsAccount account, String phones, String smsMessae, SendSmsForm sendSmsForm) {
        //转为 请求对象
        KuanXinSendSmsRequest request = toRequestObject(account, phones, smsMessae);
        KuanXinSendSmsResponse response = null;
        //发送请求，并返回ZhangYouResponse对象
        try {
            response = sendHttpRequest(request);
        } catch (SmsSenderException e) {
            //自定义异常捕获到,日志已经记录
            //返回异常返回对象
            return ResultDTO.error(e.getCode(), e.getMessage(), new ErrorData(phones, smsMessae));
        }
        //判断是否成功-根据api的response
        if (!KuanXinErrorEnum.SUCCESS.getCode().equals(response.getCode())) {
            //根据掌游异常码获取异常枚举
            KuanXinErrorEnum errorEnum = EnumUtil.getByCode(response.getCode(), KuanXinErrorEnum.class);
            return ResultDTO.error(errorEnum, new ErrorData(phones, smsMessae));
        }

        //流水号处理
        //构建缓存对象
        CommonCacheDTO cacheDTO = CommonCacheDTO.builder()
                .id(response.getData().getTaskId())//流水号
                .platformId(sendSmsForm.getPlatformId())//平台id
                .timestamp(sendSmsForm.getTimestamp())//时间戳
                .phones(phones)//手机号
                .smsMessage(smsMessae)//短信消息
                .build();
        //存入缓存,key使用 掌游前缀 + 流水号
        CacheUtil.put(Config.KUANXIN_PRE + cacheDTO.getId(), cacheDTO);

        //成功
        return ResultDTO.success(null);
    }

    @Override
    KuanXinSendSmsRequest toRequestObject(CommonSmsAccount account, String phones, String smsMessage) {
        //时间戳
        long ts = System.currentTimeMillis();
        //签名
        String sign = CodeUtil.stringToMd5(account.getAKey() + ts + account.getBKey());
        return KuanXinSendSmsRequest.builder()
                .userId(account.getAKey())
                .mobile(phones)
                .msgcontent(CodeUtil.stringToUrlEncode(smsMessage))
                .sign(sign)
                .ts(ts)
                .build();
    }

    @Override
    KuanXinSendSmsResponse sendHttpRequest(KuanXinSendSmsRequest request) {
        //发送请求并获取返回值
        String result = HTTP_CLIENT_UTIL.doPostForString(Config.KUANXIN_SEND_SMS_URL, request);
        return stringToResponseObject(result);
    }

    @Override
    KuanXinSendSmsResponse stringToResponseObject(String result) {
        return CodeUtil.jsonStringToObject(result, KuanXinSendSmsResponse.class);
    }
}
