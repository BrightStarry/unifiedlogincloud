package com.zuma.exception;

import com.zuma.enums.CodeEnum;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 09:48
 * 表单检验异常
 */
public class FormInvalidException extends UnifiedLoginException {
    public FormInvalidException(CodeEnum<String> codeEnum) {
        super(codeEnum);
    }

    public FormInvalidException(String code, String message) {
        super(code, message);
    }
}
