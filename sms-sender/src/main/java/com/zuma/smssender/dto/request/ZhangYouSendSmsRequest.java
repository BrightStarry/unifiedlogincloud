package com.zuma.smssender.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * author:Administrator
 * datetime:2017/11/7 0007 17:44
 * 掌游接口请求参数
 */
@Data
@Builder
public class ZhangYouSendSmsRequest {
    private String sid;//* 合作商家的企业代码，每个合作商家仅有一个。由我方分配，形式如:10010001。
    private String cpid;//* 业务代码，用于区分同一合作商的不同业务。由我方分配，形式如:600100。
    private String mobi;//* 待发送的手机号码，多个号码用半角逗号隔开，但不能超过规定的个数（暂定为30个）。
    private String sign;//* 安全签名，生成方式：cpid+key的MD5加密（小写，32位）。
    //* 待发送短信内容，字符个数不能超过210个。
    // 计费方式：70字符一条。短信内容需要采用BASE64（参加附1：Base64.java）进行编码，然后URLEncode编码。
    // 其中socket方式需以字符串的形式传输。
    private String msg;
    private Integer spcode;//自定义加长接入号。(2位数字)
}
