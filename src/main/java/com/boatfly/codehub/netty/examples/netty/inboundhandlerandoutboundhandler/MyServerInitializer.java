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

        pipeline.addLast(new MyServerHandler());
    }
}
