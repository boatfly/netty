package com.boatfly.codehub.netty.examples.netty.heartbeat;

import com.boatfly.codehub.netty.examples.netty.groupchat.GroupChatServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    public static void main(String[] args) {
        //创建BossGroup WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//默认线程数为cpu核数*2 NettyRuntime.availableProcessors() * 2)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .handler(new LoggingHandler(LogLevel.INFO)) //在bossgroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //说明：
                            // IdleStateHandler netty提供的处理空闲状态的处理器
                            // readerIdleTime 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            // writerIdleTime 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            // allIdleTime 表示多长时间没有读或者写，就会发送一个心跳检测包检测是否连接
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                            //加入一个对空闲检测更进一步处理的自定义handler
                            //当IdleStateEvent 触发后，就会传递给管道的下一个处理器
                            //因此自定义检测空闲处理器要紧跟IdleStateHandler
                            //通过调用下一个处理器的 userEventTriggered(读空闲，写空闲，读写空闲) 方法
                            pipeline.addLast(new MyServerHeartBeatHandler());
                        }
                    });

            System.out.println("netty server is ok!");

            ChannelFuture bindFuture = serverBootstrap.bind(8881).sync();
            //监听关闭事件
            ChannelFuture closeFuture = bindFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
