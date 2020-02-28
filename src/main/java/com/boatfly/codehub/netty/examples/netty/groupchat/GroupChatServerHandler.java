package com.boatfly.codehub.netty.examples.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channle组，管理所有的channle
    //GlobalEventExecutor 全局事件执行器，单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示连接建立，一旦连接，第一个被执行
     * 将当前channel加入到channelgroup
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //将该client上线的消息推送给其他client
        //channelGroup.writeAndFlush 会遍历所有加入的channle，并发送信息
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + dtf.format(LocalDateTime.now()) + " 加入聊天室！\n");

        //加入channelgroup
        channelGroup.add(ctx.channel());
        System.out.println("channelgroup size:" + channelGroup.size());
    }

    /**
     * 断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //channelGroup.remove(ctx.channel()); 该事件默认执行了remove操作
        //将当前client离线的消息推送到其他在线的客户端
        channelGroup.writeAndFlush("[客户端]" + ctx.channel().remoteAddress() + dtf.format(LocalDateTime.now()) + " 离开了聊天室！\n");
        System.out.println("channelgroup size:" + channelGroup.size());
    }

    /**
     * channel处于活动的状态，提示xxx上线，用于服务端
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了！");
    }

    /**
     * 获取离线事件
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了！");
    }

    /**
     * 读取数据，并转发操作
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + dtf.format(LocalDateTime.now()) + " 发送消息：" + msg + "\n");
            } else {
                channel.writeAndFlush("[自己] " + dtf.format(LocalDateTime.now()) + " 发送消息：" + msg + "\n");
            }
        });

    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
