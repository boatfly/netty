package com.boatfly.codehub.netty.examples.netty.groupchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class GroupChatServer {
    private int port = 9991;

    public GroupChatServer(int _port) {
        this.port = _port;
    }

    //处理客户端请求
    public void run() {
        //创建BossGroup WorkerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//默认线程数为cpu核数*2 NettyRuntime.availableProcessors() * 2)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder",new StringDecoder())//解码器
                                    .addLast("encoder",new StringEncoder())//编码器
                            .addLast(new GroupChatServerHandler());//加入自用的业务处理handler
                        }
                    });

            System.out.println("netty server is ok!");

            ChannelFuture bindFuture = serverBootstrap.bind(port).sync();
            //监听关闭事件
            ChannelFuture closeFuture = bindFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new GroupChatServer(9990).run();
    }
}
