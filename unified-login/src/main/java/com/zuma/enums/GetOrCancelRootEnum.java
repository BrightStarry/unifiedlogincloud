package com.zuma.enums;

import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 13:06
 * 获取/取消授权枚举
 */
@Getter
public enum GetOrCancelRootEnum implements CodeEnum<Integer>{
    GET_ROOT(1,"获取所有授权"),
    CANCEL_ROOT(0,"取消所有授权");

    private Integer code;
    private String message;

    GetOrCancelRootEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
