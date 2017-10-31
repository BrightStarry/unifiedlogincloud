package com.zuma.enums.error;

import com.zuma.enums.CodeEnum;
import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:55
 * 异常枚举
 */
@Getter
public enum ErrorEnum implements CodeEnum<String> {
    //通用
    SUCCESS("0000","成功"),
    UNKNOWN_ERROR("0001","系统异常"),

    //内部
    USER_ERROR("0002","用户不存在或密码错误"),
    USER_EMPTY("0003","用户不存在"),
    UPDATE_AFTER_BEFORE_SAME("0004","值修改前后相同"),
    PLATFORM_EMPTY("0005","平台不存在"),
    NAME_REPEAT("0006","名字重复"),
    SYSTEM_ACCOUNT_ERROR("0007","账号或密码错误"),
    FORM_ERROR("0008","表单参数异常"),
    USER_UNLOGIN("0012","用户未登录"),
    USER_PLATFORM_ALREADY_ROOT("0013","用户-平台已经授权"),

    //HTTP
    NOT_FOUND("404","路径不见鸟"),

    //接口
    API_ID_ERROR("0002","id错误"),
    API_USER_EMPTY("0003","用户名错误，用户不存在"),
    API_PWD_ERROR("0004","密码错误"),
    API_USER_INVALID("0010","用户无效，未启用该账号"),
    API_USER_NOT_ROOT("0011","用户未取得该平台授权"),









    ;
    private String code;
    private String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
