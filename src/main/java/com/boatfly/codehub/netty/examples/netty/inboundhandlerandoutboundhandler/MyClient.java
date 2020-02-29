package com.boatfly.codehub.netty.examples.netty.inboundhandlerandoutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {
    public static void main(String[] args) {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new MyClientInitializer());

            ChannelFuture future = bootstrap.connect("localhost", 9992).sync();
            ChannelFuture sync = future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
