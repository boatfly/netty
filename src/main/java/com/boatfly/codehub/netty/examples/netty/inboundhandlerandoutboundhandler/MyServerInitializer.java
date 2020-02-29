package com.boatfly.codehub.netty.examples.netty.inboundhandlerandoutboundhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //入站handler进行解码MyByteToLongDecoder
        pipeline.addLast(new MyByteToLongDecoder());
        //出站handler进行编码
        pipeline.addLast(new MyLongToByteEncoder());

        pipeline.addLast(new MyServerHandler());
    }
}
