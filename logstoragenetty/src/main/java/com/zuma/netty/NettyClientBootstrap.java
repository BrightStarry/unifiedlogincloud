package com.zuma.netty;

import com.zuma.dto.LogHeader;
import com.zuma.dto.LogMessage;
import com.zuma.handler.ClientHandler;
import com.zuma.serializer.LogDecoder;
import com.zuma.serializer.LogEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 12:15
 */
@Slf4j
public class NettyClientBootstrap {

    public static  ChannelFuture channelFuture;

    public static void start() throws Exception {
        //创建线程组
        EventLoopGroup workLoopGroup = new NioEventLoopGroup();
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
                                .addLast("length decodere",
                                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,2,0,2))
                                .addLast("logMessage decoder", new StringDecoder())
                                .addLast("length encoder",new LengthFieldPrepender(2))
                                .addLast("logMessage encoder", new LogEncoder())
                                //超时
                                .addLast(new ReadTimeoutHandler(Integer.MAX_VALUE))
                                .addLast(new ClientHandler());
                    }
                });
        //链接到服务端
        channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
        log.info("客户端启动成功");

         //关闭
        channelFuture.channel().closeFuture().sync();

        workLoopGroup.close();

    }

    public static void main(String[] args) throws Exception {
        NettyClientBootstrap.start();

    }
}
