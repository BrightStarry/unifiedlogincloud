package com.zuma.smssender.socket;

import com.zuma.smssender.config.Config;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * author:ZhengXing
 * datetime:2017/11/23 0023 13:55
 * 筑望 连接到服务器
 */
@Slf4j
@Component
public class SocketConnectBootstrap {
    //每个该类对应一个新的handler,主要为了保存每个socket连接各自的一些属性,方便关闭时清理资源
    private ZhuWangHandler zhuWangHandler = new ZhuWangHandler();

    @Autowired
    private ZhuWangDecoder zhuWangDecoder;

    @Autowired
    private ZhuWangEncoder zhuWangEncoder;


    /**
     * 异步启动
     */
    public  void start() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                start1(1,new Date());
            }
        });
    }

    /**
     * 启动
     * @throws Exception
     */
    private void start1(int retryNum,Date lastTryTime) {
        try {
            //创建线程组
            EventLoopGroup workLoopGroup = new NioEventLoopGroup();
            //创建引导程序
            Bootstrap bootstrap = new Bootstrap();
            //保持长连接
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            //将线程加入bootstrap
            bootstrap.group(workLoopGroup)
                    //使用指定通道类
                    .channel(NioSocketChannel.class)
                    //设置日志
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //重写通道初始化方法
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline()
                                    /**
                                     * 心跳检测：超过xs未触发触发读取事件，则触发userEventTriggered()事件
                                     */
                                    .addLast("idleState handler",new IdleStateHandler(Config.ZHUWANG_ACTIVE_TEST_SECOND,0,0, TimeUnit.SECONDS))
                                    /**
                                     * 长度解码器，防止粘包拆包
                                     * @param maxFrameLength 解码时，处理每个帧数据的最大长度
                                     * @param lengthFieldOffset 该帧数据中，存放该帧数据的长度的数据的起始位置
                                     * @param lengthFieldLength 记录该帧数据长度的字段本身的长度
                                     * @param lengthAdjustment 修改帧数据长度字段中定义的值，可以为负数
                                     * @param initialBytesToStrip 解析的时候需要跳过的字节数
                                     * @param failFast 为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
                                     */
//                                    .addLast("length decoder",
//                                            new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
                                    /**
                                     * 消息解码器，将收到的消息进行转换
                                     */
                                    .addLast("logMessage decoder", zhuWangDecoder)
                                    /**
                                     * 消息编码器，将发送的消息进行转换,使用编码器后会有问题..
                                     */
//                                    .addLast("logMessage encoder",zhuWangEncoder )
                                    /**
                                     * 长度编码器，防止粘包拆包
                                     * 第一个参数为 长度字段长度
                                     * 第二个为参数为 长度是否包含长度字段长度
                                     */
                                    .addLast("length encoder",new LengthFieldPrepender(4,true))
                                    /**
                                     * 超过s秒未触发读取事件，关闭
                                     */
//                                    .addLast(new ReadTimeoutHandler(LogClientConfig.TIMEOUT_SECONDS))
                                    /**
                                     * 自定义处理器
                                     */
                                    .addLast(zhuWangHandler);
                        }
                    });
            //链接到服务端
            ChannelFuture channelFuture = bootstrap.connect(Config.ZHUWANG_IP,Config.ZHUWANG_PORT).sync();
            log.info("筑望客户端启动成功");
            //关闭前阻塞
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("筑望客户端启动失败.error={}",e.getMessage(),e);
        }finally {
            //清理资源
            zhuWangHandler.clean();

            /**
             * 重试机制:
             * 该重试机制会一直在retryStart方法和start1方法间循环,
             * 在未到达重试次数限制,陷入暂停前,不会执行到下面的start1()
             * 一旦执行了下面这句start1,则表明已经经历了一次重试失败机制,
             * 再次开始新的连接
             */
            retryStart(retryNum,lastTryTime);

            //执行到这里表示已经经历过一次重试x次失败的等待.继续执行该方法即可..重置重试次数为1
            start1(1,new Date());
        }

    }

    /**
     * 重试机制
     */
    private  void  retryStart(int retryNum,Date lastTryTime){
        //如果距离上一次重试的时间,超过x分钟
        if (new Date().after(DateUtils.addSeconds(lastTryTime, Config.ZHUWANG_RETRY_CONNECT_TIME))) {
            lastTryTime = new Date();
            retryNum = 0;
        }


        //重试机制,如果重试次数超过限制。暂停x秒后再重试
        try {
            if(retryNum >= Config.ZHUWANG_CONNECT_MAX_RETRY_NUMBER){
                log.warn("[筑望]socket出错.超过最大重试次数.进入暂停状态.");
                TimeUnit.SECONDS.sleep(Config.ZHUWANG_RETRY_CONNECT_TIME);
                log.info("[筑望]socket从暂停状态恢复.");
                //如果被暂停了.跳出该重试方法
                return;
            }
        } catch (Exception e) {
            log.error("筑望客户端尝试重启失败。error={}",e.getMessage());
        }

        //否则重启,并递增重试次数
        lastTryTime = new Date();
        retryNum++;
        log.warn("[筑望]socket出错,正在重试.第{}次",retryNum);
        start1(retryNum,lastTryTime);
    }
}
