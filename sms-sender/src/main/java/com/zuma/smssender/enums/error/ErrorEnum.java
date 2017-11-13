package com.zuma.smssender.enums.error;

import com.zuma.smssender.config.Config;
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
    HTTP_ERROR("0002","http请求网络异常，无法发送http请求"),
    FORM_ERROR("0003","参数校验失败"),
    PLATFORM_EMPTY("0004","平台不存在"),
    CHANNEL_EMPTY("0005","通道不存在"),
    PHONE_UNKNOWN("0006","存在无法识别手机号"),
    PHONE_NUMBER_OVER("0007","手机号数目超限,最大为" + Config.MAX_PHONE_NUM + "个"),
    SMS_LEN_AND_PHONE_LEN_MISMATCH("0008","短信数和手机号数不匹配，必须为一对一或一对多或多对多且数目相同"),
    UNSUPPORTED_OPERATOR("0009","包含指定通道不支持的运营商"),
    HTTP_RESPONSE_IO_ERROR("0010","http请求response对象，转换时io异常"),
    HTTP_STATUS_CODE_ERROR("0011","http请求状态码非200"),
    STRING_TO_RESPONSE_ERROR("0012","短信API返回参数有误"),
    SEND_SMS_ERROR("0013","短信发送异常"),
    TRANSCODE_ERROR("0014","转码失败"),
    IP_UNALLOW("0015","IP非法"),
    CACHE_EXPIRE("0016","缓存过期,无法解析发送短信接口异步回调信息"),
    SEND_CALLBACK_TO_PLATFORM_ERROR("0017", "发送回调请求到平台失败"),
    SIGN_ERROR("0018","签名不匹配"),
    POOL_ERROR("0019","对象池异常")



    ;
    private String code;
    private String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
