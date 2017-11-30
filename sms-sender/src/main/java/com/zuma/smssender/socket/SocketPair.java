package com.zuma.smssender.socket;

import com.zuma.smssender.config.Config;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import lombok.Data;

import java.util.concurrent.*;

/**
 * author:ZhengXing
 * datetime:2017/11/30 0030 14:24
 * socket对,封装socket相关的若干属性
 */
@Data
public class SocketPair {
    //时间循环组,用来关闭socket
    private EventExecutor eventExecutor;
    //通道,用来使用该socket发送数据
    private ChannelHandlerContext channelHandlerContext;
    //信号量,表示当前该socket的并发数
    private Semaphore semaphore;
    //清理器-自身维护的定时线程池,用来每若干秒清空信号量的值
    private ScheduledExecutorService cleaner;
    //是否关闭
    private Boolean open = false;

    /**
     * setup方法,开启定时线程,限制并发
     */
    public void setup() {
        this.cleaner = Executors.newScheduledThreadPool(1);
        //该方法能保证固定频率的执行任务,如果任务延期则会发生任务并发(但Semaphore类释放过界长度的信号,不会有问题)
        //0.5秒后开始执行,每秒0.5秒执行一次,释放一半数量的信号
        //在进行发送数据操作时,将只获取信号量,不再释放信号量
        cleaner.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                semaphore.release(Config.ZHUWANG_SOCKET_QPS / 2);
            }
        }, 500, 500, TimeUnit.MILLISECONDS);
        //状态:打开
        open = true;
    }
    /**
     * 获取通道
     */
    public Channel getChannel() {
        return channelHandlerContext.channel();
    }

    /**
     * 是否关闭
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * 关闭时清理资源
     */
    public void clean() {
        if (cleaner != null && !cleaner.isShutdown())
            cleaner.shutdown();
        cleaner = null;
        if (channelHandlerContext != null)
            channelHandlerContext.close();
        channelHandlerContext = null;
        if (eventExecutor != null && !eventExecutor.isShutdown())
            eventExecutor.shutdownGracefully();
        eventExecutor = null;
        //状态:关闭
        open=false;
    }

    /**
     * 获取当前并发数
     */
    public int getCurrentQPS() {
        //总并发数 - 当前可用并发数
        return Config.ZHUWANG_SOCKET_QPS - semaphore.availablePermits();
    }

    public SocketPair(EventExecutor eventExecutor, ChannelHandlerContext channelHandlerContext, Semaphore semaphore) {
        this.eventExecutor = eventExecutor;
        this.channelHandlerContext = channelHandlerContext;
        this.semaphore = semaphore;
        //启动定时清理器
        setup();
    }

    private SocketPair(){}

}
