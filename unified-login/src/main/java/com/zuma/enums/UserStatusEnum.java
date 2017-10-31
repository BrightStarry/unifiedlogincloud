package com.zuma.enums;

import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 09:28
 * 用户状态枚举
 */
@Getter
public enum UserStatusEnum implements CodeEnum<Integer>{

    VALID(1,"有效"),
    INVALID(0,"无效");

    private Integer code;
    private String message;

    UserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
