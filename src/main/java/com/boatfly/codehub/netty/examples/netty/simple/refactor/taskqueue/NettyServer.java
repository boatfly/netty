package com.boatfly.codehub.netty.examples.netty.simple.refactor.taskqueue;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.IOException;

public class NettyServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        //创建BossGroup WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//默认线程数为cpu核数*2 NettyRuntime.availableProcessors() * 2)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //设置服务器端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, bossGroup)
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器端channel的实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //在此处可以将socketChannel加入一个集合中进行统一管理，之后可以根据特定表示进行召回 @TODO
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });//为workgroup的eventloop对应的管道设置处理器
            System.out.println("server is ready!....");


            //绑定一个端口，并且同步，
            ChannelFuture future = serverBootstrap.bind(9997).sync();

            //注册监听器
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("bind port 9997 success!");
                    }else{
                        System.out.println("bind port 9997 failed!");
                    }
                }
            });

            //对关闭通道进行监听
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
