package com.zuma.smssender.enums;

import lombok.Getter;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 10:14
 */
@Getter
public enum  ResultDTOTypeEnum implements CodeEnum<String>{
    COMMON("1","通用（无意义）"),
    SEND_SMS_CALLBACK("2","发送短信异步回调"),
    SMS_UP("3","短信上行推送")
    ;
    private String code;
    private String message;

    ResultDTOTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
