package com.zuma.controller;

import com.zuma.form.LoginForm;
import com.zuma.service.UserService;
import com.zuma.util.ResultVOUtil;
import com.zuma.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * author:ZhengXing
 * datetime:2017/10/23 0023 16:41
 * 接口调用类
 */

@Api(value = "接口调用类",description = "其中的/verify为平台进行登录校验时调用的接口")
@RequestMapping("/")
@RestController
@Slf4j
public class ApiController extends BaseController{

    @Autowired
    private UserService userService;

    /**
     * 判断某用户是否允许登录某平台
     */
    @ApiOperation(value = "登录校验",httpMethod = "POST",notes = "判断某账号是否可以登录某平台",
            response = ResultVO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "平台id",required = true),
            @ApiImplicitParam(name = "username",value = "用户名",required = true),
            @ApiImplicitParam(name = "password",value = "密码(加上指定盐后MD5)",required = true),
            @ApiImplicitParam(name = "channel",value = "渠道.0:网页；1：安卓；2：ios",required = true)
    })
    @PostMapping("/verify")
    public ResultVO<?> verify(@ApiIgnore @Valid LoginForm loginForm, BindingResult bindingResult) {
        //表单验证
        isValid(bindingResult, log, "【验证某用户是否允许登录某平台】表单校验失败.form={}",loginForm);
        //登录验证
        userService.login(loginForm);
        //返回成功
        return ResultVOUtil.success();
    }


    @PostMapping("/test")
    public ResultVO<?> test(String test){
        log.info(test);
        return ResultVOUtil.success();
    }
}
