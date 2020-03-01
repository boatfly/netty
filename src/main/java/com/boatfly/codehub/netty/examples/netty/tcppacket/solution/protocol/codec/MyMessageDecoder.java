package com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol.codec;

import com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder is called!");
        //将得到的二进制字节码转成MessageProtocol
        int len = in.readInt();
        byte[] content = new byte[len];
        in.readBytes(content);

        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(content);

        out.add(messageProtocol);
    }
}
