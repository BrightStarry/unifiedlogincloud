package com.zuma.smssender.enums;

import lombok.Getter;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 15:30
 */
@Getter
public enum BooleanStatusEnum implements CodeEnum<Integer> {
    TRUE(1,"是"),
    FALSE(0,"否"),
    OTHER(2,"待定")
    ;

    private Integer code;
    private String message;

    BooleanStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
