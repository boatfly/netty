package com.boatfly.codehub.netty.examples.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

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
                            //基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加ChunkedWriteHandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());
                            //http数据在传输过程中是分段的，HttpObjectAggregator将多个段聚合起来
                            //当浏览器发送大数据时，会发出多次请求的原因
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            //对于websocket，它的数据是以 帧（frame）的形式传递
                            //可以看到websocketframe 有6个子类
                            //浏览器请求时，ws://localhost:8880/xxx 表示请求的uri
                            //WebSocketServerProtocolHandler 核心功能是将http协议 升级 为ws协议 保持长连接
                            pipeline.addLast(new WebSocketServerProtocolHandler("/groupchat"));// 地址和前端调用保持一致

                            //自定义handler
                            pipeline.addLast(new MyTextWebSocketFrameHandler());

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
