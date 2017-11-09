package com.zuma.smssender.strategy;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.ZhangYouCacheDTO;
import com.zuma.smssender.dto.ZhangYouResult;
import com.zuma.smssender.dto.request.ZhangYouSendSmsRequest;
import com.zuma.smssender.dto.response.ZhangYouSendSmsSyncResponse;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.SmssAndPhonesRelationEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.enums.error.ZhangYouErrorEnum;
import com.zuma.smssender.exception.SmsSenderException;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.CacheUtil;
import com.zuma.smssender.util.CodeUtil;
import com.zuma.smssender.util.EnumUtil;
import com.zuma.smssender.util.PhoneUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 15:46
 * 掌游，策略实现类
 */
@Slf4j
public class ZhangYouSendSmsStrategy implements SendSmsStrategy {


    /**
     * 发送短信，根据实体和　手机号数组包含的所有不同运营商数组 和  短信消息-手机号对应关系
     *
     * @param sendSmsForm               api收到的消息
     * @param containOperators          手机号数组包含的所有不同的运营商
     * @param smssAndPhonesRelationEnum 短信消息-手机号 关系，例如1-1；1-*；*-*
     * @return 返回对象
     */
    @Override
    public  ResultDTO<?> sendSms(SendSmsForm sendSmsForm, PhoneOperatorEnum[] containOperators, SmssAndPhonesRelationEnum smssAndPhonesRelationEnum) {
        //接口请求次数
        int apiRequestCount = 0;
        //每次调用返回对象数组
        List<ResultDTO<ErrorData>> resultDTOList = new ArrayList<>();

        //判断关系,根据一对一、一对多、多对多分别使用不同方法
        switch (smssAndPhonesRelationEnum.getCode()) {
            //1-1
            case 1:
                //api调用次数累加--Integer也是值传递，所以直接放在方法外
                apiRequestCount++;
                //相同步骤封装
                loop(containOperators[0],sendSmsForm.getPhones(),sendSmsForm.getSmsMessage(),resultDTOList,sendSmsForm);
                break;
            //1-*
            case 2:
                //循环运营商，将手机号分为多个运营商组发送
                for (PhoneOperatorEnum phoneOperatorEnum : containOperators) {
                    //api调用次数累加
                    apiRequestCount++;
                    loop(phoneOperatorEnum,
                            PhoneUtil.getPhonesByOperator(sendSmsForm.getPhones(), phoneOperatorEnum),
                            sendSmsForm.getSmsMessage(),
                            resultDTOList,sendSmsForm);
                }
                break;
            //*-*
            case 3:
                //切割手机号
                String[] phones = StringUtils.split(sendSmsForm.getPhones(),Config.PHONES_SEPARATOR);
                //切割消息
                String[] smsMessages = StringUtils.split(sendSmsForm.getSmsMessage(), Config.SMS_MESSAGE_SEPARATOR);
                //循环手机号
                for (int i = 0; i < phones.length; i++) {
                    loop(PhoneUtil.getPhoneOperator(phones[i])[0],//手机号运营商
                            phones[i],
                            smsMessages[i],
                            resultDTOList,
                            sendSmsForm);
                }
                break;
        }

        //拼接返回对象<T>中的T
        ZhangYouResult zhangYouResult = new ZhangYouResult(apiRequestCount, resultDTOList);
        //判断是否有异常返回
        if(CollectionUtils.isEmpty(resultDTOList))
            return ResultDTO.success(zhangYouResult);
        //如果有异常
        return ResultDTO.error(ErrorEnum.SEND_SMS_ERROR,zhangYouResult);
    }

    /**
     * 封装每个for循环中相同的部分
     * @param phoneOperatorEnum  手机运营商
     * @param phone 手机号码s
     * @param smsMessage 短信消息s
     * @param resultDTOList 异常返回list
     */
    private void loop(PhoneOperatorEnum phoneOperatorEnum,
                     String phone,String smsMessage,
                     List<ResultDTO<ErrorData>> resultDTOList,
                     SendSmsForm sendSmsForm){
        //获取当前通道、当前运营商帐号
        CommonSmsAccount account = ACCOUNTS.getAccounts()[ChannelEnum.ZHANG_YOU.getCode()][phoneOperatorEnum.getCode()];
        //调用http请求工具，获取response并解析为返回对象返回
        ResultDTO<ErrorData> resultDTO = getResponse(account, phone, smsMessage,sendSmsForm);
        //如果成功将返回对象加入数组
        if(ResultDTO.isSuccess(resultDTO))
            resultDTOList.add(resultDTO);
    }

    /**
     * 直接获取response，组合下面两个方法。
     * 并解析response对象，组装为ResultDTO<ErrorData>
     * @param account
     * @param phones
     * @param smsMessae
     * @return
     */
    private ResultDTO<ErrorData> getResponse(CommonSmsAccount account,
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
        ZhangYouCacheDTO cacheDTO = ZhangYouCacheDTO.builder()
                .id(response.getId())//流水号
                .platformId(sendSmsForm.getPlatformId())//平台id
                .timestamp(sendSmsForm.getTimestamp())//时间戳
                .phones(phones)//手机号
                .smsMessage(smsMessae)//短信消息
                .build();
        //存入缓存,key使用 掌游前缀 + 流水号
        CacheUtil.put(Config.ZHANGYOU_PRE + response.getId(),cacheDTO);

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
    private ZhangYouSendSmsRequest toRequestObject(CommonSmsAccount account, String phones, String msg) {
        //签名
        String sign = CodeUtil.StringToMd5(account.getAKey() + account.getCKey());
        //创建请求对象
        return ZhangYouSendSmsRequest.builder()
                .sid(account.getAKey())
                .cpid(account.getBKey())
                .sign(sign)
                .mobi(phones)
                .msg(msg)
                .build();
    }

    /**
     * 发送http请求，并将返回数据组装为对应的response对象
     *
     * @param request
     * @return
     */
    private ZhangYouSendSmsSyncResponse sendHttpRequest(ZhangYouSendSmsRequest request) {
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
    private ZhangYouSendSmsSyncResponse stringToResponseObject(String result) {
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
