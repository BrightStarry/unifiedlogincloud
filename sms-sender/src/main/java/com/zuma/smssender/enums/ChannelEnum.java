package com.zuma.smssender.enums;

import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 16:44
 * 短信通道 枚举
 */
@Getter
public enum ChannelEnum implements CodeEnum<String> {
    ZHANG_YOU("1","掌游"),
    KUAN_XIN("2","宽信"),
    QUN_ZHENG("3","群正")
    ;
    private String code;
    private String message;

    ChannelEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
