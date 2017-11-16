package com.zuma.netty;

import com.zuma.config.LogServerConfig;
import com.zuma.handler.ServerHandler;
import com.zuma.serializer.LogDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * author:ZhengXing
 * datetime:2017/10/31 0031 10:08
 *
 * 服务端
 */
@Component
@Slf4j
public class LogServiceBootstrap {

    private LogServerConfig logServerConfig = new LogServerConfig();

    private static Integer RETRY_NUM = 0;

    /**
     * 服务启动-异步启动
     * @throws InterruptedException
     */
    public  void start() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    start1();
                } catch (InterruptedException e) {
                    log.error("服务端线程启动失败。error={}",e.getMessage());
                }
            }
        });
    }
    //服务启动
    private  void start1() throws InterruptedException {
        try {
            //1. 接收client连接的线程组
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            //2. 实际业务操作的线程组
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            //3. 辅助类Bootstrap，对Server进行配置
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //4. 将线程组加入到Bootstrap
            serverBootstrap.group(bossGroup, workerGroup)
                    //配置本通道,指定通道类型(channelXXX()是配置子通道)
                    .channel(NioServerSocketChannel.class)
                    //绑定具体的事件处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    /**
                                     * 空闲状检测：超过xs未触发触发读取事件，则触发userEventTriggered()事件
                                     */
                                    .addLast("idleState handler",new IdleStateHandler(logServerConfig.getReadIdleTimeout(),0,0, TimeUnit.SECONDS))
                                    /**
                                     * 长度解码器，防止粘包拆包
                                     */
                                    .addLast("length decoder",
                                            new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,2,0,2))
                                    /**
                                     * 消息解码器，将收到的消息进行转换
                                     */
                                    .addLast("logMessage decoder", new LogDecoder())
                                    /**
                                     * 长度编码器，防止粘包拆包
                                     */
                                    .addLast("length encoder",new LengthFieldPrepender(2))
                                    /**
                                     * 消息编码器，将发送的消息进行转换
                                     */
                                    .addLast("logMessage encoder", new StringEncoder())
                                    /**
                                     * 自定义事件处理类
                                     */
                                    .addLast("base handler",new ServerHandler());

                        }
                    })
                    //TCP连接参数,第一、二次握手队列大小
                    .option(ChannelOption.SO_BACKLOG, logServerConfig.getSoBacklog())
                    //保持连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //发送缓冲区大小
                    .option(ChannelOption.SO_SNDBUF, logServerConfig.getSoSndbuf())
                    //接收缓冲区大小
                    .option(ChannelOption.SO_RCVBUF, logServerConfig.getSoRcvbbuf());
            //绑定端口，进行监听
            ChannelFuture channelFuture = serverBootstrap.bind(logServerConfig.getPort()).sync();
            log.info("服务端启动成功.");
            //关闭前阻塞
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e ){
            log.error("服务端启动失败。error={}",e.getMessage(),e);
        }finally {
            //重试机制
            retryStart();
            start1();
        }

    }

    /**
     * 重试机制
     */
    private  void retryStart(){
        //重试机制,如果重试次数超过限制。暂停x秒后再重试
        try {
            if(++LogServiceBootstrap.RETRY_NUM > logServerConfig.getRetryNum()){
                LogServiceBootstrap.RETRY_NUM = 0;
                Thread.sleep(logServerConfig.getStopTime());
            }
        } catch (InterruptedException e) {
            log.error("尝试重启失败。线程sleep error。error={}",e.getMessage(),e);
        }
    }
}
