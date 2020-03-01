package com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol;

import com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol.codec.MyMessageDecoder;
import com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol.codec.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new MyMessageEncoder());
        pipeline.addLast(new MyMessageDecoder());
        pipeline.addLast(new MyClientHandler());
    }
}
