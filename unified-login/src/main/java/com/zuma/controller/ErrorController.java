package com.zuma.controller;

import com.zuma.config.Config;
import com.zuma.enums.error.ErrorEnum;
import com.zuma.util.ResultVOUtil;
import com.zuma.vo.ResultVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
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
     */
    @ResponseBody
    @RequestMapping(value = "/")
    public ResultVO<?> commonJson(HttpServletRequest request) {
        return ResultVOUtil.error((String) request.getAttribute("code"), (String) request.getAttribute("message"));
    }

    /**
     * 普通请求异常返回
     */
    @RequestMapping(value = "/", produces = "text/html")
    public String commonHtml(HttpServletRequest request) {
        return Config.ERROR_URL;
    }


    /**
     * 404-页面
     */
    @RequestMapping(value = "/404",produces = "text/html")
    public String error404Html(Model model) {
        model.addAttribute("code", ErrorEnum.NOT_FOUND.getCode());
        model.addAttribute("message", ErrorEnum.NOT_FOUND.getMessage());
        return Config.ERROR_URL;
    }

    /**
     * 404-json
     */
    @ResponseBody
    @RequestMapping(value = "/404")
    public ResultVO<?> error404 () {
        return  ResultVOUtil.error(ErrorEnum.NOT_FOUND.getCode(), ErrorEnum.NOT_FOUND.getMessage());
    }
}
