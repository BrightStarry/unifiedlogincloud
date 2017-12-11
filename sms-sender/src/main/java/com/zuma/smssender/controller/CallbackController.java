package com.zuma.smssender.controller;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.response.smsup.QunZhengSmsUpResponse;
import com.zuma.smssender.dto.response.sendsms.async.KuanXinSendSmsAsyncResponse;
import com.zuma.smssender.dto.response.smsup.KuanXinSmsUpResponse;
import com.zuma.smssender.dto.response.sendsms.async.QunZhengSendSmsAsyncResponse;
import com.zuma.smssender.dto.response.sendsms.async.ZhangYouAsyncResponse;
import com.zuma.smssender.dto.response.self.ZhangYouAsyncResponseResult;
import com.zuma.smssender.enums.ChannelEnum;
import com.zuma.smssender.enums.ZhangYouCallbackMsgTypeEnum;
import com.zuma.smssender.enums.error.ZhangYouErrorEnum;
import com.zuma.smssender.service.CallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * author:Administrator
 * datetime:2017/11/10 0010 08:53
 * 回调接收
 */
@RestController
@RequestMapping("/callback")
public class CallbackController {

    @Autowired
    private CallbackService callbackService;

    /**
     * 掌游平台异步回调
     *
     * @param response
     * @return
     */
    @RequestMapping("/" + Config.ZHANGYOU_PRE)
    public ZhangYouAsyncResponseResult zhangyouCallback(ZhangYouAsyncResponse response) {
        //如果为下行结果报告
        if (ZhangYouCallbackMsgTypeEnum.REPORT.getMessage().equals(response.getMsgType())) {
            boolean flag = callbackService.sendSmsCallbackHandle(response, ChannelEnum.ZHANG_YOU);
        }
        //如果为上行推送
        if (ZhangYouCallbackMsgTypeEnum.MO.getMessage().equals(response.getMsgType())) {
            callbackService.smsUpCallbackHandle(response, ChannelEnum.ZHANG_YOU);
        }

        //返回成功
        return new ZhangYouAsyncResponseResult(ZhangYouErrorEnum.RESPONSE_SUCCESS, new Date());
    }

    /**
     * 宽信平台发送短信异步回调
     *
     * @param response
     */
    @RequestMapping("/" + Config.KUANXIN_PRE + "/sendsms")
    public void kuanxinSendSmsCallback(KuanXinSendSmsAsyncResponse response) {
        callbackService.sendSmsCallbackHandle(response, ChannelEnum.KUAN_XIN);
    }

    /**
     * 群正平台发送短信异步回调,该回调可能会合并多个平台的数据
     *
     * @param response
     */
    @RequestMapping("/" + Config.QUNZHENG_PRE + "/sendsms")
    public void qunzhengSendSmsCallback(@RequestBody QunZhengSendSmsAsyncResponse response) {
        //循环调用
        for (int i = 0; i < response.getResponse(); i++) {
            response.setUniqueSms(response.getSms().get(i));
            callbackService.sendSmsCallbackHandle(response, ChannelEnum.QUN_ZHENG);
        }
    }

    /**
     * 宽信短信上行接口
     */
    @RequestMapping("/" + Config.KUANXIN_PRE + "/smsup")
    public void kuanxinSmsUp(KuanXinSmsUpResponse response) {
        callbackService.smsUpCallbackHandle(response, ChannelEnum.KUAN_XIN);
    }

    /**
     * 群正短信上行接口
     */
    @RequestMapping("/" + Config.QUNZHENG_PRE + "/smsup")
    public void qunzhengSmsUp(@RequestBody QunZhengSmsUpResponse response) {
        //循环调用
        for (int i = 0; i < response.getResponse(); i++) {
            response.setUniqueSms(response.getSms().get(i));
            callbackService.smsUpCallbackHandle(response, ChannelEnum.QUN_ZHENG);
        }
    }


}
