package com.zuma.smssender.strategy;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.CommonCacheDTO;
import com.zuma.smssender.dto.request.ZhangYouSendSmsRequest;
import com.zuma.smssender.dto.response.ZhangYouSendSmsSyncResponse;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.enums.error.ZhangYouErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CacheUtil;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 15:46
 * 掌游，策略实现类
 */
@Slf4j
public class ZhangYouSendSmsTemplate extends SendSmsTemplate<ZhangYouSendSmsRequest,ZhangYouSendSmsSyncResponse> {


    /**
     * 直接获取response，组合下面两个方法。
     * 并解析response对象，组装为ResultDTO<ErrorData>
     * @param account
     * @param phones
     * @param smsMessae
     * @return
     */
    public ResultDTO<ErrorData> getResponse(CommonSmsAccount account,
                                             String phones, String smsMessae,
                                             SendSmsForm sendSmsForm) {
        //转为 请求对象
        ZhangYouSendSmsRequest request = toRequestObject(account, phones, smsMessae);
        ZhangYouSendSmsSyncResponse response = null;
        //发送请求，并返回ZhangYouResponse对象
        try {
            response = sendHttpRequest(request);
        } catch (SmsSenderException e) {
            //自定义异常捕获到,日志已经记录
            //返回异常返回对象
            return ResultDTO.error(e.getCode(), e.getMessage(),new ErrorData(phones,smsMessae));
        }
        //判断是否成功-根据api的response
        if(!ZhangYouErrorEnum.SUCCESS.getCode().equals(response.getCode())){
            //根据掌游异常码获取异常枚举
            ZhangYouErrorEnum zhangyouErrorEnum = EnumUtil.getByCode(response.getCode(), ZhangYouErrorEnum.class);
            return ResultDTO.error(zhangyouErrorEnum,new ErrorData(phones,smsMessae));
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
        CacheUtil.put(Config.ZHANGYOU_PRE + cacheDTO.getId(),cacheDTO);

        //成功
        return ResultDTO.success(null);
    }

    /**
     * 根据帐号、手机号s，短信消息 转 请求对象
     *
     * @param account
     * @param phones
     * @param msg
     * @return
     */
    public ZhangYouSendSmsRequest toRequestObject(CommonSmsAccount account, String phones, String msg) {
        //签名
        String sign = CodeUtil.stringToMd5(account.getAKey() + account.getCKey());
        //创建请求对象
        return ZhangYouSendSmsRequest.builder()
                .sid(account.getAKey())
                .cpid(account.getBKey())
                .sign(sign)
                .mobi(phones)
                .msg(CodeUtil.stringToUrlEncode(CodeUtil.stringToBase64(msg)))
                .build();
    }

    /**
     * 发送http请求，并将返回数据组装为对应的response对象
     *
     * @param request
     * @return
     */
    public ZhangYouSendSmsSyncResponse sendHttpRequest(ZhangYouSendSmsRequest request) {
        //发送请求并获取返回值
        String result = HTTP_CLIENT_UTIL.doPostForString(Config.ZHANGYOU_SEND_SMS_URL, request);
        /**
         * 其格式为  1002|00000000000000000000
         * 前4位为状态码，后20位为本次调用流水号,用来对应异步回调接口的状态返回
         */
        return stringToResponseObject(result);
    }

    /**
     * 将返回的string转为对应response对象
     *
     * @param result
     * @return
     */
    public ZhangYouSendSmsSyncResponse stringToResponseObject(String result) {
        try {
            //根据| 分割，获取[0]code 和[1]流水号
            String[] temp = StringUtils.split(result, "|");
            return ZhangYouSendSmsSyncResponse.builder()
                    .code(temp[0])
                    .id(temp[1])
                    .build();
        } catch (Exception e) {
            log.error("【掌游sendSms策略】返回的string转为response对象失败.resultString={},error={}", result, e.getMessage(), e);
            throw new SmsSenderException(ErrorEnum.STRING_TO_RESPONSE_ERROR);
        }
    }


}
