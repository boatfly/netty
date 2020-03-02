package com.boatfly.codehub.netty.examples.netty.dubborpc.netty;

import com.boatfly.codehub.netty.examples.netty.dubborpc.provider.BoatServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //获取客户端发送的消息，并调用服务端服务
        System.out.println("msg="+msg);
        //客户端调用服务器api时，必须满足约定协议
        //比如，我们可以约定每次消息都必须以某个字符串开头，譬如："BoatService#echo#"
        if(msg.toString().startsWith("BoatService#echo#")){
            String rpath = msg.toString().substring(msg.toString().lastIndexOf("#"));
            String result = new BoatServiceImpl().echo(rpath);
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常，关闭通道！" + cause.getMessage());
        ctx.close();
    }
}
