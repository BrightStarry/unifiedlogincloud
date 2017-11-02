package com.zuma.netty;

import com.zuma.config.LogClientConfig;
import com.zuma.dto.LogMessage;
import com.zuma.handler.ClientHandler;
import com.zuma.serializer.LogEncoder;
import com.zuma.sender.LogSender;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 12:15
 */
@Slf4j
public class LogClientBootstrap {

    private static EventLoopGroup workLoopGroup;
    private static Integer RETRY_NUM = 0;

    /**
     * 异步启动
     */
    public static void start() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            public void run() {
                    LogClientBootstrap.start1();
            }
        });
    }

    /**
     * 启动
     * @throws Exception
     */
    private static void start1() {
        try {
            //创建线程组
            workLoopGroup = new NioEventLoopGroup();
            //创建引导程序
            Bootstrap bootstrap = new Bootstrap();
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
                                     * 长度解码器，防止粘包拆包
                                     */
                                    .addLast("length decoder",
                                            new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,2,0,2))
                                    /**
                                     * 消息解码器，将收到的消息进行转换
                                     */
                                    .addLast("logMessage decoder", new StringDecoder())
                                    /**
                                     * 长度编码器，防止粘包拆包
                                     */
                                    .addLast("length encoder",new LengthFieldPrepender(2))
                                    /**
                                     * 消息编码器，将发送的消息进行转换
                                     */
                                    .addLast("logMessage encoder", new LogEncoder())
                                    /**
                                     * 超过s秒未触发读取事件，关闭
                                     */
                                    .addLast(new ReadTimeoutHandler(LogClientConfig.TIMEOUT_SECONDS))
                                    /**
                                     * 自定义处理器
                                     */
                                    .addLast(new ClientHandler());
                        }
                    });
            //链接到服务端
            ChannelFuture channelFuture = bootstrap.connect(LogClientConfig.IP,LogClientConfig.PORT).sync();
            log.info("客户端启动成功");
            //关闭前阻塞
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("客户端启动失败.error={}",e.getMessage());
        }finally {

            //重试机制
            retryStart();
            start1();
        }

    }

    /**
     * 重试机制
     */
    private static void retryStart(){
        //重试机制,如果重试次数超过限制。暂停x秒后再重试
        try {
            if(++LogClientBootstrap.RETRY_NUM > LogClientConfig.RETRY_NUM){
                LogClientBootstrap.RETRY_NUM = 0;
                Thread.sleep(LogClientConfig.STOP_TIME);
            }
        } catch (InterruptedException e) {
            log.error("尝试重启失败。线程sleep error。error={}",e.getMessage());
        }
    }

    /**
     * 关闭
     */
    public static void stop() {
        //关闭
        try {
            workLoopGroup.shutdownGracefully();
        } catch (Exception e) {
            log.error("客户端关闭失败!error={}",e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {

        start();
        Thread.sleep(3000);
        log.info("是否可用： {}",LogSender.isAvailable());
        for (int i = 0; i < 10; i++) {
            LogSender.send(LogMessage.builder()
                    .content(i + "  17:19:39.663 [main] DEBUG io.netty.buffer.PooledByteBufAllocator - -Dio.netty.allocator.pageSize: 8192")
                    .date(new Date())
                    .build());
            Thread.sleep(5000);
        }
    }
}
