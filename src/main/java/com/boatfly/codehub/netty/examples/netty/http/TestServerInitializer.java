package com.boatfly.codehub.netty.examples.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个netty提供的httpserverCodec  codec=>[coder - decoder]
        //HttpServerCodec说明：
        //1.netty提供的处理http的编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

    }
}
