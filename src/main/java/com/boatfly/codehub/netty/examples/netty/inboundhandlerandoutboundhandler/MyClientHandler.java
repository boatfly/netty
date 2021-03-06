package com.boatfly.codehub.netty.examples.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("收到服务器端返回的信息:" + msg);
    }

    /**
     * 发送数据
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        //ctx.writeAndFlush(123456L);

        //ctx.writeAndFlush(Unpooled.copiedBuffer("武汉加油！", CharsetUtil.UTF_8));

        ctx.writeAndFlush(Unpooled.copiedBuffer("wuhanjiayouwuhanjiayou", CharsetUtil.UTF_8));
    }
}
