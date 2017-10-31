package com.zuma.enums;

import lombok.Getter;

/**
 * author:Administrator
 * datetime:2017/10/23 0023 15:12
 */
@Getter
public enum ChannelEnum implements CodeEnum<Integer> {
    WEB(0,"网页端"),
    APP(1,"安卓应用端"),
    IOS(2,"苹果应用端"),
    ;

    private Integer code;
    private String message;

    ChannelEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
