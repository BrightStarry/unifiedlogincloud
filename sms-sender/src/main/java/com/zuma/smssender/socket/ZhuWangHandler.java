package com.zuma.smssender.socket;

import com.zuma.smssender.config.Config;
import com.zuma.smssender.dto.zhuwang.ZhuWangActiveTestAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangConnectAPI;

import com.zuma.smssender.dto.zhuwang.ZhuWangDeliverAPI;
import com.zuma.smssender.dto.zhuwang.ZhuWangSubmitAPI;
import com.zuma.smssender.enums.ZhuWangCommandIdEnum;
import com.zuma.smssender.enums.error.ZhuWangSubmitErrorEnum;
import com.zuma.smssender.template.sendsms.callback.ZhuWangSendSmsAsyncCallbackTemplate;
import com.zuma.smssender.template.sendsms.callback.ZhuWangSendSmsCallbackTemplate;
import com.zuma.smssender.template.smsup.ZhuWangSmsUpCallbackTemplate;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Semaphore;

/**
 * author:ZhengXing
 * datetime:2017/11/23 0023 14:05
 * 筑望 通道时间处理类, 实现其适配器类
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ZhuWangHandler extends ChannelHandlerAdapter {

    //筑望 发送器
    private static ZhuWangSender zhuwangSender;

    //筑望同步(submit接口response)处理类
    private static ZhuWangSendSmsCallbackTemplate zhuWangSendSmsCallbackTemplate;

    //筑望异步(deliver接口request)处理类
    private static ZhuWangSendSmsAsyncCallbackTemplate zhuWangSendSmsAsyncCallbackTemplate;

    //筑望短信上行处理类
    private static ZhuWangSmsUpCallbackTemplate zhuWangSmsUpCallbackTemplate;

    //当前处理类对应的socket连接对应的相关属性
    private SocketPair socketPair;

    @Autowired
    public void init(ZhuWangSender zhuwangSender,
                     ZhuWangSendSmsCallbackTemplate zhuWangSendSmsCallbackTemplate,
                     ZhuWangSendSmsAsyncCallbackTemplate zhuWangSendSmsAsyncCallbackTemplate,
                     ZhuWangSmsUpCallbackTemplate zhuWangSmsUpCallbackTemplate) {
//        zhuWangHandler  = this;
//        zhuWangHandler.zhuwangSender = this.zhuwangSender;
//        zhuWangHandler.zhuWangSendSmsCallbackTemplate = this.zhuWangSendSmsCallbackTemplate;
//        zhuWangHandler.zhuWangSendSmsAsyncCallbackTemplate = this.zhuWangSendSmsAsyncCallbackTemplate;
//        zhuWangHandler.zhuWangSmsUpCallbackTemplate = this.zhuWangSmsUpCallbackTemplate;
        ZhuWangHandler.zhuwangSender = zhuwangSender;
        ZhuWangHandler.zhuWangSendSmsCallbackTemplate = zhuWangSendSmsCallbackTemplate;
        ZhuWangHandler.zhuWangSendSmsAsyncCallbackTemplate = zhuWangSendSmsAsyncCallbackTemplate;
        ZhuWangHandler.zhuWangSmsUpCallbackTemplate = zhuWangSmsUpCallbackTemplate;

    }

    /**
     * 通道激活后.初始化对应socketPair
     */
    public void generateSocketPair(ChannelHandlerContext ctx) {
        //公平锁的信号量
        SocketPair socketPair = new SocketPair(ctx.executor(), ctx,
                new Semaphore(Config.ZHUWANG_SOCKET_QPS, true));
        this.socketPair = socketPair;
        //保存该socket信息到socketStore
        SocketStore.add(socketPair);
    }

    /**
     * 关闭时,清理各类资源
     */
    public void clean() {
        this.socketPair.clean();
    }

    /**
     * 通道激活
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //生成通道相关属性
        generateSocketPair(ctx);
        log.debug("[筑望]连接到服务器,通道激活,发送器可用.");
        //发送连接请求
        zhuwangSender.sendConnectRequest();

    }



    /**
     * 读取到消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //处理连接响应
        if (msg instanceof ZhuWangConnectAPI.Response) {
            ZhuWangConnectAPI.Response response = (ZhuWangConnectAPI.Response) msg;
            log.info("[筑望]handler接收到connectResponse消息:{}", response);

//            zhuwangSender.sendSms("【口袋铃声】您的验证码是123456，请在3分钟内操作。", "17826824998");
        //处理发送短信响应
        } else if (msg instanceof ZhuWangSubmitAPI.Response) {
            ZhuWangSubmitAPI.Response response = (ZhuWangSubmitAPI.Response) msg;
            log.info("[筑望]handler接收到submitResponse消息:{}", response);

            //处理该同步(算是同步把)回调结果,如果此处失败,直接返回调用方失败,此处成功,不做任何处理
            zhuWangSendSmsCallbackTemplate.callbackHandle(response);

        //处理状态报告或短信上行
        } else if (msg instanceof ZhuWangDeliverAPI.Request) {
            ZhuWangDeliverAPI.Request request = (ZhuWangDeliverAPI.Request) msg;
            log.info("[筑望]handler接收到DeliverRequest消息:{}", request);
            //给筑望服务器响应
            zhuwangSender.sendDeliverResponse(request, ZhuWangSubmitErrorEnum.SUCCESS);

            //如果是状态推送,也就是发送短信的异步回调,
            if(request.getRegisteredDeliver() == 1){
                zhuWangSendSmsAsyncCallbackTemplate.callbackHandle(request);
            }else{
                //否则就是短信上行处理
                zhuWangSmsUpCallbackTemplate.callbackHandle(request);
            }
        }
        //处理链路检测请求
        else if (msg instanceof ZhuWangActiveTestAPI.Response) {
            ZhuWangActiveTestAPI.Response response = (ZhuWangActiveTestAPI.Response) msg;
            //判断是对方请求我们,还是响应我们的请求
            if (response.getCommandId().equals(ZhuWangCommandIdEnum.CMPP_ACTIVE_TEST.getCode())) {
                //如果是对方请求我们,发送响应
                zhuwangSender.sendActiveTest(response.getSequenceId());
            }else{
                //如果是他们响应我们的请求
                log.info("[筑望]handler收到对方链路检测响应消息.");
            }
        }

    }

    /**
     * 用户自定义事件触发
     * 如果超过x秒未收到筑望服务端的返回数据,发送链路检测
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //如果不是 空闲状态事件,执行父类方法
        if (!(evt instanceof IdleStateEvent))
            super.userEventTriggered(ctx,evt);


        IdleStateEvent event = (IdleStateEvent) evt;
        //如果不是读取空闲超时事件
        if(event.state() != IdleState.READER_IDLE)
            return;

        //发送链路检测
        zhuwangSender.sendActiveTest(null);

        //TODO 开启线程等待链路检测结果
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("筑望socket异常.error={}", cause.getMessage());
    }
}
