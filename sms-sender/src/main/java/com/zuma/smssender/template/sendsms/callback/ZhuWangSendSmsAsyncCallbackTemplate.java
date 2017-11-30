package com.zuma.smssender.template.sendsms.callback;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.zhuwang.ZhuWangDeliverAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.ResultDTOTypeEnum;
import com.zuma.smssender.enums.error.ZhuWangDeliverStatEnum;
import com.zuma.smssender.enums.error.ZhuWangSubmitErrorEnum;
import com.zuma.smssender.util.EnumUtil;
import org.springframework.stereotype.Component;

/**
 * author:ZhengXing
 * datetime:2017/11/27 0027 13:56
 */
@Component
public class ZhuWangSendSmsAsyncCallbackTemplate extends SendSmsCallbackTemplate<ZhuWangDeliverAPI.Request>  {
    @Override
    String getKey(ZhuWangDeliverAPI.Request response) {
        return Config.ZHUWANG_PRE + String.valueOf(response.getMsgContent().getMsgId());
    }

    @Override
    ResultDTO<ErrorData> getResultDTO(CommonCacheDTO cacheDTO, ZhuWangDeliverAPI.Request response) {
        //如果成功
        if(ZhuWangDeliverStatEnum.DELIVERED.getCode().equals(response.getMsgContent().getStat())){
            return ResultDTO.success(new ErrorData()).setType(ResultDTOTypeEnum.SEND_SMS_CALLBACK_ASYNC.getCode());
        }
        //失败
        //找到失败码对应枚举
        ZhuWangDeliverStatEnum errorEnum = EnumUtil.getByCode(response.getMsgContent().getStat(), ZhuWangDeliverStatEnum.class);
        //返回失败信息
        return ResultDTO.error(errorEnum,new ErrorData(cacheDTO.getPhones(),cacheDTO.getSmsMessage()));
    }
}
