package com.zuma.controller;

import com.zuma.annotation.Logined;
import com.zuma.config.Config;
import com.zuma.form.AccountForm;
import com.zuma.form.LoginForm;
import com.zuma.service.SystemService;
import com.zuma.service.UserService;
import com.zuma.util.ResultVOUtil;
import com.zuma.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * author:ZhengXing
 * datetime:2017/10/18 0018 09:30
 * 系统控制类
 */
@Api(value = "接口调用类",description = "其中的/verify为平台进行登录校验时调用的接口")
@Controller
@RequestMapping("/")
@Slf4j
public class SystemController extends BaseController{
    @Autowired
    private SystemService systemService;



    /**
     * 登录页面
     */
    @GetMapping({"/login","/"})
    public String loginView() {
        return "login";
    }


    /**
     * 登录
     */
    @ResponseBody
    @PostMapping("/account/login")
    public ResultVO<?> login(@Valid AccountForm accountForm, BindingResult bindingResult, HttpSession session) {
        //表单验证
        isValid(bindingResult,log,"【系统登录】用户名密码不符合规定.", accountForm);
        //账号密码验证
        systemService.login(accountForm);
        //登录
        session.setAttribute(Config.SESSION_USER_ATTRIBUTE_NAME, accountForm);
        log.info("【登录】用户{}登录成功.", accountForm.getUsername());
        return ResultVOUtil.success();
    }

    /**
     * 注销
     */
    @Logined
    @GetMapping("/exit")
    public String exit(HttpSession session) {
        session.setAttribute(Config.SESSION_USER_ATTRIBUTE_NAME, null);
        //返回登录页面
        return "redirect:/";
    }
}
