package com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        //接收到数据，并处理
        System.out.println("MessageProtocol's id:" + msg.getLen());
        System.out.println("MessageProtocol's content:" + new String(msg.getContent(), StandardCharsets.UTF_8));

        System.out.println("服务器接收到的消息包数量:" + ++this.count);
        //返回客户端数据
        String response = UUID.randomUUID().toString();
        int length = response.getBytes("utf-8").length;
        byte[] content = response.getBytes("utf-8");
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setContent(content);
        messageProtocol.setLen(length);
        ctx.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
