package com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常信息：" + cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送10条数据 "武汉加油，天佑中华！"
        for (int i = 0; i < 5; i++) {
            String msg = "武汉加油，天佑中华！";
            byte[] content = msg.getBytes(StandardCharsets.UTF_8);
            int len = msg.getBytes(StandardCharsets.UTF_8).length;
            //创建协议包
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageProtocol msg) throws Exception {
        System.out.println("收到从服务器端回送的消息：");
        System.out.println("MessageProtocol' id:" + msg.getLen());
        System.out.println("MessageProtocol' content:" + new String(msg.getContent(), StandardCharsets.UTF_8));
    }
}
