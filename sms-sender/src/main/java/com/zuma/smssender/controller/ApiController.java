package com.zuma.smssender.controller;

import com.zuma.smssender.annotation.Verify;
import com.zuma.smssender.controller.base.BaseController;
import com.zuma.smssender.dto.CommonResult;
import com.zuma.smssender.dto.ResultDTO;
import com.zuma.smssender.form.SendSmsForm;
import com.zuma.smssender.service.SendSmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

/**
 * author:Administrator
 * datetime:2017/11/8 0008 09:49
 * api
 */
@Api(value = "接口调用类",description = "/sendSms为平台发送短信调用的接口")
@RestController
@RequestMapping("/")
@Slf4j
public class ApiController extends BaseController {

    @Autowired
    private SendSmsService sendSmsService;


    @ApiOperation(value = "发送短信",httpMethod = "POST",
            notes = "使用通道发送短信，通道若未指定，自动选择通道发送，支持手机号和短信消息 一对一，一对多，多对多（一一对应）",
            response = ResultDTO.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platformId",value = "平台id",required = true),
            @ApiImplicitParam(name = "channel",value = "通道",required = false),
            @ApiImplicitParam(name = "phone",value = "手机号(\",\"分隔)",required = true),
            @ApiImplicitParam(name = "smsMessage",value = "短信消息(\"!&\"分隔)",required = true),
            @ApiImplicitParam(name = "timestamp",value = "毫秒数",required = true),
            @ApiImplicitParam(name = "sign",value = "签名:平台key + 手机号 + 当前毫秒数,做MD5，32,小写",required = true)
    })
    @Verify
    @PostMapping("/sendsms")
    public ResultDTO<CommonResult> sendSms(@ApiIgnore @Valid SendSmsForm sendSmsForm, BindingResult bindingResult){
        //参数基本校验
        isValid(bindingResult,log,"【API发送短信接口】参数校验失败.form={}",sendSmsForm);
        log.info("【API发送短信接口】接收到请求.params={}",sendSmsForm);
        return sendSmsService.sendSms(sendSmsForm);
    }

}
