package com.zuma.enums;

import lombok.Getter;

/**
 * author:Administrator
 * datetime:2017/11/1 0001 16:26
 */
@Getter
public enum ServiceEnum implements CodeEnum<Integer>{
    A(1,"服务A"),
    B(2,"服务B"),
    C(3,"服务C"),
    D(4,"服务D"),
    ;
    private Integer code;
    private String message;

    ServiceEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
