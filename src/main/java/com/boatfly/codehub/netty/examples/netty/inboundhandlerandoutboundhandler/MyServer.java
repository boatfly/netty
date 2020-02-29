package com.boatfly.codehub.netty.examples.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossgroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workergroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossgroup, workergroup).channel(NioServerSocketChannel.class).childHandler(new MyServerInitializer());

            ChannelFuture future = serverBootstrap.bind(9992).sync();

            ChannelFuture channelFuture = future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossgroup.shutdownGracefully();
            workergroup.shutdownGracefully();
        }
    }
}
