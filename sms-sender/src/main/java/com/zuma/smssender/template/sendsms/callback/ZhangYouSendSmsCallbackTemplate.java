package com.zuma.smssender.template.sendsms.callback;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.response.sendsms.async.ZhangYouAsyncResponse;
import com.zuma.smssender.enums.ResultDTOTypeEnum;
import com.zuma.smssender.enums.error.ZhangYouErrorEnum;
import com.zuma.smssender.util.EnumUtil;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 10:36
 */
public class ZhangYouSendSmsCallbackTemplate extends SendSmsCallbackTemplate<ZhangYouAsyncResponse> {

    @Override
    String getKey(ZhangYouAsyncResponse response) {
        return Config.ZHANGYOU_PRE + response.getTaskId();
    }

    @Override
    ResultDTO<ErrorData> getResultDTO(CommonCacheDTO cacheDTO, ZhangYouAsyncResponse response) {
        //如果成功
        if(ZhangYouErrorEnum.SUCCESS.getCode().equals(response.getMsgCode())){
            return ResultDTO.success(new ErrorData()).setType(ResultDTOTypeEnum.SEND_SMS_CALLBACK_ASYNC.getCode());
        }
        //失败
        //找到失败码对应枚举
        ZhangYouErrorEnum errorEnum = EnumUtil.getByCode(response.getMsgCode(), ZhangYouErrorEnum.class);
        //返回失败信息
        return ResultDTO.error(errorEnum,new ErrorData(cacheDTO.getPhones(),cacheDTO.getSmsMessage()));
    }
}
