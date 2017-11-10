package com.zuma.smssender.template.sendsms.callback;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.response.sendsms.QunZhengSendSmsAsyncResponse;
import com.zuma.smssender.enums.ResultDTOTypeEnum;
import com.zuma.smssender.enums.error.QunZhengErrorEnum;
import com.zuma.smssender.util.EnumUtil;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 11:32
 */
public class QunZhengSendSmsCallbackTemplate extends SendSmsCallbackTemplate<QunZhengSendSmsAsyncResponse> {
    @Override
    String getKey(QunZhengSendSmsAsyncResponse response) {
        return Config.QUNZHENG_PRE + response.getSms().getPno();
    }

    @Override
    ResultDTO<ErrorData> getResultDTO(CommonCacheDTO cacheDTO, QunZhengSendSmsAsyncResponse response) {
        //如果成功
        if(QunZhengErrorEnum.CALLBACK_SUCCESS.getCode().equals(response.getSms().getState())){
            return ResultDTO.success(ErrorData.class).setType(ResultDTOTypeEnum.SEND_SMS_CALLBACK.getCode());
        }
        //失败
        //找到失败码对应枚举
        QunZhengErrorEnum errorEnum = EnumUtil.getByCode(response.getSms().getState(), QunZhengErrorEnum.class);
        //返回失败信息
        return ResultDTO.error(errorEnum,new ErrorData(cacheDTO.getPhones(),cacheDTO.getSmsMessage()));
    }
}
