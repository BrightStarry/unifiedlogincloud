package com.zuma.smssender.strategy;

import com.zuma.smssender.config.CommonSmsAccount;
import com.zuma.smssender.config.Config;
import com.zuma.smssender.config.SmsAccountCollection;
import com.zuma.smssender.dto.CommonResult;
import com.zuma.smssender.dto.ErrorData;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.dto.request.ZhangYouSendSmsRequest;
import com.zuma.smssender.dto.response.ZhangYouSendSmsSyncResponse;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.PhoneOperatorEnum;
import com.zuma.smssender.enums.SmssAndPhonesRelationEnum;
import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.util.HttpClientUtil;
import com.zuma.smssender.util.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:25
 * 发送短信参数策略模式 接口
 */
public abstract class SendSmsTemplate<R, P> {
    //帐号s
    SmsAccountCollection ACCOUNTS = SmsAccountCollection.getInstance();
    //httpclient工具类
    HttpClientUtil HTTP_CLIENT_UTIL = HttpClientUtil.getInstance();

    /**
     * 发送短信，根据实体和　手机号数组包含的所有不同运营商数组 和  短信消息-手机号对应关系
     *
     * @param sendSmsForm               api收到的消息
     * @param containOperators          手机号数组包含的所有不同的运营商
     * @param smssAndPhonesRelationEnum 短信消息-手机号 关系，例如1-1；1-*；*-*
     * @return 返回对象
     */
    public  ResultDTO<?> sendSms(SendSmsForm sendSmsForm, PhoneOperatorEnum[] containOperators, SmssAndPhonesRelationEnum smssAndPhonesRelationEnum){
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
                String[] phones = StringUtils.split(sendSmsForm.getPhones(), Config.PHONES_SEPARATOR);
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

        return errorData(apiRequestCount, resultDTOList);
    }


    ResultDTO<?> errorData(int apiRequestCount, List<ResultDTO<ErrorData>> resultDTOList){
        //拼接返回对象<T>中的T
        CommonResult commonResult = new CommonResult(apiRequestCount, resultDTOList);
        //判断是否有异常返回
        if(CollectionUtils.isEmpty(resultDTOList))
            return ResultDTO.success(commonResult);
        //如果有异常
        return ResultDTO.error(ErrorEnum.SEND_SMS_ERROR, commonResult);
    }
    /**
     * 封装每个for循环中相同的部分
     *
     * @param phoneOperatorEnum 手机运营商
     * @param phone             手机号码s
     * @param smsMessage        短信消息s
     * @param resultDTOList     异常返回list
     */
    void loop(PhoneOperatorEnum phoneOperatorEnum,
                       String phone, String smsMessage,
                       List<ResultDTO<ErrorData>> resultDTOList,
                       SendSmsForm sendSmsForm){
        //获取当前通道、当前运营商帐号
        CommonSmsAccount account = ACCOUNTS.getAccounts()[ChannelEnum.ZHANG_YOU.getCode()][phoneOperatorEnum.getCode()];
        //调用http请求工具，获取response并解析为返回对象返回
        ResultDTO<ErrorData> resultDTO = getResponse(account, phone, smsMessage, sendSmsForm);
        //如果成功将返回对象加入数组
        if (ResultDTO.isSuccess(resultDTO))
            resultDTOList.add(resultDTO);
    }

    /**
     * 直接获取response，组合下面两个方法。
     * 并解析response对象，组装为ResultDTO<ErrorData>
     *
     * @param account
     * @param phones
     * @param smsMessae
     * @return
     */
    abstract ResultDTO<ErrorData> getResponse(CommonSmsAccount account,
                                              String phones, String smsMessae,
                                              SendSmsForm sendSmsForm);

    /**
     * 根据帐号、手机号s，短信消息 转 请求对象
     *
     * @param account
     * @param phones
     * @param msg
     * @return
     */
    abstract R toRequestObject(CommonSmsAccount account, String phones, String smsMessage);


    /**
     * 发送http请求，并将返回数据组装为对应的response对象
     *
     * @param request
     * @return
     */
    abstract P sendHttpRequest(R request);

    /**
     * 将返回的string转为对应response对象
     *
     * @param result
     * @return
     */
    abstract P stringToResponseObject(String result);
}
