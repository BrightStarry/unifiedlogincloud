package com.zuma.controller;

import com.zuma.config.Config;
import com.zuma.domain.User;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.exception.FormInvalidException;
import com.zuma.exception.UnifiedLoginException;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.print.Pageable;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 09:39
 * 基础Controller，复用一些方法
 */
public class BaseController {
    private static final String NAV_TOP1 = "navTop1";
    private static final String NAV_TOP2 = "navTop2";

    /**
     * 表单验证
     * @param bindingResult 检验返回对象
     * @param log slf4j日志对象
     * @param logInfo 日志内容
     * @param logObject 日志输出对象
     */
    public void isValid(BindingResult bindingResult, Logger log, String logInfo, Object... logObject) {
        //如果校验不通过,记录日志，并抛出异常
        if (bindingResult.hasErrors()) {
            log.error(logInfo, logObject);
            throw new FormInvalidException(ErrorEnum.FORM_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage());
        }
    }

    /**
     * 从session中获取用户信息
     * @param session
     * @return
     */
    public User getUser(HttpSession session) {
        Object user = session.getAttribute(Config.SESSION_USER_ATTRIBUTE_NAME);
        if (user == null) {
            throw new UnifiedLoginException(ErrorEnum.USER_UNLOGIN);
        }
        return (User)user;
    }

    /**
     * String 非空验证
     */
    public void notEmptyOfString(String... param) {
        for (String temp : param) {
            if (StringUtils.isEmpty(temp)) {
                throw new FormInvalidException(ErrorEnum.FORM_ERROR);
            }
        }
    }

    /**
     * long 非0校验
     */
    public void notZeroOfLong(long... param) {
        for (long temp : param) {
            if (temp == 0) {
                throw new FormInvalidException(ErrorEnum.FORM_ERROR);
            }
        }
    }


    /**
     * 分页请求对象拼接
     */
    public PageRequest getPageRequest(int pageNo, Integer pageSize) {
        return new PageRequest(--pageNo, pageSize == null ? Config.PAGE_SIZE : pageSize, new Sort(Sort.Direction.DESC,"id"));
    }

    /**
     * 设置导航栏
     */
    public void setNavigation(Model model, String navTop1, String navTop2) {
        model.addAttribute(NAV_TOP1, navTop1);
        model.addAttribute(NAV_TOP2, navTop2);
    }



}
