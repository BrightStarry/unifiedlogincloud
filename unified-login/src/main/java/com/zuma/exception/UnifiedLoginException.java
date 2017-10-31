package com.zuma.exception;

import com.zuma.enums.CodeEnum;
import lombok.Getter;

/**
 * author:ZhengXing
 * datetime:2017/10/16 0016 17:33
 * 统一登录-自定义异常
 */
@Getter
public class UnifiedLoginException extends RuntimeException {
    private String code;

    /**
     * 根据异常枚举构造自定义异常
     * @param codeEnum
     */
    public UnifiedLoginException(CodeEnum<String> codeEnum){
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    /**
     * 根据异常码和消息构造自定义异常
     * @param code
     * @param message
     */
    public UnifiedLoginException(String code, String message) {
        super(message);
        this.code = code;
    }

}
