package com.boatfly.codehub.netty.examples.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler,需要继承netty设定好的某个HandlerAdapter（规范）
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据事件，可以获取客户端发送的消息
     *
     * @param ctx 上下文
     * @param msg 消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx:" + ctx);
        // 将msg转成ByteBuf
        ByteBuf buf = (ByteBuf) msg;

        System.out.println("client sengd msg：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("client address is:" + ctx.channel().remoteAddress());
    }

    /**
     * 读完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //对发送数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理，一般需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
