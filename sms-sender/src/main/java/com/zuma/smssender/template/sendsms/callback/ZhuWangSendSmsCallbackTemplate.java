package com.zuma.smssender.template.sendsms.callback;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.ResultDTOTypeEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.enums.error.QunZhengErrorEnum;
import com.zuma.smssender.enums.error.ZhuWangSubmitErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.util.CacheUtil;
import com.zuma.smssender.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * author:ZhengXing
 * datetime:2017/11/27 0027 13:39
 */
@Slf4j
@Component
public class ZhuWangSendSmsCallbackTemplate extends SendSmsCallbackTemplate<ZhuWangSubmitAPI.Response> {
    //获取流水号
    @Override
    String getKey(ZhuWangSubmitAPI.Response response) {
        return Config.ZHUWANG_PRE + String.valueOf(response.getSequenceId());
    }

    /**
     * 该平台需要重写返回处理方法,成功后,只修改缓存中的值,失败才通知调用者
     */
    @Override
    public boolean callbackHandle(ZhuWangSubmitAPI.Response response) {
        String key = getKey(response);
        log.info("[筑望][短信下发同步回调]从缓存中取出数据,key:{}",key);
        CommonCacheDTO cacheDTO = getCache(key);
        ResultDTO<ErrorData> resultDTO = getResultDTO(cacheDTO, response);
        //如果成功
        if(ResultDTO.isSuccess(resultDTO)){
            CommonCacheDTO newCacheDTO = CommonCacheDTO.builder()
                    .id(String.valueOf(response.getMsgId()))//流水号
                    .platformId(cacheDTO.getPlatformId())//平台id
                    .timestamp(cacheDTO.getTimestamp())//时间戳
                    .phones(cacheDTO.getPhones())//手机号
                    .smsMessage(cacheDTO.getSmsMessage())//短信消息
                    .recordId(cacheDTO.getRecordId())//该次发送记录数据库id
                    .build();
            //存入缓存,key使用 掌游前缀 + 流水号
            CacheUtil.put(Config.ZHUWANG_PRE + newCacheDTO.getId(),newCacheDTO);
            log.info("[筑望][发送短信]同步回调信息存入缓存.key:{}",Config.ZHUWANG_PRE + newCacheDTO.getId());
        }else{
            //失败
            sendCallback(resultDTO,cacheDTO.getPlatformId(),0);
        }
        return true;
    }

    /**
     * 该平台需要重写该方法 ,防止回调太快,cache还未存储,导致缓存为空
     */
    @Override
    CommonCacheDTO getCache(String key) {
        CommonCacheDTO result = CacheUtil.getAndDelete(key, CommonCacheDTO.class);
        if (result == null) {
            //等待500ms
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                //...不作处理
            }
            //再次尝试获取
            result = CacheUtil.getAndDelete(key, CommonCacheDTO.class);
            //再次判断
            if(result == null){
                log.error("【发送短信异步回调】缓存过期，无法获取对应信息.");
                throw new SmsSenderException(ErrorEnum.CACHE_EXPIRE);
            }
        }
        return result;
    }

    @Override
    ResultDTO<ErrorData> getResultDTO(CommonCacheDTO cacheDTO, ZhuWangSubmitAPI.Response response) {
        //如果成功
        if(ZhuWangSubmitErrorEnum.SUCCESS.getCode().equals((int)response.getResult())){
            return ResultDTO.success(new ErrorData()).setType(ResultDTOTypeEnum.SEND_SMS_CALLBACK_SYNC.getCode());
        }
        //失败
        //找到失败码对应枚举
        ZhuWangSubmitErrorEnum errorEnum = EnumUtil.getByCode((int) response.getResult(), ZhuWangSubmitErrorEnum.class);
        //返回失败信息
        return ResultDTO.errorOfInteger(errorEnum,new ErrorData(cacheDTO.getPhones(),cacheDTO.getSmsMessage()));
    }


}
