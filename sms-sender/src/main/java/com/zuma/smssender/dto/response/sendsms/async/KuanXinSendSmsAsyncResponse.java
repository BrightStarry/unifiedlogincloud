package com.zuma.smssender.dto.response.sendsms.async;

import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 10:55
 * 宽信发送短信接口异步响应对象
 */
@Data
public class KuanXinSendSmsAsyncResponse {
    private String taskId;//id
    private String code;//状态码
    private String msg;//消息
    private String mobile;//用户手机号
    private String time;//接收时间，需要按yyyyMMddHHmmss 格式，如：20110115105822
}
