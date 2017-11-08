package com.zuma.smssender.controller;

import com.zuma.smssender.enums.error.ErrorEnum;
import com.zuma.smssender.exception.FormInvalidException;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpSession;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 09:39
 * 基础Controller，复用一些方法
 */
public class BaseController {


    /**
     * 表单验证
     * @param bindingResult 检验返回对象
     * @param log slf4j日志对象
     * @param logInfo 日志内容
     * @param logObject 日志输出对象
     */
    protected void isValid(BindingResult bindingResult, Logger log, String logInfo, Object... logObject) {
        //如果校验不通过,记录日志，并抛出异常
        if (bindingResult.hasErrors()) {
            log.error(logInfo, logObject);
            throw new FormInvalidException(ErrorEnum.FORM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
    }


    /**
     * String 非空验证
     */
    protected void notEmptyOfString(String... param) {
        for (String temp : param) {
            if (StringUtils.isEmpty(temp)) {
                throw new FormInvalidException(ErrorEnum.FORM_ERROR);
            }
        }
    }

    /**
     * long 非0校验
     */
    protected void notZeroOfLong(long... param) {
        for (long temp : param) {
            if (temp == 0) {
                throw new FormInvalidException(ErrorEnum.FORM_ERROR);
            }
        }
    }



}
