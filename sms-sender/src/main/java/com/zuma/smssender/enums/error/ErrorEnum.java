package com.zuma.smssender.enums.error;

import com.zuma.smssender.enums.CodeEnum;
import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/11/7 0007 16:42
 * 异常状态枚举
 */
@Getter
public enum ErrorEnum implements CodeEnum<String> {
    SUCCESS("0000","成功"),
    UNKNOWN_ERROR("0001","发生未知异常"),
    HTTP_ERROR("0002","网络异常，无法发送http请求"),

    ;
    private String code;
    private String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
