package com.zuma.netty;

import com.zuma.handler.ServerHandler;
import com.zuma.serializer.LogDecoder;
import com.zuma.serializer.LogEncoder;
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
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * author:Administrator
 * datetime:2017/10/31 0031 10:08
 *
 * 服务端
 */
@Slf4j
public class NettyServiceBootstrap {
    //服务启动
    public static void start() throws InterruptedException {
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
                                .addLast("length decodere",
                                        new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,2,0,2))
                                .addLast("logMessage decoder", new LogDecoder())
                                .addLast("length encoder",new LengthFieldPrepender(2))
                                .addLast("logMessage encoder", new StringEncoder())
                                .addLast(new ServerHandler());

                    }
                })
                //TCP连接参数,第一、二次握手队列大小
                .option(ChannelOption.SO_BACKLOG, 128)
                //保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //发送缓冲区大小
                .option(ChannelOption.SO_SNDBUF, 32 * 1024)
                //接收缓冲区大小
                .option(ChannelOption.SO_RCVBUF, 32 * 1024);
        //绑定端口，进行监听
        ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
        log.info("服务端启动成功.");
        //关闭前阻塞
        channelFuture.channel().closeFuture().sync();
        //关闭线程组
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        NettyServiceBootstrap.start();
    }
}
