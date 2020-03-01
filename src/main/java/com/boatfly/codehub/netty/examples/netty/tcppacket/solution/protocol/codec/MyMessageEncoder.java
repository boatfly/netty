package com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol.codec;

import com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MyMessageEncoder extends MessageToByteEncoder<MessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyMessageEncoder is called!");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
