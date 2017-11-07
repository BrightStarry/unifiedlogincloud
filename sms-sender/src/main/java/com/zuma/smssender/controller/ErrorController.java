package com.zuma.smssender.controller;

import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.util.ResultDTOUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * author:ZhengXing
 * datetime:2017/10/17 0017 13:09
 * 异常返回控制类
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    /**
     * ajax请求异常返回
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/")
    public ResultDTO<?> commonJson(HttpServletRequest request) {
        return ResultDTOUtil.error((String) request.getAttribute("code"), (String) request.getAttribute("message"));
    }

}
