package com.boatfly.codehub.netty.examples.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TestServer {
    public static void main(String[] args) {
        //创建BossGroup WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//默认线程数为cpu核数*2 NettyRuntime.availableProcessors() * 2)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, bossGroup)
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器端channel的实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new TestServerInitializer());

            ChannelFuture bindFuture = serverBootstrap.bind(9996).sync();

            ChannelFuture closeFuture = bindFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
