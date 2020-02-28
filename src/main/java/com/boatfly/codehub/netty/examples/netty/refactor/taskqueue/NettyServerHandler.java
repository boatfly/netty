package com.boatfly.codehub.netty.examples.netty.refactor.taskqueue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

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
//        System.out.println("server ctx:" + ctx);
//        // 将msg转成ByteBuf
//        ByteBuf buf = (ByteBuf) msg;
//
//        System.out.println("client sengd msg：" + buf.toString(CharsetUtil.UTF_8));
//        System.out.println("client address is:" + ctx.channel().remoteAddress());

        //将耗时的任务 -> 异步执行
        //方案1：用户程序自定义的普通任务 -> taskQueue
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(7);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！+2", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 模拟加入多个任务到taskqueue中
        ctx.channel().eventLoop().execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(17);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！+3", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //方案2.用户自定义定时任务 -> scheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(() -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！+4", CharsetUtil.UTF_8));
        }, 20, TimeUnit.SECONDS);


        System.out.println("server is go on...");
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
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！+1", CharsetUtil.UTF_8));
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
