package com.zuma.smssender.template.sendsms.callback;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.response.sendsms.async.KuanXinSendSmsAsyncResponse;
import com.zuma.smssender.enums.ResultDTOTypeEnum;
import com.zuma.smssender.enums.error.KuanXinErrorEnum;
import com.zuma.smssender.util.EnumUtil;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 11:03
 */
public class KuanXinSendSmsCallbackTemplate extends  SendSmsCallbackTemplate<KuanXinSendSmsAsyncResponse> {
    @Override
    String getKey(KuanXinSendSmsAsyncResponse response) {
        return Config.KUANXIN_PRE + response.getTaskId();
    }

    @Override
    ResultDTO<ErrorData> getResultDTO(CommonCacheDTO cacheDTO, KuanXinSendSmsAsyncResponse response) {
        //如果成功
        if(KuanXinErrorEnum.CALLBACK_SUCCESS.getCode().equals(response.getCode())){
            return ResultDTO.success(new ErrorData()).setType(ResultDTOTypeEnum.SEND_SMS_CALLBACK_ASYNC.getCode());
        }

        //失败--宽信接口似乎只会将成功返回，此处应该不会执行到
        //找到失败码对应枚举
        KuanXinErrorEnum errorEnum = EnumUtil.getByCode(response.getCode(), KuanXinErrorEnum.class);
        //返回失败信息
        return ResultDTO.error(errorEnum,new ErrorData(cacheDTO.getPhones(),cacheDTO.getSmsMessage()));
    }
}
